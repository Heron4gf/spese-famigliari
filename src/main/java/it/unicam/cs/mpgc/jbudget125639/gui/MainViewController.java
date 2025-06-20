package it.unicam.cs.mpgc.jbudget125639.gui;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.modules.ViewChoiceHandler;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import it.unicam.cs.mpgc.jbudget125639.modules.GlobalModule;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.stream.Collectors;

@NoArgsConstructor
@SuppressWarnings("unchecked")
public class MainViewController {

    @FXML private ListView<String> viewsListView;
    @FXML private VBox detailsPane;
    @FXML private Label viewNameLabel;
    @FXML private Label viewBalanceLabel;
    @FXML private TableView<Transaction> transactionsTableView;
    @FXML private VBox userControlsPane;
    @FXML private TextField transactionDescriptionField;
    @FXML private TextField transactionAmountField;
    @FXML private ChoiceBox<Currency> transactionCurrencyChoiceBox;
    @FXML private ChoiceBox<TransactionDirection> transactionDirectionChoiceBox;

    private ModulesManager modulesManager;

    private View currentView() {
        return modulesManager.getModule(ViewChoiceHandler.class).selectedView();
    }

    public void initializeManager(@NonNull ModulesManager modulesManager) {
        this.modulesManager = modulesManager;
        initializeUIComponents();
        populateViewsList();
        setupListViewListener();
    }

    private void initializeUIComponents() {
        transactionsTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("direction"));
        transactionsTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("moneyAmount"));
        transactionsTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("description"));
        transactionsTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("date"));

        transactionCurrencyChoiceBox.setItems(FXCollections.observableArrayList(Currency.values()));
        transactionCurrencyChoiceBox.setValue(Currency.EUR);
        transactionDirectionChoiceBox.setItems(FXCollections.observableArrayList(TransactionDirection.values()));
        transactionDirectionChoiceBox.setValue(TransactionDirection.OUT);
    }

    private void populateViewsList() {
        GlobalModule globalModule = modulesManager.getModule(GlobalModule.class);
        viewsListView.setItems(globalModule.getGlobal().getViews()
                .stream()
                .map(View::getName)
                .sorted()
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    private void setupListViewListener() {
        ViewChoiceHandler viewChoiceHandler = modulesManager.getModule(ViewChoiceHandler.class);
        viewsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            viewChoiceHandler.setView(newVal);
            updateDetailsForCurrentView();
        });
    }

    private void updateDetailsForCurrentView() {
        viewNameLabel.setText("Dettagli per: " + currentView().getName());
        double balance = currentView().total(TransactionDirection.IN) - currentView().total(TransactionDirection.OUT);
        viewBalanceLabel.setText(String.format("Saldo: %.2f EUR", balance));
        transactionsTableView.setItems(FXCollections.observableArrayList(currentView().getFiltered()));
        userControlsPane.setVisible(currentView() instanceof User);
        detailsPane.setVisible(true);
    }

    @FXML
    private void handleAddNewUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuovo Utente");
        dialog.setHeaderText("Aggiungi un nuovo utente");
        dialog.setContentText("Nome Utente:");
        dialog.showAndWait().ifPresent(name -> {
            if (name.trim().isEmpty()) return;
            User newUser = new User(name.trim());
            modulesManager.getModule(GlobalModule.class).getGlobal().addView(newUser);
            populateViewsList();
            viewsListView.getSelectionModel().select(newUser.getName());
        });
    }

    @FXML
    private void handleAddTransaction() {
        if (!(currentView() instanceof User user)) return;
        try {
            MoneyAmount moneyAmount = new MoneyAmount(Double.parseDouble(transactionAmountField.getText()), transactionCurrencyChoiceBox.getValue());
            Transaction newTransaction = new Transaction(transactionDirectionChoiceBox.getValue(), moneyAmount, transactionDescriptionField.getText());
            user.addTransaction(newTransaction); // Aggiunge solo alla collezione in memoria dell'utente
            updateDetailsForCurrentView();
            clearTransactionFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore Input", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteTransaction() {
        if (!(currentView() instanceof User user)) return;
        Transaction selectedTransaction = transactionsTableView.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) return;

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Sei sicuro di voler eliminare la transazione?", ButtonType.YES, ButtonType.NO);
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                user.getTransactions().remove(selectedTransaction);
                updateDetailsForCurrentView();
            }
        });
    }

    private void clearTransactionFields() {
        transactionDescriptionField.clear();
        transactionAmountField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}