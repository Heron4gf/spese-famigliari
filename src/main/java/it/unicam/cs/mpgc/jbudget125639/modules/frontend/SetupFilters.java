package it.unicam.cs.mpgc.jbudget125639.modules.frontend;

import it.unicam.cs.mpgc.jbudget125639.filters.EmptyFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.AbstractModule;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

import java.util.Collection;
import java.util.List;

public class SetupFilters extends AbstractModule {

    private final Collection<FilterConfig<? extends IFilter>> filters;
    private final Runnable onFilterChange;

    public SetupFilters(Collection<FilterConfig<? extends IFilter>> filters, Runnable onFilterChange) {
        this.filters = filters;
        this.onFilterChange = onFilterChange;
    }

    @Override
    protected void internalLoad() {
        filters.forEach(this::setupFilter);
    }

    private <T extends IFilter> void setupFilter(FilterConfig<T> config) {
        var items = FXCollections.<T>observableArrayList();
        @SuppressWarnings("unchecked")
        T empty = (T) new EmptyFilter(config.prompt());
        items.add(empty);
        items.addAll(List.of(config.values()));
        
        config.comboBox().setItems(items);
        config.comboBox().setPromptText(config.prompt());
        config.comboBox().valueProperty().addListener((obs, oldVal, newVal) -> onFilterChange.run());
    }

    @Override
    protected void internalUnload() {}

    @Override
    public String name() {
        return "SetupFilters";
    }

    public record FilterConfig<T extends IFilter>(ComboBox<T> comboBox, T[] values, String prompt) {}
}
