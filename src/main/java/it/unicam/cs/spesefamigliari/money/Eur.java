package it.unicam.cs.spesefamigliari.money;

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
