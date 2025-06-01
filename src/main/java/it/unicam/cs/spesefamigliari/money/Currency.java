package it.unicam.cs.spesefamigliari.money;

import java.io.Serializable;

public interface Currency extends Serializable {

    String name();
    float absoluteValue();

    String format(double value);

}
