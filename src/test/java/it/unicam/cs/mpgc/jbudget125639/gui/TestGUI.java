package it.unicam.cs.mpgc.jbudget125639.gui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TestGUI extends Application {

    private StackPane mainContentStack; // Contenitore principale per gestire l'overlay del dialogo
    private Node homeView;
    private Node statsView;

    // Entry point per lanciare l'applicazione di test.
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestore Finanziario - DEMO");

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // 1. Header
        root.setTop(createHeader());

        // 2. Il contenuto centrale ora è uno StackPane per gestire gli overlay
        mainContentStack = new StackPane();

        // VBox principale che conterrà bilancio, navigazione e le viste
        VBox centerLayout = new VBox(20);
        centerLayout.setPadding(new Insets(20, 40, 40, 40));
        centerLayout.setAlignment(Pos.TOP_CENTER);

        // Crea le due viste principali
        homeView = createHomeView();
        statsView = createStatsView();
        statsView.setVisible(false); // La vista statistiche è inizialmente nascosta

        // StackPane per sovrapporre le viste Home e Statistiche
        StackPane viewsContainer = new StackPane(statsView, homeView);
        VBox.setVgrow(viewsContainer, Priority.ALWAYS);

        // Aggiunge i componenti principali al layout centrale
        centerLayout.getChildren().addAll(
                createBalanceBox(),
                createNavigationView(),
                viewsContainer
        );

        mainContentStack.getChildren().add(centerLayout);

        // Lo ScrollPane contiene tutto il contenuto centrale per permettere lo scorrimento
        ScrollPane scrollPane = new ScrollPane(mainContentStack);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        root.setCenter(scrollPane);

        // Creazione della scena e collegamento del CSS
        Scene scene = new Scene(root, 1100, 850);
        java.net.URL cssURL = getClass().getResource("/styles.css");
        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm());
        } else {
            System.err.println("ERRORE: 'styles.css' non trovato in 'src/test/resources/'");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Node createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 20, 10, 20));
        header.getStyleClass().add("header");

        // Filtri
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Filtra per data");
        ComboBox<String> directionComboBox = new ComboBox<>();
        directionComboBox.getItems().addAll("Tutte", "IN", "OUT");
        directionComboBox.setValue("Tutte");
        TextField tagsTextField = new TextField();
        tagsTextField.setPromptText("Cerca per tag...");
        HBox filtersBox = new HBox(10, datePicker, directionComboBox, tagsTextField);
        filtersBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Selettore utente modificato
        ComboBox<String> userComboBox = new ComboBox<>();
        userComboBox.getItems().addAll("Mario Rossi", "Luigi Verdi", "Global");
        userComboBox.getItems().add(""); // Separatore visuale
        userComboBox.getItems().add("Aggiungi Utente...");
        userComboBox.setValue("Mario Rossi");
        userComboBox.getStyleClass().add("user-combo-box");

        header.getChildren().addAll(filtersBox, spacer, userComboBox);
        return header;
    }

    private Node createBalanceBox() {
        VBox balanceBox = new VBox(5);
        balanceBox.setAlignment(Pos.CENTER);
        balanceBox.getStyleClass().add("balance-box");

        Label titleLabel = new Label("BILANCIO ATTUALE");
        titleLabel.getStyleClass().add("balance-title");

        Label amountLabel = new Label("4.750,50 €");
        amountLabel.getStyleClass().add("balance-amount");

        balanceBox.getChildren().addAll(titleLabel, amountLabel);
        return balanceBox;
    }

    private Node createNavigationView() {
        HBox navBox = new HBox();
        navBox.setAlignment(Pos.CENTER);
        navBox.setSpacing(0);
        navBox.getStyleClass().add("nav-box");

        ToggleGroup toggleGroup = new ToggleGroup();

        ToggleButton homeButton = new ToggleButton("Home");
        homeButton.setToggleGroup(toggleGroup);
        homeButton.setSelected(true);
        homeButton.getStyleClass().add("nav-button");

        ToggleButton statsButton = new ToggleButton("Statistiche");
        statsButton.setToggleGroup(toggleGroup);
        statsButton.getStyleClass().add("nav-button");

        // Logica per cambiare vista
        homeButton.setOnAction(e -> {
            homeView.setVisible(true);
            statsView.setVisible(false);
        });

        statsButton.setOnAction(e -> {
            homeView.setVisible(false);
            statsView.setVisible(true);
        });

        navBox.getChildren().addAll(homeButton, statsButton);
        return navBox;
    }

    private Node createHomeView() {
        VBox homeLayout = new VBox(20);
        homeLayout.setAlignment(Pos.TOP_CENTER);

        Label historyLabel = new Label("Storico Transazioni");
        historyLabel.getStyleClass().add("section-title");
        HBox historyContainer = new HBox(historyLabel);
        historyContainer.setAlignment(Pos.CENTER_LEFT);
        historyContainer.setPadding(new Insets(10, 0, 0, 0));

        Node transactionGrid = createTransactionGrid();
        VBox.setVgrow(transactionGrid, Priority.ALWAYS);

        Button addButton = new Button("+ Aggiungi Transazione");
        addButton.getStyleClass().add("add-button");
        addButton.setOnAction(e -> showAddTransactionDialog());

        HBox buttonContainer = new HBox(addButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(20, 0, 0, 0));

        homeLayout.getChildren().addAll(historyContainer, transactionGrid, buttonContainer);
        return homeLayout;
    }

    private Node createStatsView() {
        VBox statsLayout = new VBox(30);
        statsLayout.setAlignment(Pos.TOP_CENTER);
        statsLayout.setPadding(new Insets(20, 0, 0, 0));

        // Grafico a Torta per le Uscite
        Label pieChartLabel = new Label("Uscite per Categoria");
        pieChartLabel.getStyleClass().add("section-title");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Casa", 415),
                new PieChart.Data("Auto", 300),
                new PieChart.Data("Shopping", 220),
                new PieChart.Data("Svago", 80)
        );
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle(""); // Titolo già presente nella Label
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(false);

        // Grafico a Barre per Entrate/Uscite
        Label barChartLabel = new Label("Entrate vs Uscite (Ultimi Mesi)");
        barChartLabel.getStyleClass().add("section-title");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("");
        barChart.setLegendVisible(true);

        XYChart.Series<String, Number> entrateSeries = new XYChart.Series<>();
        entrateSeries.setName("Entrate");
        entrateSeries.getData().addAll(
                new XYChart.Data<>("Agosto", 2575),
                new XYChart.Data<>("Settembre", 2500),
                new XYChart.Data<>("Ottobre", 2575)
        );

        XYChart.Series<String, Number> usciteSeries = new XYChart.Series<>();
        usciteSeries.setName("Uscite");
        usciteSeries.getData().addAll(
                new XYChart.Data<>("Agosto", 1800),
                new XYChart.Data<>("Settembre", 2100),
                new XYChart.Data<>("Ottobre", 1160.50)
        );
        barChart.getData().addAll(entrateSeries, usciteSeries);

        // Layout a griglia per i grafici per affiancarli
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

        statsLayout.getChildren().add(chartsGrid);
        return statsLayout;
    }

    private void showAddTransactionDialog() {
        // Overlay scuro che copre tutta la finestra
        StackPane overlay = new StackPane();
        overlay.getStyleClass().add("dialog-overlay");

        // Box del dialogo
        VBox dialogBox = new VBox(20);
        dialogBox.getStyleClass().add("dialog-pane");
        dialogBox.setMaxWidth(500);

        Label title = new Label("Nuova Transazione");
        title.getStyleClass().add("dialog-title");

        // Form per l'inserimento dati
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.add(new Label("Descrizione:"), 0, 0);
        formGrid.add(new TextField(), 1, 0);
        formGrid.add(new Label("Importo:"), 0, 1);
        formGrid.add(new TextField("0.00"), 1, 1);
        formGrid.add(new Label("Direzione:"), 0, 2);
        formGrid.add(new ComboBox<>(FXCollections.observableArrayList("IN", "OUT")), 1, 2);
        formGrid.add(new Label("Data:"), 0, 3);
        formGrid.add(new DatePicker(java.time.LocalDate.now()), 1, 3);
        formGrid.add(new Label("Tags:"), 0, 4);
        formGrid.add(new TextField("es: Lavoro, Cibo"), 1, 4);

        // Pulsanti
        Button saveButton = new Button("Salva");
        saveButton.getStyleClass().add("save-button");
        Button cancelButton = new Button("Annulla");
        cancelButton.getStyleClass().add("cancel-button");

        HBox buttonBox = new HBox(15, cancelButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        dialogBox.getChildren().addAll(title, formGrid, buttonBox);

        overlay.getChildren().add(dialogBox);
        mainContentStack.getChildren().add(overlay);

        // Animazioni di entrata
        dialogBox.setScaleX(0.9);
        dialogBox.setScaleY(0.9);
        ScaleTransition st = new ScaleTransition(Duration.millis(200), dialogBox);
        st.setToX(1.0);
        st.setToY(1.0);
        FadeTransition ft = new FadeTransition(Duration.millis(200), overlay);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
        st.play();

        // Azioni per chiudere il dialogo
        cancelButton.setOnAction(e -> mainContentStack.getChildren().remove(overlay));
        saveButton.setOnAction(e -> mainContentStack.getChildren().remove(overlay)); // Per la demo, chiude solo
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) { // Chiude solo se si clicca sullo sfondo scuro
                mainContentStack.getChildren().remove(overlay);
            }
        });
    }

    private Node createTransactionGrid() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("transaction-grid");
        grid.setHgap(10);
        grid.setVgap(15);

        // Imposta i vincoli per le colonne per gestirne la larghezza
        ColumnConstraints col1 = new ColumnConstraints(); col1.setPercentWidth(12);
        ColumnConstraints col2 = new ColumnConstraints(); col2.setPercentWidth(38);
        ColumnConstraints col3 = new ColumnConstraints(); col3.setPercentWidth(15); col3.setHalignment(HPos.RIGHT);
        ColumnConstraints col4 = new ColumnConstraints(); col4.setPercentWidth(10); col4.setHalignment(HPos.CENTER);
        ColumnConstraints col5 = new ColumnConstraints(); col5.setPercentWidth(25);
        grid.getColumnConstraints().addAll(col1, col2, col3, col4, col5);

        // Header della griglia
        grid.add(new Label("DATA"), 0, 0);
        grid.add(new Label("DESCRIZIONE"), 1, 0);
        grid.add(new Label("IMPORTO"), 2, 0);
        grid.add(new Label("DIREZIONE"), 3, 0);
        grid.add(new Label("TAGS"), 4, 0);
        grid.getChildren().forEach(node -> node.getStyleClass().add("grid-header"));

        // Dati hardcoded
        addGridRow(grid, 1, "26/10/2023", "Stipendio Ottobre", "2.500,00 €", "IN", new String[]{"Lavoro"});
        addGridRow(grid, 2, "25/10/2023", "Spesa supermercato Esselunga", "145,50 €", "OUT", new String[]{"Casa", "Cibo"});
        addGridRow(grid, 3, "24/10/2023", "Riparazione gomma auto", "300,00 €", "OUT", new String[]{"Auto", "Imprevisti"});
        addGridRow(grid, 4, "22/10/2023", "Vendita Vinted", "75,00 €", "IN", new String[]{"Extra"});
        addGridRow(grid, 5, "20/10/2023", "Cena con amici - Ristorante 'La Brace'", "80,00 €", "OUT", new String[]{"Svago", "Ristorante"});
        addGridRow(grid, 6, "18/10/2023", "Bolletta mensile energia elettrica", "115,00 €", "OUT", new String[]{"Casa", "Bollette"});
        addGridRow(grid, 7, "15/10/2023", "Acquisto felpa su Zalando", "220,00 €", "OUT", new String[]{"Shopping"});

        return grid;
    }

    private void addGridRow(GridPane grid, int rowIndex, String date, String description, String amount, String direction, String[] tags) {
        grid.add(new Label(date), 0, rowIndex);
        grid.add(new Label(description), 1, rowIndex);
        Label amountLabel = new Label(amount);
        amountLabel.getStyleClass().add(direction.equals("IN") ? "amount-in" : "amount-out");
        grid.add(amountLabel, 2, rowIndex);
        Label directionLabel = new Label(direction);
        directionLabel.getStyleClass().addAll("direction-label", direction.equals("IN") ? "direction-in" : "direction-out");
        grid.add(directionLabel, 3, rowIndex);
        HBox tagsBox = new HBox(5);
        for (String tag : tags) {
            Label tagLabel = new Label(tag);
            tagLabel.getStyleClass().add("tag-label");
            tagsBox.getChildren().add(tagLabel);
        }
        grid.add(tagsBox, 4, rowIndex);

        for (int i = 0; i < grid.getColumnCount(); i++) {
            Node node = getNodeByRowColumnIndex(rowIndex, i, grid);
            if (node != null) {
                node.getStyleClass().add("grid-cell");
            }
        }
    }

    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            int nodeRow = (r == null) ? 0 : r;
            int nodeCol = (c == null) ? 0 : c;

            if (nodeRow == row && nodeCol == column) {
                return node;
            }
        }
        return null;
    }
}