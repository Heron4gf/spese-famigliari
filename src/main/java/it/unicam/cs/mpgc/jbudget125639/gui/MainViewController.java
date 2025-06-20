package it.unicam.cs.mpgc.jbudget125639.gui;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.dates.TimeSpan;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.gui.constants.UIConfig;
import it.unicam.cs.mpgc.jbudget125639.gui.services.*;
import it.unicam.cs.mpgc.jbudget125639.gui.services.validation.ValidationException;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.AbstractModule;
import it.unicam.cs.mpgc.jbudget125639.modules.frontend.SetupFilters;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.modules.frontend.UIComponentsModule;
import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
public class MainViewController {

    // FXML Components
    @FXML private ComboBox<String> viewsComboBox;
    @FXML private VBox filtersPane;

    @FXML private ComboBox<IFilter> timeSpanFilterComboBox;
    @FXML private ComboBox<IFilter> directionFilterComboBox;
    @FXML private ComboBox<IFilter> tagFilterComboBox;

    @FXML private VBox detailsPane;
    @FXML private Label viewNameLabel;
    @FXML private Label viewBalanceLabel;
    @FXML private TableView<Transaction> transactionsTableView;
    @FXML private VBox userControlsPane;
    @FXML private TextField transactionDescriptionField;
    @FXML private TextField transactionAmountField;
    @FXML private ChoiceBox<Currency> transactionCurrencyChoiceBox;
    @FXML private ChoiceBox<TransactionDirection> transactionDirectionChoiceBox;
    @FXML private ListView<NamedTag> transactionTagsListView;

    private ServiceFactory.ServiceBundle services;
    private List<IFilter> currentFilters = new ArrayList<>();

    public void initializeManager(@NonNull ModulesManager modulesManager) {
        // Initialize services first
        ServiceFactory serviceFactory = new ServiceFactory(modulesManager);
        this.services = serviceFactory.createServiceBundle();
        
        populateViewsList();
        setupEventListeners();
        updateDetailsForCurrentView();

        // Initialize setupFilters after FXML injection
        SetupFilters setupFilters = new SetupFilters(
                List.of(
                        new SetupFilters.FilterConfig<>(timeSpanFilterComboBox, TimeSpan.values(), "Tutti i periodi"),
                        new SetupFilters.FilterConfig<>(directionFilterComboBox, TransactionDirection.values(), "Tutte le direzioni"),
                        new SetupFilters.FilterConfig<>(tagFilterComboBox, NamedTag.values(), "Tutti i tag")
                ),
                this::applyFiltersOnChange
        );

        List<AbstractModule> modulesToLoad = List.of(
                setupFilters,
                new UIComponentsModule(transactionsTableView,
                        transactionCurrencyChoiceBox,
                        transactionDirectionChoiceBox,
                        transactionTagsListView,
                        transactionDescriptionField,
                        transactionAmountField)
        );

        modulesToLoad.forEach(modulesManager::addAndLoad);
    }

    private void applyFiltersOnChange() {
        currentFilters.clear();
        currentFilters.addAll(List.of(
                timeSpanFilterComboBox.getValue(),
                directionFilterComboBox.getValue(),
                tagFilterComboBox.getValue()
        ));
        updateHeaderAndTable();
    }

