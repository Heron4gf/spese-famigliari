package it.unicam.cs.mpgc.jbudget125639.gui.components;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsView {
    
    private final VBox container;
    private PieChart pieChart;
    private BarChart<String, Number> barChart;

    public StatsView() {
        container = new VBox(30);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(20, 0, 0, 0));

        createCharts();
        setupLayout();
    }

    private void createCharts() {
        Label pieChartLabel = new Label("Uscite per Categoria");
        pieChartLabel.getStyleClass().add("section-title");

        pieChart = new PieChart();
        pieChart.setTitle("");
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(false);

        Label barChartLabel = new Label("Entrate vs Uscite (Ultimi Mesi)");
        barChartLabel.getStyleClass().add("section-title");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("");
        barChart.setLegendVisible(true);

        GridPane chartsGrid = new GridPane();
        chartsGrid.setHgap(30);
        chartsGrid.setVgap(20);
        chartsGrid.setAlignment(Pos.CENTER);

        VBox pieContainer = new VBox(10, pieChartLabel, pieChart);
        pieContainer.setAlignment(Pos.CENTER);
        VBox barContainer = new VBox(10, barChartLabel, barChart);
        barContainer.setAlignment(Pos.CENTER);

        chartsGrid.add(pieContainer, 0, 0);
        chartsGrid.add(barContainer, 1, 0);

        container.getChildren().add(chartsGrid);
    }

    private void setupLayout() {
        // Initial empty data
        updateData(java.util.Collections.emptyList());
    }

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

        pieChart.setData(pieChartData);
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

        barChart.getData().clear();
        barChart.getData().addAll(incomeSeries, expensesSeries);
    }

    private String getMonthYear(Transaction transaction) {
        LocalDate date = transaction.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        return date.format(DateTimeFormatter.ofPattern("MM/yyyy"));
    }

    public Node getNode() {
        return container;
    }
}
