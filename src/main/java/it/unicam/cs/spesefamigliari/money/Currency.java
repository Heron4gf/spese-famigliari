package it.unicam.cs.spesefamigliari.money;

public interface Currency {

    String name();
    float absoluteValue();

    String format(double value);

}
