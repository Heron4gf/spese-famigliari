package it.unicam.cs.mpgc.jbudget125639.gui.components;

import it.unicam.cs.mpgc.jbudget125639.filters.EmptyFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.dates.TimeSpan;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.gui.services.ServiceFactory;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HeaderBar {
    
    private final HBox container;
    private final DatePicker datePicker;
    private final ComboBox<String> directionComboBox;
    private final TextField tagsTextField;
    private final ComboBox<String> userComboBox;
    private final ServiceFactory.ServiceBundle services;

    private final Consumer<String> onUserChanged;
    private final Runnable onFiltersChanged;

    public HeaderBar(ServiceFactory.ServiceBundle services, Consumer<String> onUserChanged, Runnable onFiltersChanged) {
        this.services = services;
        this.onUserChanged = onUserChanged;
        this.onFiltersChanged = onFiltersChanged;
        
        container = new HBox(20);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(10, 20, 10, 20));
        container.getStyleClass().add("header");

        datePicker = new DatePicker();
        datePicker.setPromptText("Filtra per data");
        datePicker.setOnAction(e -> onFiltersChanged.run());

        directionComboBox = new ComboBox<>();
        directionComboBox.getItems().addAll("Tutte", "IN", "OUT");
        directionComboBox.setValue("Tutte");
        directionComboBox.setOnAction(e -> onFiltersChanged.run());

        tagsTextField = new TextField();
        tagsTextField.setPromptText("Cerca per tag...");
        tagsTextField.textProperty().addListener((obs, oldVal, newVal) -> onFiltersChanged.run());

        HBox filtersBox = new HBox(10, datePicker, directionComboBox, tagsTextField);
        filtersBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        userComboBox = new ComboBox<>();
        userComboBox.getStyleClass().add("user-combo-box");
        userComboBox.setOnAction(e -> {
            String selected = userComboBox.getValue();
            if (selected != null) {
                onUserChanged.accept(selected);
            }
        });

        refreshUsers();

        container.getChildren().addAll(filtersBox, spacer, userComboBox);
    }

    public void refreshUsers() {
        // This method is kept for backward compatibility but now uses updateUserList
        updateUserList(List.of());
    }
    
    /**
     * Updates the user list in the combo box with the provided view names.
     * This method is called by JBudgetApp to centralize view management.
     * 
     * @param viewNames the list of view names to display
     */
    public void updateUserList(List<String> viewNames) {
        userComboBox.getItems().clear();
        userComboBox.getItems().addAll(viewNames);
        userComboBox.getItems().add("");
        userComboBox.getItems().add("Aggiungi Utente...");
        
        if (!viewNames.isEmpty()) {
            userComboBox.setValue(viewNames.get(0));
        }
    }

    public void setCurrentUser(String userName) {
        userComboBox.setValue(userName);
    }

    public List<IFilter> getCurrentFilters() {
        List<IFilter> filters = new ArrayList<>();
        
        if (datePicker.getValue() != null) {
            // Add date filter logic here if needed
        }
        
        String direction = directionComboBox.getValue();
        if (!"Tutte".equals(direction)) {
            TransactionDirection dir = TransactionDirection.valueOf(direction);
            filters.add(transaction -> transaction.getDirection() == dir);
        }
        
        String tagText = tagsTextField.getText();
        if (tagText != null && !tagText.trim().isEmpty()) {
            filters.add(transaction -> 
                transaction.getAssociatedTags()
                    .anyMatch(tag -> tag.toString().toLowerCase().contains(tagText.toLowerCase()))
            );
        }
        
        if (filters.isEmpty()) {
            filters.add(new EmptyFilter());
        }
        
        return filters;
    }

    public Node getNode() {
        return container;
    }
}