    private void setupEventListeners() {
        viewsComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> handleViewSelection(newVal)
        );
    }

    private void handleViewSelection(String viewName) {
        if (viewName != null) {
            services.viewService.setCurrentView(viewName);
            updateDetailsForCurrentView();
        }
    }

    private void populateViewsList() {
        viewsComboBox.setItems(services.viewService.getViewNames());
        viewsComboBox.setPrefWidth(UIConfig.VIEWS_LIST_WIDTH.getValue());
    }

    private void updateDetailsForCurrentView() {
        filtersPane.setVisible(true);
        
        updateHeaderAndTable();
        updateTransactionControls();
        detailsPane.setVisible(true);
    }

    private void updateHeaderAndTable() {
        viewNameLabel.setText(services.viewService.getFormattedViewDetails());
        viewBalanceLabel.setText(services.viewService.getFormattedBalance());
        
        // Apply current filters to get filtered transactions
        IFilter[] filtersArray = currentFilters.toArray(new IFilter[0]);
        var transactions = FXCollections.observableArrayList(
                services.viewService.getCurrentView().getFiltered(filtersArray)
        );
        transactionsTableView.setItems(transactions);
    }

    private void updateTransactionControls() {
        boolean isUserView = services.viewService.isCurrentViewUser();
        userControlsPane.setVisible(true);
        
        setControlsEnabled(isUserView);
    }

    private void setControlsEnabled(boolean enabled) {
        List.of(transactionDescriptionField, transactionAmountField, transactionCurrencyChoiceBox, 
                transactionDirectionChoiceBox, transactionTagsListView)
            .forEach(control -> control.setDisable(!enabled));
    }

    @FunctionalInterface
    private interface ThrowingOperation {
        void execute() throws Exception;
    }

    private void executeWithErrorHandling(ThrowingOperation operation) {
        try {
            operation.execute();
        } catch (ValidationException e) {
            services.dialogService.showError(e);
        } catch (NumberFormatException e) {
            services.dialogService.showError("Importo non valido. Inserire un numero.");
        } catch (Exception e) {
            services.dialogService.showError("Errore: " + e.getMessage());
        }
    }

    // Event Handlers

    @FXML
    private void handleAddNewUser() {
        executeWithErrorHandling(() -> {
            Optional<String> userName = services.dialogService.showNewUserDialog();
            if (userName.isPresent()) {
                User newUser = services.userService.createAndAddUser(userName.get());
                refreshViewsAndSelect(newUser.getName());
            }
        });
    }

    @FXML
    private void handleAddTransaction() {
        if (!services.viewService.isCurrentViewUser()) return;
        
        executeWithErrorHandling(() -> {
            User user = services.viewService.getCurrentViewAsUser();
            Transaction transaction = createTransactionFromInput();
            
            services.transactionService.addTransactionToUser(user, transaction);
            updateDetailsForCurrentView();
            clearTransactionFields();
        });
    }

    private Transaction createTransactionFromInput() throws ValidationException {
        double amount = Double.parseDouble(transactionAmountField.getText());
        Currency currency = transactionCurrencyChoiceBox.getValue();
        TransactionDirection direction = transactionDirectionChoiceBox.getValue();
        String description = transactionDescriptionField.getText();
        
        // Get selected tags
        List<NamedTag> selectedTags = transactionTagsListView.getSelectionModel().getSelectedItems();
        
        return services.transactionService.createTransactionWithTags(direction, amount, currency, description, selectedTags);
    }

    @FXML
    private void handleDeleteTransaction() {
        Transaction selectedTransaction = transactionsTableView.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            services.dialogService.showError("Nessuna transazione selezionata.");
            return;
        }

        if (services.dialogService.confirmTransactionDeletion()) {
            try {
                User user = services.viewService.getCurrentViewAsUser();
                services.transactionService.removeTransactionFromUser(user, selectedTransaction);
                updateDetailsForCurrentView();
            } catch (IllegalStateException e) {
                services.dialogService.showError("Impossibile eliminare transazione da questa vista.");
            }
        }
    }

    @FXML
    private void handleDeleteView() {
        String selectedViewName = viewsComboBox.getSelectionModel().getSelectedItem();
        if (selectedViewName == null) {
            services.dialogService.showError("Nessuna vista selezionata.");
            return;
        }

        if (services.dialogService.confirmViewDeletion(selectedViewName)) {
            services.viewService.deleteView(selectedViewName);
            refreshViewsAndSelect(null);
        }
    }

    private void refreshViewsAndSelect(String viewNameToSelect) {
        populateViewsList();
        if (viewNameToSelect != null) {
            viewsComboBox.getSelectionModel().select(viewNameToSelect);
        }
        updateDetailsForCurrentView();
    }

    private void clearTransactionFields() {
        transactionDescriptionField.clear();
        transactionAmountField.clear();
        transactionTagsListView.getSelectionModel().clearSelection();
    }
}
