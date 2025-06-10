package it.unicam.cs.mpgc.jbudget125639.money;

public class Eur extends AbstractCurrency {
    @Override
    public String name() {
        return "€";
    }

    @Override
    public float absoluteValue() {
        return 1;
    }
}
