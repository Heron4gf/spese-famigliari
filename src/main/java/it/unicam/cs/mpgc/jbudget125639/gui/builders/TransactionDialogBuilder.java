package it.unicam.cs.mpgc.jbudget125639.gui.builders;

import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.controlsfx.control.CheckComboBox;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import static it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils.createDialogTitle;
import static it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils.createStyledVBox;

/**
 * Builder for creating TransactionDialog components with optional configuration for callbacks.
 */
public class TransactionDialogBuilder implements ComponentBuilder<TransactionDialogBuilder.TransactionDialogComponent, TransactionDialogBuilder> {

    private Consumer<TransactionDialogComponent.TransactionData> onSave;
    private Runnable onCancel;

    /**
     * Sets the callback to be executed when the transaction is saved.
     *
     * @param onSave the consumer for the transaction data
     * @return this builder for method chaining
     */
    public TransactionDialogBuilder withSaveHandler(Consumer<TransactionDialogComponent.TransactionData> onSave) {
        this.onSave = onSave;
        return self();
    }

    /**
     * Sets the callback to be executed when the dialog is cancelled.
     *
     * @param onCancel the runnable to execute on cancellation
     * @return this builder for method chaining
     */
    public TransactionDialogBuilder withCancelHandler(Runnable onCancel) {
        this.onCancel = onCancel;
        return self();
    }

    @Override
    public TransactionDialogComponent build() {
        return new TransactionDialogComponent(onSave, onCancel);
    }

    @Override
    public TransactionDialogBuilder self() {
        return this;
    }

    @Getter
    public static class TransactionDialogComponent implements NodeBuilder {

        public record TransactionData(
                String description,
                double amount,
                Currency currency,
                TransactionDirection direction,
                LocalDate date,
                List<NamedTag> tags
        ) {}

        private final VBox container;
        private final TextField descriptionField;
        private final TextField amountField;
        private final ComboBox<TransactionDirection> directionComboBox;
        private final DatePicker datePicker;
        private final CheckComboBox<NamedTag> tagsField;

        private final Consumer<TransactionData> onSave;
        private final Runnable onCancel;

        /**
         * Constructs a new transaction dialog UI component.
         *
         * @param onSave   The callback to execute upon saving.
         * @param onCancel The callback to execute upon cancellation.
         */
        public TransactionDialogComponent(Consumer<TransactionData> onSave, Runnable onCancel) {
            this.onSave = onSave;
            this.onCancel = onCancel;

            container = createStyledVBox(20, Pos.TOP_LEFT, "dialog-pane");
            container.setMaxWidth(500);

            Label title = createDialogTitle("Nuova Transazione");

            GridPane formGrid = new GridPane();
            formGrid.setHgap(15);
            formGrid.setVgap(15);

            descriptionField = new TextField();
            amountField = new TextField("0.00");
            directionComboBox = new ComboBox<>(FXCollections.observableArrayList(TransactionDirection.values()));
            datePicker = new DatePicker(LocalDate.now());
            tagsField = new CheckComboBox<>(FXCollections.observableArrayList(NamedTag.values()));

            formGrid.add(new Label("Descrizione:"), 0, 0);
            formGrid.add(descriptionField, 1, 0);
            formGrid.add(new Label("Importo:"), 0, 1);
            formGrid.add(amountField, 1, 1);
            formGrid.add(new Label("Direzione:"), 0, 2);
            formGrid.add(directionComboBox, 1, 2);
            formGrid.add(new Label("Data:"), 0, 3);
            formGrid.add(datePicker, 1, 3);
            formGrid.add(new Label("Tags:"), 0, 4);
            formGrid.add(tagsField, 1, 4);

            Button saveButton = new Button("Salva");
            saveButton.getStyleClass().add("save-button");
            saveButton.setOnAction(e -> handleSave());

            Button cancelButton = new Button("Annulla");
            cancelButton.getStyleClass().add("cancel-button");
            cancelButton.setOnAction(e -> handleCancel());

            HBox buttonBox = new HBox(15, cancelButton, saveButton);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);

            container.getChildren().addAll(title, formGrid, buttonBox);
        }

        private void handleSave() {
            if (onSave != null) {
                try {
                    String description = descriptionField.getText();
                    double amount = Double.parseDouble(amountField.getText().replace(',', '.'));
                    TransactionDirection direction = directionComboBox.getValue();
                    LocalDate date = datePicker.getValue();

                    List<NamedTag> selectedTags = tagsField.getCheckModel().getCheckedItems();

                    TransactionData data = new TransactionData(
                            description, amount, Currency.EUR, direction, date, selectedTags
                    );

                    onSave.accept(data);
                } catch (NumberFormatException e) {
                    // Ideally, show an error to the user
                    System.err.println("Invalid amount format: " + e.getMessage());
                }
            }
        }

        private void handleCancel() {
            if (onCancel != null) {
                onCancel.run();
            }
        }

        @Override
        public Node getNode() {
            return container;
        }
    }
}
