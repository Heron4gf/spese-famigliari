package it.unicam.cs.spesefamigliari.money;


public abstract class AbstractCurrency implements Currency {

    public String format(double value) {
        return value+name();
    }

}
