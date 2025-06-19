package it.unicam.cs.mpgc.jbudget125639.money;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public enum Currency implements ICurrency {

    EUR(new BaseCurrency("Eur", "€")),
    USD(new BaseCurrency("Dollar", "$"));

    private final ICurrency currency;

    @Override
    public String getName() {
        return currency.getName();
    }

    @Override
    public String getSymbol() {
        return currency.getSymbol();
    }

    @Override
    public String format(double value) {
        return currency.format(value);
    }
}
