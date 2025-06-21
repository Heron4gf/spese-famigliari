package it.unicam.cs.mpgc.jbudget125639.gui.builders;

import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import static it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils.createSectionTitle;
import static it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils.createStyledVBox;

/**
 * Builder for creating StatsView components that uses View methods for filtering and calculations.
 */
public class StatsViewBuilder implements ComponentBuilder<StatsViewBuilder.StatsViewComponent, StatsViewBuilder> {

    private View view;

    /**
     * Sets the view to use for getting filtered transactions and totals.
     *
     * @param view the view to use for data access
     * @return this builder for method chaining
     */
    public StatsViewBuilder withView(View view) {
        this.view = view;
        return self();
    }


    @Override
    public StatsViewComponent build() {
        return new StatsViewComponent(view);
    }

    @Override
    public StatsViewBuilder self() {
        return this;
    }

    @RequiredArgsConstructor
    @Getter
    private enum ChartType {
        PIE("Uscite per Categoria", 0),
        BAR("Entrate vs Uscite (Ultimi Mesi)", 1);

        private final String labelText;
        private final int columnIndex;
    }

    @Getter
    public static class StatsViewComponent implements NodeBuilder {
        private VBox container;
        private final EnumMap<ChartType, Chart> charts = new EnumMap<>(ChartType.class);
        private View currentView;

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
                VBox chartContainer = createStyledVBox(10, Pos.CENTER);
                chartContainer.getChildren().addAll(label, chart);

                grid.add(chartContainer, type.getColumnIndex(), 0);
            });

            return grid;
        }

        /**
         * Updates the charts using the current view and filters.
         */
        public void updateData() {
            if (currentView != null) {
                updatePieChart();
                updateBarChart();
            }
        }

        /**
         * Updates the view and filters, then refreshes the charts.
         *
         * @param view the new view to use
         */
        public void updateData(View view) {
            this.currentView = view;
            updateData();
        }

        private void updatePieChart() {
            if (currentView == null) {
                return;
            }

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
            if (currentView == null) {
                return;
            }

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

        @Override
        public Node getNode() {
            return container;
        }
    }
}
