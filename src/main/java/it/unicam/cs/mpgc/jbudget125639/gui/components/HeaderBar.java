package it.unicam.cs.mpgc.jbudget125639.gui.components;

import it.unicam.cs.mpgc.jbudget125639.filters.EmptyFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.dates.TimeSpan;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.gui.services.ServiceFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.controlsfx.control.CheckComboBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class HeaderBar {

    public static final String ADD_USER_LABEL = "Aggiungi Utente...";
    private static final String ALL_DIRECTIONS_LABEL = "Tutte";
    private static final String ALL_TIMESPAN_LABEL = "Tutto il tempo";
    private static final String ALL_TAGS_LABEL = "Tutti i tag";

    private final HBox container;
    private final ComboBox<String> timeSpanComboBox;
    private final ComboBox<String> directionComboBox;
    private final ComboBox<String> tagsComboBox;
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

        // TimeSpan dropdown for date filtering
        timeSpanComboBox = new ComboBox<>();
        timeSpanComboBox.setPromptText("Filtra per periodo");
        timeSpanComboBox.getItems().add(ALL_TIMESPAN_LABEL);
        Arrays.stream(TimeSpan.values()).forEach(timeSpan -> 
            timeSpanComboBox.getItems().add(timeSpan.name())
        );
        timeSpanComboBox.setValue(ALL_TIMESPAN_LABEL);
        timeSpanComboBox.setOnAction(e -> onFiltersChanged.run());

        // TransactionDirection dropdown using enum names
        directionComboBox = new ComboBox<>();
        directionComboBox.getItems().add(ALL_DIRECTIONS_LABEL);
        Arrays.stream(TransactionDirection.values()).forEach(direction -> 
            directionComboBox.getItems().add(direction.name())
        );
        directionComboBox.setValue(ALL_DIRECTIONS_LABEL);
        directionComboBox.setOnAction(e -> onFiltersChanged.run());

        // NamedTag dropdown using getName()
        tagsComboBox = new ComboBox<>();
        tagsComboBox.setPromptText("Filtra per tag");
        tagsComboBox.getItems().add(ALL_TAGS_LABEL);
        Arrays.stream(NamedTag.values()).forEach(namedTag -> 
            tagsComboBox.getItems().add(namedTag.getName())
        );
        tagsComboBox.setValue(ALL_TAGS_LABEL);
        tagsComboBox.setOnAction(e -> onFiltersChanged.run());

        HBox filtersBox = new HBox(10, timeSpanComboBox, directionComboBox, tagsComboBox);
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
        userComboBox.getItems().add(ADD_USER_LABEL);
        
        if (!viewNames.isEmpty()) {
            userComboBox.setValue(viewNames.get(0));
        }
    }

    public void setCurrentUser(String userName) {
        userComboBox.setValue(userName);
    }

    public List<IFilter> getCurrentFilters() {
        List<IFilter> filters = new ArrayList<>();
        
        // TimeSpan filter
        String selectedTimeSpan = timeSpanComboBox.getValue();
        if (selectedTimeSpan != null && !ALL_TIMESPAN_LABEL.equals(selectedTimeSpan)) {
            try {
                TimeSpan timeSpan = TimeSpan.valueOf(selectedTimeSpan);
                filters.add(timeSpan);
            } catch (IllegalArgumentException e) {
                // Invalid timespan, ignore
            }
        }
        
        // TransactionDirection filter using enum
        String direction = directionComboBox.getValue();
        if (direction != null && !ALL_DIRECTIONS_LABEL.equals(direction)) {
            try {
                TransactionDirection dir = TransactionDirection.valueOf(direction);
                filters.add(dir);
            } catch (IllegalArgumentException e) {
                // Invalid direction, ignore
            }
        }
        
        // NamedTag filter using getName()
        String selectedTagName = tagsComboBox.getValue();
        if (selectedTagName != null && !ALL_TAGS_LABEL.equals(selectedTagName)) {
            NamedTag namedTag = getNamedTagByName(selectedTagName);
            if (namedTag != null) {
                filters.add(namedTag);
            }
        }
        
        // Use EmptyFilter as default if no filters are applied
        if (filters.isEmpty()) {
            filters.add(new EmptyFilter("No filters applied"));
        }
        
        return filters;
    }


    private NamedTag getNamedTagByName(String name) {
        return Arrays.stream(NamedTag.values())
                .filter(namedTag -> namedTag.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Node getNode() {
        return container;
    }
}
