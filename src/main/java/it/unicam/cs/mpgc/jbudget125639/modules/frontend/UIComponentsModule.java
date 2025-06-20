package it.unicam.cs.mpgc.jbudget125639.modules.frontend;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.gui.constants.UIConfig;
import it.unicam.cs.mpgc.jbudget125639.gui.constants.UILabel;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.AbstractModule;
import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.stream.IntStream;

@RequiredArgsConstructor
public class UIComponentsModule extends AbstractModule {

    private final TableView<Transaction> transactionsTableView;
    private final ChoiceBox<Currency> transactionCurrencyChoiceBox;
    private final ChoiceBox<TransactionDirection> transactionDirectionChoiceBox;
    private final ListView<NamedTag> transactionTagsListView;
    private final TextField transactionDescriptionField;
    private final TextField transactionAmountField;


    @Override
    public String name() {
        return "UI Components Initializer";
    }

    @Override
    protected void internalLoad() {
        setupTableColumns();
        setupChoiceBoxes();
        setupTagsList();
        setupFieldPrompts();
    }

    @Override
    protected void internalUnload() {
    }

    private void setupTableColumns() {
        var columns = transactionsTableView.getColumns();
        var propertyNames = new String[]{
                UIConfig.Property.DIRECTION.getName(),
                UIConfig.Property.MONEY_AMOUNT.getName(),
                UIConfig.Property.DESCRIPTION.getName(),
                UIConfig.Property.DATE.getName()
        };

        IntStream.range(0, Math.min(propertyNames.length, columns.size()))
                .forEach(i -> {
                    @SuppressWarnings("unchecked")
                    TableColumn<Transaction, ?> column = (TableColumn<Transaction, ?>) columns.get(i);
                    column.setCellValueFactory(new PropertyValueFactory<>(propertyNames[i]));
                });

        transactionsTableView.setPrefHeight(UIConfig.TABLE_PREFERRED_HEIGHT.getValue());
    }

    private void setupChoiceBoxes() {
        setupChoiceBox(transactionCurrencyChoiceBox, Currency.values(), Currency.EUR);
        setupChoiceBox(transactionDirectionChoiceBox, TransactionDirection.values(), TransactionDirection.OUT);

        transactionCurrencyChoiceBox.setPrefWidth(UIConfig.CURRENCY_CHOICE_WIDTH.getValue());
        transactionDirectionChoiceBox.setPrefWidth(UIConfig.DIRECTION_CHOICE_WIDTH.getValue());
    }

    private <T> void setupChoiceBox(@NonNull ChoiceBox<T> choiceBox, T[] values, T defaultValue) {
        choiceBox.setItems(FXCollections.observableArrayList(values));
        choiceBox.setValue(defaultValue);
    }

    private void setupTagsList() {
        transactionTagsListView.setItems(FXCollections.observableArrayList(NamedTag.values()));
        transactionTagsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Custom cell factory to display tag names
        transactionTagsListView.setCellFactory(listView -> new ListCell<NamedTag>() {
            @Override
            protected void updateItem(NamedTag tag, boolean empty) {
                super.updateItem(tag, empty);
                if (empty || tag == null) {
                    setText(null);
                } else {
                    setText(tag.getName());
                }
            }
        });
    }

    private void setupFieldPrompts() {
        transactionDescriptionField.setPromptText(UILabel.DESCRIPTION_PROMPT.get());
        transactionAmountField.setPromptText(UILabel.AMOUNT_PROMPT.get());
        transactionAmountField.setPrefWidth(UIConfig.AMOUNT_FIELD_WIDTH.getValue());
    }
}