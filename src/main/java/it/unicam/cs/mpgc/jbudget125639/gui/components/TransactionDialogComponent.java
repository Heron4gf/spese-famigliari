package it.unicam.cs.mpgc.jbudget125639.gui.components;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils;
import it.unicam.cs.mpgc.jbudget125639.gui.builders.NodeBuilder;
import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.controlsfx.control.CheckComboBox;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Componente UI per il dialogo di inserimento di una nuova transazione.
 * La sua istanza viene creata tramite il builder generato da Lombok,
 * che richiede i gestori per gli eventi di salvataggio e annullamento.
 */
@Getter
public class TransactionDialogComponent implements NodeBuilder {

    private final VBox container;
    private final TextField descriptionField = new TextField();
    private final TextField amountField = new TextField("0.00");
    private final ComboBox<TransactionDirection> directionComboBox =
            new ComboBox<>(FXCollections.observableArrayList(TransactionDirection.values()));
    private final DatePicker datePicker = new DatePicker(LocalDate.now());
    private final CheckComboBox<NamedTag> tagsField =
            new CheckComboBox<>(FXCollections.observableArrayList(NamedTag.values()));

    @NonNull private final Consumer<Transaction> onSave;
    @NonNull private final Runnable onCancel;

    /**
     * Costruisce un nuovo componente UI per il dialogo di transazione.
     * È annotato con @Builder per generare un builder che richiede i callback.
     *
     * @param onSave Callback da eseguire al salvataggio. Non può essere nullo.
     * @param onCancel Callback da eseguire all'annullamento. Non può essere nullo.
     */
    @Builder
    public TransactionDialogComponent(@NonNull Consumer<Transaction> onSave, @NonNull Runnable onCancel) {
        this.onSave = onSave;
        this.onCancel = onCancel;

        this.container = buildContainer();
        Label title = createDialogTitle("Nuova Transazione");
        GridPane formGrid = buildFormGrid();
        HBox buttonBox = buildButtonBox();

        container.getChildren().addAll(title, formGrid, buttonBox);
    }

    private VBox buildContainer() {
        VBox box = BuilderUtils.createStyledVBox(20, Pos.TOP_LEFT, "dialog-pane");
        box.setMaxWidth(500);
        return box;
    }

    private GridPane buildFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        Node[][] fields = {
                {new Label("Descrizione:"), descriptionField},
                {new Label("Importo:"), amountField},
                {new Label("Direzione:"), directionComboBox},
                {new Label("Data:"), datePicker},
                {new Label("Tags:"), tagsField}
        };

        IntStream.range(0, fields.length).forEach(i -> {
            grid.add(fields[i][0], 0, i);
            grid.add(fields[i][1], 1, i);
        });

        return grid;
    }

    private HBox buildButtonBox() {
        Button saveButton = new Button("Salva");
        saveButton.getStyleClass().add("save-button");
        saveButton.setOnAction(e -> handleSave());

        Button cancelButton = new Button("Annulla");
        cancelButton.getStyleClass().add("cancel-button");
        cancelButton.setOnAction(e -> handleCancel());

        HBox box = new HBox(15, cancelButton, saveButton);
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }

    private void handleSave() {
        try {
            String description = descriptionField.getText();
            double amount = Double.parseDouble(amountField.getText().replace(',', '.'));
            TransactionDirection direction = directionComboBox.getValue();
            LocalDate localDate = datePicker.getValue();
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            List<NamedTag> selectedTags = tagsField.getCheckModel().getCheckedItems();

            Transaction transaction = new Transaction(description, new MoneyAmount(amount, Currency.EUR), direction, date, selectedTags);
            onSave.accept(transaction);
        } catch (NumberFormatException e) {
            System.err.println("Formato dell'importo non valido: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Errore durante il salvataggio della transazione: " + e.getMessage());
        }
    }

    private void handleCancel() {
        onCancel.run();
    }

    public static Label createDialogTitle(String title) {
        return BuilderUtils.createStyledLabel(title, "dialog-title");
    }

    @Override
    public Node getNode() {
        return container;
    }
}