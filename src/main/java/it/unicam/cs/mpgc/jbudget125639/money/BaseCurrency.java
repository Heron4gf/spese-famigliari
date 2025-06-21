package it.unicam.cs.mpgc.jbudget125639.money;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
 * classe concreta per definire una currency
 */
@RequiredArgsConstructor
@Getter
public class BaseCurrency implements ICurrency {

    private final String name;
    private final String symbol;

    @Override
    public String format(double value) {
        return value + " " + getSymbol();
    }

}
