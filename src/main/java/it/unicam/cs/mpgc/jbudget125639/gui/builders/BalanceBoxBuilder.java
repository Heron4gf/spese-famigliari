package it.unicam.cs.mpgc.jbudget125639.gui.builders;

import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils.createStyledLabel;
import static it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils.createStyledVBox;

/**
 * Builder for creating BalanceBox components with optional initial configuration.
 */
public class BalanceBoxBuilder implements ComponentBuilder<BalanceBoxBuilder.BalanceBoxComponent, BalanceBoxBuilder> {
    
    private MoneyAmount initialBalance;
    
    /**
     * Sets the initial balance to display.
     * 
     * @param balance the initial balance amount
     * @return this builder for method chaining
     */
    public BalanceBoxBuilder withInitialBalance(MoneyAmount balance) {
        this.initialBalance = balance;
        return self();
    }
    
    @Override
    public BalanceBoxComponent build() {
        return new BalanceBoxComponent(initialBalance);
    }

    @Override
    public BalanceBoxBuilder self() {
        return this;
    }
    
    @RequiredArgsConstructor
    @Getter
    public static class BalanceBoxComponent implements NodeBuilder {
        private final VBox container;
        private final Label amountLabel;

        public BalanceBoxComponent(MoneyAmount initialBalance) {
            container = createStyledVBox(5, Pos.CENTER, "balance-box");

            Label titleLabel = createStyledLabel("BILANCIO ATTUALE", "balance-title");
            amountLabel = createStyledLabel("0,00 €", "balance-amount");

            container.getChildren().addAll(titleLabel, amountLabel);
            
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
}
