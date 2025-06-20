package it.unicam.cs.mpgc.jbudget125639.gui.components;

import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;

public class BalanceBox {
    
    private final VBox container;
    private final Label amountLabel;
    private final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public BalanceBox() {
        container = new VBox(5);
        container.setAlignment(Pos.CENTER);
        container.getStyleClass().add("balance-box");

        Label titleLabel = new Label("BILANCIO ATTUALE");
        titleLabel.getStyleClass().add("balance-title");

        amountLabel = new Label("0,00 €");
        amountLabel.getStyleClass().add("balance-amount");

        container.getChildren().addAll(titleLabel, amountLabel);
    }

    public void updateBalance(MoneyAmount amount) {
        amountLabel.setText(amount.toString());
    }

    public Node getNode() {
        return container;
    }
}
