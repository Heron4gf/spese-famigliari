package it.unicam.cs.mpgc.jbudget125639.gui.components;

import it.unicam.cs.mpgc.jbudget125639.filters.EmptyFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.dates.TimeSpan;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.modules.headerbarinit.FiltersComboBoxInitModule;
import it.unicam.cs.mpgc.jbudget125639.modules.headerbarinit.InitContainerModule;
import it.unicam.cs.mpgc.jbudget125639.modules.headerbarinit.UserChangedConsumerModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HeaderBar {

    @Getter
    private final HBox container = new HBox(20);
    private final EnumMap<ComboKey, ComboBox<String>> comboBoxes = new EnumMap<>(ComboKey.class);
    {
        comboBoxes.put(ComboKey.TIMESPAN, new ComboBox<>());
        comboBoxes.put(ComboKey.DIRECTION, new ComboBox<>());
        comboBoxes.put(ComboKey.TAGS, new ComboBox<>());
        comboBoxes.put(ComboKey.USER, new ComboBox<>());
    }

    private ComboBox<String> combo(ComboKey key) {
        return comboBoxes.get(key);
    }

    public HeaderBar(Consumer<String> onUserChanged, Runnable onFiltersChanged, ModulesManager modulesManager) {
        modulesManager.addAndLoad(
                new InitContainerModule(container),
                new FiltersComboBoxInitModule(
                        combo(ComboKey.TIMESPAN),
                        combo(ComboKey.DIRECTION),
                        combo(ComboKey.TAGS),
                        onFiltersChanged
                ),
                new UserChangedConsumerModule(
                        onUserChanged,
                        combo(ComboKey.USER)
                )
        );

        assembleComponents();
    }

    private void assembleComponents() {
        HBox filtersBox = new HBox(
                10,
                combo(ComboKey.TIMESPAN),
                combo(ComboKey.DIRECTION),
                combo(ComboKey.TAGS)
        );
        filtersBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        container.getChildren().addAll(filtersBox, spacer, combo(ComboKey.USER));
    }

    /**
     * Aggiorna la lista degli utenti nel combo box, aggiungendo i nomi forniti,
     * un'opzione vuota e una voce per aggiungere un nuovo utente.
     *
     * @param viewNames elenco dei nomi utente da visualizzare
     */
    public void updateUserList(List<String> viewNames) {
        ComboBox<String> userBox = combo(ComboKey.USER);
        ObservableList<String> items = FXCollections.observableArrayList();

        if (viewNames != null && !viewNames.isEmpty()) {
            items.addAll(viewNames);
            userBox.setValue(viewNames.getFirst());
        }

        items.add(""); // Separatore
        items.add(LabelKey.ADD_USER.label());

        userBox.setItems(items);
    }

    public void setCurrentUser(String userName) {
        combo(ComboKey.USER).setValue(userName);
    }

    public List<IFilter> getCurrentFilters() {
        List<IFilter> filters = Stream.of(
                        extractEnumFilter(combo(ComboKey.TIMESPAN), LabelKey.ALL_TIMESPAN.label(), TimeSpan::valueOf),
                        extractEnumFilter(combo(ComboKey.DIRECTION), LabelKey.ALL_DIRECTIONS.label(), TransactionDirection::valueOf),
                        extractNamedTagFilter(combo(ComboKey.TAGS), LabelKey.ALL_TAGS.label())
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (filters.isEmpty()) {
            filters.add(new EmptyFilter("No filters applied"));
        }

        return filters;
    }

    private <E extends IFilter> E extractEnumFilter(ComboBox<String> comboBox, String allLabel, Function<String, E> parser) {
        String value = comboBox.getValue();
        if (value != null && !value.equals(allLabel)) {
            try {
                return parser.apply(value);
            } catch (IllegalArgumentException ignored) {
                // valore non valido, restituisce null
            }
        }
        return null;
    }

    private NamedTag extractNamedTagFilter(ComboBox<String> comboBox, String allLabel) {
        String value = comboBox.getValue();
        if (value != null && !value.equals(allLabel)) {
            return getNamedTagByName(value);
        }
        return null;
    }


    private NamedTag getNamedTagByName(String name) {
        return Arrays.stream(NamedTag.values())
                .filter(namedTag -> namedTag.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Accessors(fluent = true)
    @RequiredArgsConstructor
    @Getter
    public enum LabelKey {
        ADD_USER("Aggiungi Utente..."),
        ALL_DIRECTIONS("Tutte"),
        ALL_TIMESPAN("Tutto il tempo"),
        ALL_TAGS("Tutti i tag");

        private final String label;
    }

}
