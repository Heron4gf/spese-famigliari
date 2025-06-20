package it.unicam.cs.mpgc.jbudget125639.gui.components;

import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Setter;
import org.controlsfx.control.CheckComboBox;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class TransactionDialog {

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
    
    @Setter
    private Consumer<TransactionData> onSave;
    @Setter
    private Runnable onCancel;

    public TransactionDialog() {
        container = new VBox(20);
        container.getStyleClass().add("dialog-pane");
        container.setMaxWidth(500);

        Label title = new Label("Nuova Transazione");
        title.getStyleClass().add("dialog-title");

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
                double amount = Double.parseDouble(amountField.getText());
                TransactionDirection direction = directionComboBox.getValue();
                LocalDate date = datePicker.getValue();

                List<NamedTag> selectedTags = tagsField.getCheckModel().getCheckedItems();
                
                TransactionData data = new TransactionData(
                        description, amount, Currency.EUR, direction, date, selectedTags
                );
                
                onSave.accept(data);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleCancel() {
        if (onCancel != null) {
            onCancel.run();
        }
    }

    public Node getNode() {
        return container;
    }
}
