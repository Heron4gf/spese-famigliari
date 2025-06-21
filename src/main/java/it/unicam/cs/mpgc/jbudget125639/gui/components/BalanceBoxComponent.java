package it.unicam.cs.mpgc.jbudget125639.gui.components;

import it.unicam.cs.mpgc.jbudget125639.gui.builders.NodeBuilder;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Builder;
import lombok.Getter;

import static it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils.createStyledLabel;
import static it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils.createStyledVBox;

/**
 * Componente riquadro del bilancio per il frontend.
 * La sua istanza può essere creata tramite il builder generato da Lombok.
 * Esempio: BalanceBoxComponent.builder().initialBalance(balance).build();
 */
@Getter
public class BalanceBoxComponent implements NodeBuilder {
    private final VBox container;
    private final Label amountLabel;

    @Builder
    public BalanceBoxComponent(MoneyAmount initialBalance) {
        this.container = createStyledVBox(5, Pos.CENTER, "balance-box");

        Label titleLabel = createStyledLabel("BILANCIO ATTUALE", "balance-title");
        this.amountLabel = createStyledLabel("0,00 €", "balance-amount");

        this.container.getChildren().addAll(titleLabel, amountLabel);

        if (initialBalance != null) {
            updateBalance(initialBalance);
        }
    }

    @Override
    public Node getNode() {
        return container;
    }

    public void updateBalance(MoneyAmount amount) {
        amountLabel.setText(amount.toString());
    }
}