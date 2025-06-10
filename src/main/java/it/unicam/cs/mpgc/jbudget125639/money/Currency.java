package it.unicam.cs.mpgc.jbudget125639.money;

import java.io.Serializable;

public interface Currency extends Serializable {

    String name();
    float absoluteValue();

    String format(double value);

}
