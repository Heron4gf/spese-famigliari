package it.unicam.cs.mpgc.jbudget125639.gui.builders;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.Getter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;

/**
 * Builder for creating TransactionGrid components with optional initial data.
 */
public class TransactionGridBuilder implements ComponentBuilder<TransactionGridBuilder.TransactionGridComponent, TransactionGridBuilder> {

    private Collection<Transaction> initialTransactions;

    /**
     * Sets the initial transactions to display in the grid.
     *
     * @param transactions the initial collection of transactions
     * @return this builder for method chaining
     */
    public TransactionGridBuilder withInitialTransactions(Collection<Transaction> transactions) {
        this.initialTransactions = transactions;
        return self();
    }

    @Override
    public TransactionGridComponent build() {
        return new TransactionGridComponent(initialTransactions);
    }

    @Override
    public TransactionGridBuilder self() {
        return this;
    }

    @Getter
    public static class TransactionGridComponent implements NodeBuilder {
        private final GridPane grid;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        public TransactionGridComponent(Collection<Transaction> initialTransactions) {
            grid = new GridPane();
            grid.getStyleClass().add("transaction-grid");
            grid.setHgap(10);
            grid.setVgap(15);

            setupColumns();
            setupHeaders();

            updateTransactions(initialTransactions != null ? initialTransactions : Collections.emptyList());
        }

        private void setupColumns() {
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(12);

            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(38);

            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPercentWidth(15);
            col3.setHalignment(HPos.RIGHT);

            ColumnConstraints col4 = new ColumnConstraints();
            col4.setPercentWidth(10);
            col4.setHalignment(HPos.CENTER);

            ColumnConstraints col5 = new ColumnConstraints();
            col5.setPercentWidth(25);

            grid.getColumnConstraints().addAll(col1, col2, col3, col4, col5);
        }

        private void setupHeaders() {
            addHeaderCell("DATA", 0);
            addHeaderCell("DESCRIZIONE", 1);
            addHeaderCell("IMPORTO", 2);
            addHeaderCell("DIREZIONE", 3);
            addHeaderCell("TAGS", 4);
        }

        private void addHeaderCell(String text, int column) {
            Label header = new Label(text);
            header.getStyleClass().add("grid-header");
            grid.add(header, column, 0);
        }

        /**
         * Updates the grid with a new collection of transactions.
         *
         * @param transactions the transactions to display
         */
        public void updateTransactions(Collection<Transaction> transactions) {
            clearData();
            IntStream.range(0, transactions.size())
                    .forEach(i -> addTransactionRow(
                            new ArrayList<>(transactions).get(i), i + 1
                    ));
        }

        private void clearData() {
            grid.getChildren().removeIf(node -> {
                Integer rowIndex = GridPane.getRowIndex(node);
                return rowIndex != null && rowIndex > 0;
            });
        }

        private void addTransactionRow(Transaction transaction, int row) {
            String date = dateFormatter.format(transaction.getDate());
            addCell(new Label(date), 0, row);

            addCell(new Label(transaction.getDescription()), 1, row);

            Label amountLabel = new Label(transaction.getAmount().toString());
            amountLabel.getStyleClass().add(transaction.getDirection() == TransactionDirection.IN ? "amount-in" : "amount-out");
            addCell(amountLabel, 2, row);

            String direction = transaction.getDirection().name();
            Label directionLabel = new Label(direction);
            directionLabel.getStyleClass().addAll("direction-label",
                    transaction.getDirection() == TransactionDirection.IN ? "direction-in" : "direction-out");
            addCell(directionLabel, 3, row);

            HBox tagsBox = new HBox(5);
            transaction.getAssociatedTags().forEach(tag -> {
                Label tagLabel = new Label(tag.toString());
                tagLabel.getStyleClass().add("tag-label");
                tagsBox.getChildren().add(tagLabel);
            });
            addCell(tagsBox, 4, row);
        }

        private void addCell(Node node, int column, int row) {
            node.getStyleClass().add("grid-cell");
            grid.add(node, column, row);
        }

        @Override
        public Node getNode() {
            return grid;
        }
    }
}
