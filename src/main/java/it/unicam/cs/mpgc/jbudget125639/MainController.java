package it.unicam.cs.mpgc.jbudget125639;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.Tag;
import it.unicam.cs.mpgc.jbudget125639.model.TransactionService;
import it.unicam.cs.mpgc.jbudget125639.ui.MainViewModel;
import it.unicam.cs.mpgc.jbudget125639.ui.NavigationService;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import it.unicam.cs.mpgc.jbudget125639.views.ViewsHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections; // Import necessario
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainController {

    @FXML private ListView<View> viewsListView;
    @FXML private Label currentViewNameLabel;
    @FXML private Label balanceLabel;
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private Button addTransactionButton;
    @FXML private ComboBox<String> directionFilterComboBox;
    @FXML private ComboBox<String> tagFilterComboBox;
    @FXML private ComboBox<String> timeSpanFilterComboBox;

    private MainViewModel viewModel;
    private TransactionService transactionService;
    private final NavigationService navigationService;

    public MainController() {
        this.navigationService = new NavigationService();
    }

    public void initModel(ViewsHandler viewsHandler, TransactionService transactionService) {
        if (this.viewModel != null) {
            return;
        }
        this.transactionService = transactionService;
        this.viewModel = new MainViewModel(viewsHandler);
        bindViewModel(viewsHandler);
    }

    @FXML
    public void initialize() {
        setupTransactionsTable();
        setupViewsListView();
    }

    private void bindViewModel(ViewsHandler viewsHandler) {
        // CORREZIONE: Convertiamo la Collection in un ObservableList.
        viewsListView.setItems(FXCollections.observableArrayList(viewsHandler.getViews()));

        viewsListView.getSelectionModel().select(viewModel.getSelectedView().get());

        currentViewNameLabel.textProperty().bind(viewModel.getCurrentViewName());
        balanceLabel.textProperty().bind(viewModel.getBalanceText());
        balanceLabel.styleProperty().bind(viewModel.getBalanceStyle());

        transactionsTable.setItems(viewModel.getTransactions());

        directionFilterComboBox.setItems(viewModel.getDirectionFilterOptions());
        tagFilterComboBox.setItems(viewModel.getTagFilterOptions());
        timeSpanFilterComboBox.setItems(viewModel.getTimeSpanFilterOptions());

        viewModel.getSelectedDirection().bindBidirectional(directionFilterComboBox.valueProperty());
        viewModel.getSelectedTag().bindBidirectional(tagFilterComboBox.valueProperty());
        viewModel.getSelectedTimeSpan().bindBidirectional(timeSpanFilterComboBox.valueProperty());

        viewModel.getSelectedView().bind(viewsListView.getSelectionModel().selectedItemProperty());
    }

    private void setupViewsListView() {
        viewsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(View item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName().toUpperCase());
            }
        });
    }

    private void setupTransactionsTable() {
        TableColumn<Transaction, String> directionCol = new TableColumn<>("Direction");
        directionCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDirection() == TransactionDirection.IN ? "Income" : "Expense"));

        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAmount().toString()));

        TableColumn<Transaction, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().format(formatter)));

        TableColumn<Transaction, String> tagsCol = new TableColumn<>("Tags");
        tagsCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getAssociatedTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.joining(", "))
        ));

        transactionsTable.getColumns().setAll(directionCol, amountCol, descriptionCol, dateCol, tagsCol);
        transactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    @FXML
    protected void handleClearFilters() {
        viewModel.clearFiltersAndRefresh();
    }

    @FXML
    protected void handleAddTransaction() {
        try {
            Optional<Transaction> newTransaction = navigationService.showAddTransactionDialog(
                    addTransactionButton.getScene().getWindow(),
                    transactionService.getAllUsers(),
                    viewModel.getSelectedView().get()
            );

            newTransaction.ifPresent(transaction -> {
                transactionService.addTransaction(transaction);
                viewModel.updateUI();
            });

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the transaction dialog.");
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleStatsClick() {
        showAlert(Alert.AlertType.INFORMATION, "Statistics Section", "Navigation to statistics is not implemented yet.");
    }

    @FXML
    protected void handleUsersClick() {
        showAlert(Alert.AlertType.INFORMATION, "Users Management", "Navigation to user management is not implemented yet.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}