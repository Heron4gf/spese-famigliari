package it.unicam.cs.mpgc.jbudget125639;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.dates.TimeSpan;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.Tag;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import it.unicam.cs.mpgc.jbudget125639.views.ViewsHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainController {

    @FXML
    private VBox navbar;
    @FXML
    private ListView<View> viewsListView;
    @FXML
    private Label currentViewNameLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private Button statsButton;
    @FXML
    private Button usersButton;

    @FXML
    private Button addTransactionButton;

    @FXML
    private ComboBox<String> directionFilterComboBox;
    @FXML
    private ComboBox<String> tagFilterComboBox;
    @FXML
    private ComboBox<String> timeSpanFilterComboBox;

    private ViewsHandler viewsHandler;
    private View selectedView;
    private ObservableList<Transaction> transactionData = FXCollections.observableArrayList();
    private Collection<User> allUsers;

    public void setViewsHandler(ViewsHandler viewsHandler) {
        this.viewsHandler = viewsHandler;
        // Populate the viewsListView with actual data from viewsHandler
        initializeViewsListContent(); // <--- CORRECTED: Call this method to populate the ListView
        updateUI(); // Perform an initial UI update after the views are set
    }

    @FXML
    public void initialize() {
        setupTransactionsTable();
        setupViewsListViewVisualsAndListener(); // <--- CORRECTED: Method to set up cell factory and listener
        setupFilterComboBoxes();
    }

    // <--- CORRECTED: New method to populate viewsListView once viewsHandler is available
    private void initializeViewsListContent() {
        if (viewsHandler != null) {
            viewsListView.getItems().addAll(viewsHandler.getViews());
            viewsListView.getSelectionModel().select(viewsHandler.getView("global"));
        }
    }

    private void setupViewsListViewVisualsAndListener() {
        viewsListView.setCellFactory(lv -> new ListCell<View>() {
            @Override
            protected void updateItem(View item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getName().toUpperCase());
            }
        });
        viewsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedView = newVal;
                clearFilters(); // Clear existing filters when switching view
                updateUI(); // Update UI for the newly selected view
            }
        });
    }

    private void setupFilterComboBoxes() {
        // Direction Filter
        directionFilterComboBox.getItems().addAll("All", TransactionDirection.IN.name(), TransactionDirection.OUT.name());
        directionFilterComboBox.getSelectionModel().select("All");

        // Tag Filter
        List<String> tagNames = Stream.of(NamedTag.values())
                .map(NamedTag::getName)
                .collect(Collectors.toList());
        tagFilterComboBox.getItems().add("All");
        tagFilterComboBox.getItems().addAll(tagNames);
        tagFilterComboBox.getSelectionModel().select("All");

        // Time Span Filter
        List<String> timeSpanNames = Stream.of(TimeSpan.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        timeSpanFilterComboBox.getItems().add("All");
        timeSpanFilterComboBox.getItems().addAll(timeSpanNames);
        timeSpanFilterComboBox.getSelectionModel().select("All");
    }

    private void setupTransactionsTable() {
        TableColumn<Transaction, String> directionCol = new TableColumn<>("Direction");
        directionCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDirection() == TransactionDirection.IN ? "Income" : "Expense"));
        directionCol.setPrefWidth(80);
        directionCol.setReorderable(false);

        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAmount().toString()));
        amountCol.setPrefWidth(100);
        amountCol.setReorderable(false);

        TableColumn<Transaction, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        descriptionCol.setPrefWidth(200);

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return new SimpleStringProperty(cellData.getValue().getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().format(formatter));
        });
        dateCol.setPrefWidth(90);
        dateCol.setReorderable(false);

        TableColumn<Transaction, String> tagsCol = new TableColumn<>("Tags");
        tagsCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getAssociatedTags().stream()
                        .map(Tag::getName)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("")
        ));
        tagsCol.setPrefWidth(150);

        transactionsTable.getColumns().addAll(directionCol, amountCol, descriptionCol, dateCol, tagsCol);
        transactionsTable.setItems(transactionData);
        transactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void updateUI() {
        if (selectedView != null) {
            currentViewNameLabel.setText(selectedView.getName().toUpperCase());

            List<IFilter> activeFilters = getActiveFilters();
            double total = selectedView.total(activeFilters.toArray(new IFilter[0]));
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
            balanceLabel.setText(currencyFormat.format(total));

            if (total < 0) {
                balanceLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: red;");
            } else {
                balanceLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: green;");
            }

            Collection<Transaction> filteredTransactions = selectedView.ordered(activeFilters.toArray(new IFilter[0]));
            transactionData.clear();
            transactionData.addAll(filteredTransactions);
        }
    }

    private List<IFilter> getActiveFilters() {
        List<IFilter> filters = new ArrayList<>();

        String selectedDirection = directionFilterComboBox.getSelectionModel().getSelectedItem();
        if (selectedDirection != null && !"All".equals(selectedDirection)) {
            filters.add(TransactionDirection.valueOf(selectedDirection));
        }

        String selectedTag = tagFilterComboBox.getSelectionModel().getSelectedItem();
        if (selectedTag != null && !"All".equals(selectedTag)) {
            for (NamedTag nt : NamedTag.values()) {
                if (nt.getName().equalsIgnoreCase(selectedTag)) {
                    filters.add(nt);
                    break;
                }
            }
        }

        String selectedTimeSpan = timeSpanFilterComboBox.getSelectionModel().getSelectedItem();
        if (selectedTimeSpan != null && !"All".equals(selectedTimeSpan)) {
            filters.add(TimeSpan.valueOf(selectedTimeSpan));
        }

        return filters;
    }

    @FXML
    protected void handleApplyFilters() {
        updateUI();
    }

    @FXML
    protected void handleClearFilters() {
        clearFilters();
        updateUI();
    }

    private void clearFilters() {
        directionFilterComboBox.getSelectionModel().select("All");
        tagFilterComboBox.getSelectionModel().select("All");
        timeSpanFilterComboBox.getSelectionModel().select("All");
    }

    @FXML
    protected void handleAddTransaction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-transaction-dialog.fxml"));
            VBox addTransactionRoot = fxmlLoader.load();
            AddTransactionController addTransactionController = fxmlLoader.getController();

            if (allUsers != null) {
                addTransactionController.setAvailableUsers(allUsers);
            }

            if (selectedView instanceof User) {
                addTransactionController.setTargetUser((User) selectedView);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Transaction");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(addTransactionButton.getScene().getWindow());
            dialogStage.setScene(new Scene(addTransactionRoot));
            dialogStage.initStyle(StageStyle.UTILITY);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

            Optional<Transaction> newTransaction = addTransactionController.getNewTransaction();
            if (newTransaction.isPresent()) {
                Transaction addedTransaction = newTransaction.get();

                allUsers.stream()
                        .filter(user -> user.getName().equals(addedTransaction.getUser().getName()))
                        .findFirst()
                        .ifPresent(user -> user.addTransaction(addedTransaction));

                updateUI();
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load add-transaction-dialog.fxml: " + e.getMessage()); // Corrected call
        }
    }

    @FXML
    protected void handleStatsClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Statistics Section");
        alert.setHeaderText(null);
        alert.setContentText("This would navigate to the statistics view for " + (selectedView != null ? selectedView.getName() : "the current view") + ".");
        alert.showAndWait();
    }

    @FXML
    protected void handleUsersClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Users Management");
        alert.setHeaderText(null);
        alert.setContentText("This would navigate to a user management interface (add/edit users).");
        alert.showAndWait();
    }

    // <--- CORRECTED: Added showAlert helper method to MainController
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}