package it.unicam.cs.mpgc.jbudget125639;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.Tag;
import it.unicam.cs.mpgc.jbudget125639.money.Eur;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AddTransactionController {

    @FXML
    private ComboBox<User> userComboBox;
    @FXML
    private ComboBox<TransactionDirection> directionComboBox;
    @FXML
    private TextField amountField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ListView<Tag> tagsListView;

    private Optional<Transaction> newTransaction = Optional.empty();
    private User preSelectedUser; // User from MainController if a specific user view is active

    // Simple ID generator for new transactions (for demo purposes)
    private static final AtomicInteger transactionIdCounter = new AtomicInteger(100);

    @FXML
    public void initialize() {
        // Populate Direction ComboBox
        directionComboBox.getItems().addAll(TransactionDirection.IN, TransactionDirection.OUT);
        directionComboBox.getSelectionModel().selectFirst();

        // Populate Tags ListView (allowing multiple selections)
        tagsListView.getItems().addAll(NamedTag.values());
        tagsListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        // Set default date to today
        datePicker.setValue(java.time.LocalDate.now());

        // Configure User ComboBox cell display
        userComboBox.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }
        });
        userComboBox.setButtonCell(new ListCell<User>() { // For the selected item display
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName());
            }
        });
    }

    public void setAvailableUsers(Collection<User> users) {
        userComboBox.getItems().addAll(users);
        if (preSelectedUser != null && users.contains(preSelectedUser)) {
            userComboBox.getSelectionModel().select(preSelectedUser);
            userComboBox.setDisable(true); // Disable if pre-selected from main view
        } else if (!users.isEmpty()) {
            userComboBox.getSelectionModel().selectFirst(); // Default select the first user
        }
    }

    public void setTargetUser(User user) {
        this.preSelectedUser = user;
    }

    public Optional<Transaction> getNewTransaction() {
        return newTransaction;
    }

    @FXML
    private void handleAdd() {
        try {
            User selectedUser = userComboBox.getSelectionModel().getSelectedItem();
            if (selectedUser == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please select a user for the transaction.");
                return;
            }

            TransactionDirection direction = directionComboBox.getSelectionModel().getSelectedItem();
            if (direction == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please select a transaction direction.");
                return;
            }

            double amountValue = Double.parseDouble(amountField.getText());
            if (amountValue <= 0) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Amount must be a positive number.");
                return;
            }
            MoneyAmount amount = new MoneyAmount(amountValue, new Eur()); // Assuming EUR for simplicity

            String description = descriptionField.getText().trim();
            if (description.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Description cannot be empty.");
                return;
            }

            java.time.LocalDate localDate = datePicker.getValue();
            if (localDate == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please select a date.");
                return;
            }
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            List<Tag> selectedTags = tagsListView.getSelectionModel().getSelectedItems();
            Set<Tag> associatedTags = new HashSet<>(selectedTags);

            // Create a new Transaction object
            Transaction transaction = new Transaction(
                    selectedUser,
                    transactionIdCounter.getAndIncrement(), // Auto-generate ID for demo
                    direction,
                    amount,
                    description,
                    date,
                    associatedTags
            );

            newTransaction = Optional.of(transaction);
            closeDialog();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid amount. Please enter a valid number (e.g., 123.45).");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        newTransaction = Optional.empty(); // No transaction added
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) amountField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}