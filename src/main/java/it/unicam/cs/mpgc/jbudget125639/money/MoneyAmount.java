package it.unicam.cs.mpgc.jbudget125639.money;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class MoneyAmount implements Serializable {

    public MoneyAmount(@Positive double amount, @NonNull ICurrency currency) {
        this.cents = (int) amount*100;
        this.currency = currency;
    }

    @Positive
    private final int cents;

    @NonNull
    private final ICurrency currency;

    public double toDouble() {
        return cents/100d;
    }

    @Override
    public String toString() {
        return currency.format(toDouble());
    }
}
