package it.unicam.cs.mpgc.jbudget125639.gui.builders;

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
import javafx.scene.Node;
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

/**
 * Builder for creating HeaderBar components with required dependencies.
 */
public class HeaderBarBuilder implements ComponentBuilder<HeaderBarBuilder.HeaderBarComponent, HeaderBarBuilder> {
    
    private Consumer<String> onUserChanged;
    private Runnable onFiltersChanged;
    private ModulesManager modulesManager;
    
    /**
     * Sets the callback for when user selection changes.
     * 
     * @param onUserChanged the user change callback
     * @return this builder for method chaining
     */
    public HeaderBarBuilder withUserChangeHandler(Consumer<String> onUserChanged) {
        this.onUserChanged = onUserChanged;
        return self();
    }
    
    /**
     * Sets the callback for when filters change.
     * 
     * @param onFiltersChanged the filters change callback
     * @return this builder for method chaining
     */
    public HeaderBarBuilder withFiltersChangeHandler(Runnable onFiltersChanged) {
        this.onFiltersChanged = onFiltersChanged;
        return self();
    }
    
    /**
     * Sets the modules manager dependency.
     * 
     * @param modulesManager the modules manager
     * @return this builder for method chaining
     */
    public HeaderBarBuilder withModulesManager(ModulesManager modulesManager) {
        this.modulesManager = modulesManager;
        return self();
    }
    
    @Override
    public HeaderBarComponent build() {
        if (onUserChanged == null) {
            throw new IllegalStateException("User change handler is required");
        }
        if (onFiltersChanged == null) {
            throw new IllegalStateException("Filters change handler is required");
        }
        if (modulesManager == null) {
            throw new IllegalStateException("Modules manager is required");
        }
        
        return new HeaderBarComponent(onUserChanged, onFiltersChanged, modulesManager);
    }
    
    @Getter
    public static class HeaderBarComponent implements NodeBuilder {
        public static final String ADD_USER_LABEL = "Aggiungi utente...";

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

        public HeaderBarComponent(Consumer<String> onUserChanged, Runnable onFiltersChanged, ModulesManager modulesManager) {
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

        @Override
        public Node getNode() {
            return container;
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
            items.add(ADD_USER_LABEL);

            userBox.setItems(items);
        }

        public void setCurrentUser(String userName) {
            combo(ComboKey.USER).setValue(userName);
        }

        public List<IFilter> getCurrentFilters() {
            List<IFilter> filters = Arrays.stream(FilterSpec.values())
                    .map(spec -> extractEnumFilter(
                            combo(spec.key()),
                            spec.allLabel(),
                            spec.parser()
                    ))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (filters.isEmpty()) {
                filters.add(new EmptyFilter("No filters applied"));
            }

            return filters;
        }

        private <E extends IFilter> E extractEnumFilter(ComboBox<String> comboBox, String allLabel, Function<String, E> parser) {
            String value = comboBox.getValue();
            return (value == null || value.equals(allLabel)) ? null :
                    tryCatch(parser, value);
        }

        /**
         * Applica in modo sicuro una funzione che potrebbe lanciare un {@link IllegalArgumentException}.
         * <p>
         * Se l'applicazione della funzione ha successo, restituisce il risultato.
         * In caso contrario (eccezione {@code IllegalArgumentException}), restituisce {@code null}.
         * <p>
         * Utile per rendere più leggibile l'applicazione di funzioni enum-based che possono fallire sul parsing.
         *
         * @param fn    funzione da applicare
         * @param input valore di input
         * @param <T>   tipo dell'input
         * @param <R>   tipo del risultato atteso
         * @return il risultato della funzione o {@code null} in caso di eccezione
         */
        private <T, R> R tryCatch(Function<T, R> fn, T input) {
            try {
                return fn.apply(input);
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }

        @Accessors(fluent = true)
        @RequiredArgsConstructor
        @Getter
        private enum FilterSpec {
            TIMESPAN(ComboKey.TIMESPAN, "Tutto il tempo", TimeSpan::valueOf),
            DIRECTION(ComboKey.DIRECTION, "Tutte", TransactionDirection::valueOf),
            TAGS(ComboKey.TAGS, "Tutti i tag", NamedTag::valueOf);

            private final ComboKey key;
            private final String allLabel;
            private final Function<String, ? extends IFilter> parser;
        }
    }
}
