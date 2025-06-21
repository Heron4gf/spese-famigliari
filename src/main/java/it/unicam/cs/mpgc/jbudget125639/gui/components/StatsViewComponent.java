package it.unicam.cs.mpgc.jbudget125639.gui.components;

import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils;
import it.unicam.cs.mpgc.jbudget125639.gui.builders.NodeBuilder;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Componente per la visualizzazione delle statistiche tramite grafici.
 * La sua istanza viene creata tramite il builder generato da Lombok.
 * Esempio: StatsViewComponent.builder().view(myView).build();
 */
@Getter
public class StatsViewComponent implements NodeBuilder {
    private VBox container;
    private final EnumMap<ChartType, Chart> charts = new EnumMap<>(ChartType.class);
    private View currentView;

    /**
     * Costruttore del componente delle statistiche.
     * Annotato con @Builder per permettere una creazione fluida.
     *
     * @param view La vista iniziale da cui trarre i dati per i grafici. Può essere null.
     */
    @Builder
    public StatsViewComponent(View view) {
        this.currentView = view;
        initCharts();
        initContainer();
        GridPane chartsGrid = buildChartsGrid();
        container.getChildren().add(chartsGrid);
        updateData();
    }

    private void initCharts() {
        charts.put(ChartType.PIE, new PieChart());
        charts.put(ChartType.BAR, new BarChart<>(new CategoryAxis(), new NumberAxis()));

        if (charts.get(ChartType.PIE) instanceof PieChart pie) {
            pie.setLabelsVisible(false);
        }
    }

    private void initContainer() {
        container = new VBox(30);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(20, 0, 0, 0));
    }

    private GridPane buildChartsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        Arrays.stream(ChartType.values()).forEach(type -> {
            Chart chart = charts.get(type);
            chart.setLegendVisible(true);
            chart.setTitle("");

            Label label = createSectionTitle(type.getLabelText());
            VBox chartContainer = BuilderUtils.createStyledVBox(10, Pos.CENTER);
            chartContainer.getChildren().addAll(label, chart);

            grid.add(chartContainer, type.getColumnIndex(), 0);
        });

        return grid;
    }

    /**
     * Aggiorna i dati dei grafici utilizzando la vista e i filtri correnti.
     */
    public void updateData() {
        if (currentView != null) {
            updatePieChart();
            updateBarChart();
        }
    }

    /**
     * Aggiorna la vista di riferimento e ricarica i dati dei grafici.
     *
     * @param view la nuova vista da utilizzare.
     */
    public void updateData(View view) {
        this.currentView = view;
        updateData();
    }

    private void updatePieChart() {
        if (currentView == null) return;

        Map<NamedTag, Double> sums = Arrays.stream(NamedTag.values())
                .map(tag -> Map.entry(tag, currentView.total(tag, TransactionDirection.OUT)))
                .filter(entry -> entry.getValue() != 0d)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        updatePieChart(sums);
    }

    private void updatePieChart(Map<NamedTag, Double> sums) {
        List<PieChart.Data> data = Optional.of(
                        sums.entrySet().stream()
                                .map(e -> new PieChart.Data(e.getKey().name(), e.getValue()))
                                .toList()
                )
                .filter(list -> !list.isEmpty())
                .orElse(List.of(new PieChart.Data("Nessun dato", 1)));

        if (charts.get(ChartType.PIE) instanceof PieChart pieChart) {
            pieChart.setData(FXCollections.observableArrayList(data));
        }
    }

    private void updateBarChart() {
        if (currentView == null) return;

        double totalIn = currentView.total(TransactionDirection.IN);
        double totalOut = currentView.total(TransactionDirection.OUT);
        updateBarChartWithData(totalIn, totalOut);
    }


    private void updateBarChartWithData(double totalIncome, double totalExpenses) {
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Entrate");
        incomeSeries.getData().add(new XYChart.Data<>("Totale", totalIncome));

        XYChart.Series<String, Number> expensesSeries = new XYChart.Series<>();
        expensesSeries.setName("Uscite");
        expensesSeries.getData().add(new XYChart.Data<>("Totale", totalExpenses));

        BarChart<String, Number> barChart = (BarChart<String, Number>) charts.get(ChartType.BAR);
        barChart.getData().clear();
        barChart.getData().addAll(incomeSeries, expensesSeries);
    }

    public static Label createSectionTitle(String title) {
        return BuilderUtils.createStyledLabel(title, "section-title");
    }

    @Override
    public Node getNode() {
        return container;
    }

    @RequiredArgsConstructor
    @Getter
    private enum ChartType {
        PIE("Uscite per Categoria", 0),
        BAR("Entrate vs Uscite", 1);

        private final String labelText;
        private final int columnIndex;
    }
}