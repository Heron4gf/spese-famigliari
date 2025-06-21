package it.unicam.cs.mpgc.jbudget125639.gui.components;

import it.unicam.cs.mpgc.jbudget125639.filters.EmptyFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.dates.TimeSpan;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.gui.builders.ComboKey;
import it.unicam.cs.mpgc.jbudget125639.gui.builders.NodeBuilder;
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
import lombok.*;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Componente che rappresenta la barra di intestazione (Header), contenente i filtri e la selezione utente.
 * La sua istanza viene creata tramite il builder generato da Lombok, che richiede tutte le dipendenze necessarie.
 */
@Getter
public class HeaderBarComponent implements NodeBuilder {
    public static final String ADD_USER_LABEL = "Aggiungi utente...";

    private final HBox container = new HBox(20);
    private final EnumMap<ComboKey, ComboBox<String>> comboBoxes = new EnumMap<>(ComboKey.class);

    {
        // Inizializzatore d'istanza per popolare la mappa delle ComboBox
        comboBoxes.put(ComboKey.TIMESPAN, new ComboBox<>());
        comboBoxes.put(ComboKey.DIRECTION, new ComboBox<>());
        comboBoxes.put(ComboKey.TAGS, new ComboBox<>());
        comboBoxes.put(ComboKey.USER, new ComboBox<>());
    }

    /**
     * Costruisce il componente HeaderBar, inizializzando i suoi moduli e la UI.
     * Utilizza l'annotazione @Builder di Lombok per generare un builder sicuro.
     *
     * @param onUserChanged Callback da invocare quando la selezione dell'utente cambia. Non può essere nullo.
     * @param onFiltersChanged Callback da invocare quando uno dei filtri cambia. Non può essere nullo.
     * @param modulesManager Il gestore dei moduli per l'iniezione delle dipendenze. Non può essere nullo.
     */
    @Builder
    public HeaderBarComponent(@NonNull Consumer<String> onUserChanged,
                              @NonNull Runnable onFiltersChanged,
                              @NonNull ModulesManager modulesManager) {
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

    private ComboBox<String> combo(ComboKey key) {
        return comboBoxes.get(key);
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
     * Aggiorna la lista degli utenti nella ComboBox, aggiungendo i nomi forniti,
     * un'opzione vuota e una voce per aggiungere un nuovo utente.
     *
     * @param viewNames elenco dei nomi utente da visualizzare.
     */
    public void updateUserList(List<String> viewNames) {
        ComboBox<String> userBox = combo(ComboKey.USER);
        ObservableList<String> items = FXCollections.observableArrayList();

        if (viewNames != null && !viewNames.isEmpty()) {
            items.addAll(viewNames);
            userBox.setValue(viewNames.getFirst());
        }

        items.add(""); // Separatore visivo
        items.add(ADD_USER_LABEL);

        userBox.setItems(items);
    }

    public void setCurrentUser(String userName) {
        combo(ComboKey.USER).setValue(userName);
    }

    /**
     * Raccoglie i filtri correntemente selezionati dalle ComboBox nella UI.
     *
     * @return una lista di filtri {@link IFilter}. Se nessun filtro è applicato,
     *         restituisce una lista contenente un {@link EmptyFilter}.
     */
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
            filters.add(new EmptyFilter("Nessun filtro applicato"));
        }

        return filters;
    }

    /**
     * Estrae un filtro da una ComboBox, ignorando il valore se corrisponde all'etichetta "tutti".
     */
    private <E extends IFilter> E extractEnumFilter(ComboBox<String> comboBox, String allLabel, Function<String, E> parser) {
        String value = comboBox.getValue();
        return (value == null || value.equals(allLabel)) ? null :
                tryCatch(parser, value);
    }

    /**
     * Applica in modo sicuro una funzione che potrebbe lanciare una {@link IllegalArgumentException}.
     * Se l'applicazione della funzione ha successo, restituisce il risultato. Altrimenti, restituisce {@code null}.
     *
     * @param fn    funzione da applicare
     * @param input valore di input
     * @return il risultato della funzione o {@code null} in caso di eccezione.
     */
    private <T, R> R tryCatch(Function<T, R> fn, T input) {
        try {
            return fn.apply(input);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    /**
     * Definisce le specifiche per ogni filtro disponibile nella UI.
     * Associa una chiave, un'etichetta per l'opzione "tutti" e una funzione di parsing.
     */
    @Accessors(fluent = true)
    @RequiredArgsConstructor
    @Getter
    private enum FilterSpec {
        TIMESPAN(ComboKey.TIMESPAN, "Tutto il tempo", TimeSpan::valueOf),
        DIRECTION(ComboKey.DIRECTION, "Tutte", TransactionDirection::valueOf),
        TAGS(ComboKey.TAGS, "Tutti i tag", NamedTag::fromName);

        private final ComboKey key;
        private final String allLabel;
        private final Function<String, ? extends IFilter> parser;
    }
}