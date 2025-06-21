package it.unicam.cs.mpgc.jbudget125639.gui.builders;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils.createSectionTitle;
import static it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils.createStyledVBox;

/**
 * Builder for creating StatsView components with optional initial data.
 */
public class StatsViewBuilder implements ComponentBuilder<StatsViewBuilder.StatsViewComponent, StatsViewBuilder> {

    private Collection<Transaction> initialData;

    /**
     * Sets the initial transaction data to display in the statistics.
     *
     * @param transactions the initial collection of transactions
     * @return this builder for method chaining
     */
    public StatsViewBuilder withInitialData(Collection<Transaction> transactions) {
        this.initialData = transactions;
        return self();
    }

    @Override
    public StatsViewComponent build() {
        return new StatsViewComponent(initialData);
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
        private final VBox container;
        private final EnumMap<ChartType, Chart> charts = new EnumMap<>(ChartType.class);

        public StatsViewComponent(Collection<Transaction> initialData) {
            charts.put(ChartType.PIE, new PieChart());
            charts.put(ChartType.BAR, new BarChart<>(new CategoryAxis(), new NumberAxis()));

            container = new VBox(30);
            container.setAlignment(Pos.TOP_CENTER);
            container.setPadding(new Insets(20, 0, 0, 0));

            GridPane chartsGrid = new GridPane();
            chartsGrid.setHgap(30);
            chartsGrid.setVgap(20);
            chartsGrid.setAlignment(Pos.CENTER);

            Arrays.stream(ChartType.values()).forEach(type -> {
                Chart chart = charts.get(type);
                chart.setLegendVisible(true);
                chart.setTitle("");

                Label label = createSectionTitle(type.getLabelText());
                VBox chartContainer = createStyledVBox(10, Pos.CENTER);
                chartContainer.getChildren().addAll(label, chart);

                chartsGrid.add(chartContainer, type.getColumnIndex(), 0);
            });

            if (charts.get(ChartType.PIE) instanceof PieChart pie) {
                pie.setLabelsVisible(false);
            }

            container.getChildren().add(chartsGrid);

            // Load initial data
            updateData(initialData != null ? initialData : Collections.emptyList());
        }

        /**
         * Updates the charts with a new collection of transactions.
         *
         * @param transactions the transactions to display
         */
        public void updateData(Collection<Transaction> transactions) {
            updatePieChart(transactions);
            updateBarChart(transactions);
        }

        private void updatePieChart(Collection<Transaction> transactions) {
            Map<String, Double> expensesByTag = transactions.stream()
                    .filter(t -> t.getDirection() == TransactionDirection.OUT)
                    .collect(Collectors.groupingBy(
                            t -> t.getAssociatedTags().findFirst()
                                    .map(tag -> tag.getName())
                                    .orElse("Altro"),
                            Collectors.summingDouble(t -> t.getAmount().getValue())
                    ));

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            expensesByTag.forEach((category, amount) ->
                    pieChartData.add(new PieChart.Data(category, amount)));

            if (pieChartData.isEmpty()) {
                pieChartData.add(new PieChart.Data("Nessun dato", 1));
            }

            ((PieChart) charts.get(ChartType.PIE)).setData(pieChartData);
        }

        private void updateBarChart(Collection<Transaction> transactions) {
            Map<String, Double> incomeByMonth = transactions.stream()
                    .filter(t -> t.getDirection() == TransactionDirection.IN)
                    .collect(Collectors.groupingBy(
                            this::getMonthYear,
                            Collectors.summingDouble(t -> t.getAmount().getValue())
                    ));

            Map<String, Double> expensesByMonth = transactions.stream()
                    .filter(t -> t.getDirection() == TransactionDirection.OUT)
                    .collect(Collectors.groupingBy(
                            this::getMonthYear,
                            Collectors.summingDouble(t -> t.getAmount().getValue())
                    ));

            XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
            incomeSeries.setName("Entrate");

            XYChart.Series<String, Number> expensesSeries = new XYChart.Series<>();
            expensesSeries.setName("Uscite");

            incomeByMonth.forEach((month, amount) ->
                    incomeSeries.getData().add(new XYChart.Data<>(month, amount)));

            expensesByMonth.forEach((month, amount) ->
                    expensesSeries.getData().add(new XYChart.Data<>(month, amount)));

            if (incomeSeries.getData().isEmpty()) {
                incomeSeries.getData().add(new XYChart.Data<>("Nessun dato", 0));
            }

            if (expensesSeries.getData().isEmpty()) {
                expensesSeries.getData().add(new XYChart.Data<>("Nessun dato", 0));
            }

            BarChart<String, Number> barChart = (BarChart<String, Number>) charts.get(ChartType.BAR);
            barChart.getData().clear();
            barChart.getData().addAll(incomeSeries, expensesSeries);
        }

        private String getMonthYear(Transaction transaction) {
            LocalDate date = transaction.getDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
            return date.format(DateTimeFormatter.ofPattern("MM/yyyy"));
        }

        @Override
        public Node getNode() {
            return container;
        }
    }
}
