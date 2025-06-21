package it.unicam.cs.mpgc.jbudget125639.modules.headerbarinit;

import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.dates.TimeSpan;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.AbstractModule;
import javafx.scene.control.ComboBox;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
/*
 * modulo per definire i filtri delle combobox con azioni, label e nomi
 */
@RequiredArgsConstructor
public class FiltersComboBoxInitModule extends AbstractModule {

    private final ComboBox<String> timeSpanComboBox;
    private final ComboBox<String> directionComboBox;
    private final ComboBox<String> tagsComboBox;
    private final Runnable onFiltersChanged;

    private static final String ALL_TIMESPAN_LABEL = "Tutti i periodi";
    private static final String ALL_DIRECTIONS_LABEL = "Tutte le direzioni";
    private static final String ALL_TAGS_LABEL = "Tutti i tag";

    @Override
    protected void internalLoad() {
        setupComboBox(
                timeSpanComboBox,
                "Filtra per periodo",
                ALL_TIMESPAN_LABEL,
                () -> List.of(TimeSpan.values()),
                TimeSpan::name
        );

        setupComboBox(
                directionComboBox,
                null,
                ALL_DIRECTIONS_LABEL,
                () -> List.of(TransactionDirection.values()),
                TransactionDirection::name
        );

        setupComboBox(
                tagsComboBox,
                "Filtra per tag",
                ALL_TAGS_LABEL,
                () -> List.of(NamedTag.values()),
                NamedTag::getName
        );
    }

    private <T> void setupComboBox(
            ComboBox<String> comboBox,
            String prompt,
            String defaultLabel,
            Supplier<List<T>> valuesSupplier,
            Function<T, String> mapper
    ) {
        comboBox.getItems().clear();
        comboBox.getItems().add(defaultLabel);
        valuesSupplier.get().stream()
                .map(mapper)
                .forEach(comboBox.getItems()::add);
        comboBox.setValue(defaultLabel);
        if (prompt != null) {
            comboBox.setPromptText(prompt);
        }
        comboBox.setOnAction(e -> onFiltersChanged.run());
    }

    @Override
    protected void internalUnload() {
        timeSpanComboBox.setOnAction(null);
        directionComboBox.setOnAction(null);
        tagsComboBox.setOnAction(null);
    }

    @Override
    public String name() {
        return "FiltersComboBoxInitModule";
    }
}