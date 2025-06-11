package it.unicam.cs.mpgc.jbudget125639.ui;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.dates.TimeSpan;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import it.unicam.cs.mpgc.jbudget125639.views.ViewsHandler;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Getter
public class MainViewModel {

    private static final String BALANCE_STYLE_BASE = "-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: ";
    private static final String BALANCE_STYLE_NEGATIVE = BALANCE_STYLE_BASE + "red;";
    private static final String BALANCE_STYLE_POSITIVE = BALANCE_STYLE_BASE + "green;";

    private final ObjectProperty<View> selectedView = new SimpleObjectProperty<>();
    private final ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private final StringProperty currentViewName = new SimpleStringProperty();
    private final StringProperty balanceText = new SimpleStringProperty();
    private final StringProperty balanceStyle = new SimpleStringProperty();

    private final ObservableList<String> directionFilterOptions = FXCollections.observableArrayList();
    private final ObservableList<String> tagFilterOptions = FXCollections.observableArrayList();
    private final ObservableList<String> timeSpanFilterOptions = FXCollections.observableArrayList();

    private final StringProperty selectedDirection = new SimpleStringProperty("All");
    private final StringProperty selectedTag = new SimpleStringProperty("All");
    private final StringProperty selectedTimeSpan = new SimpleStringProperty("All");

    public MainViewModel(ViewsHandler viewsHandler) {
        initializeFilters();
        selectedView.set(viewsHandler.getView("global"));

        selectedView.addListener((obs, oldV, newV) -> resetFiltersAndUpdate());
        selectedDirection.addListener((obs, oldV, newV) -> updateUI());
        selectedTag.addListener((obs, oldV, newV) -> updateUI());
        selectedTimeSpan.addListener((obs, oldV, newV) -> updateUI());

        updateUI();
    }

    private void initializeFilters() {
        directionFilterOptions.addAll("All", TransactionDirection.IN.name(), TransactionDirection.OUT.name());

        tagFilterOptions.add("All");
        tagFilterOptions.addAll(Stream.of(NamedTag.values()).map(NamedTag::getName).toList());

        timeSpanFilterOptions.add("All");
        timeSpanFilterOptions.addAll(Stream.of(TimeSpan.values()).map(Enum::name).toList());
    }

    public void clearFiltersAndRefresh() {
        resetFiltersAndUpdate();
    }

    private void resetFiltersAndUpdate() {
        selectedDirection.set("All");
        selectedTag.set("All");
        selectedTimeSpan.set("All");
        updateUI();
    }

    public void updateUI() {
        View view = selectedView.get();
        if (view == null) return;

        currentViewName.set(view.getName().toUpperCase());

        List<IFilter> activeFilters = getActiveFilters();
        double total = view.total(activeFilters.toArray(new IFilter[0]));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        balanceText.set(currencyFormat.format(total));
        balanceStyle.set(total < 0 ? BALANCE_STYLE_NEGATIVE : BALANCE_STYLE_POSITIVE);

        transactions.setAll(view.ordered(activeFilters.toArray(new IFilter[0])));
    }

    private List<IFilter> getActiveFilters() {
        List<IFilter> filters = new ArrayList<>();

        final String direction = selectedDirection.get();
        if (direction != null && !"All".equals(direction)) {
            filters.add(TransactionDirection.valueOf(direction));
        }

        final String tag = selectedTag.get();
        if (tag != null && !"All".equals(tag)) {
            Stream.of(NamedTag.values())
                    .filter(nt -> nt.getName().equalsIgnoreCase(tag))
                    .findFirst()
                    .ifPresent(filters::add);
        }

        final String timeSpan = selectedTimeSpan.get();
        if (timeSpan != null && !"All".equals(timeSpan)) {
            filters.add(TimeSpan.valueOf(timeSpan));
        }

        return filters;
    }
}