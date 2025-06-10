package it.unicam.cs.mpgc.jbudget125639.money;


public abstract class AbstractCurrency implements Currency {

    public String format(double value) {
        return value+name();
    }

}
