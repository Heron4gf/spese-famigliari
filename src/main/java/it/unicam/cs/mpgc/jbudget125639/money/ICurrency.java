package it.unicam.cs.mpgc.jbudget125639.money;

import java.io.Serializable;

public interface ICurrency extends Serializable {

    String getName();
    String getSymbol();
    String format(double value);

}
