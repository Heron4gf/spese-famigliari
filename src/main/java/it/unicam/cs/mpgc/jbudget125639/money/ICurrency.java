package it.unicam.cs.mpgc.jbudget125639.money;

import java.io.Serializable;

/**
 * Interfaccia che rappresenta una valuta astratta.
 * Fornisce metodi per ottenere il nome, il simbolo e formattare valori numerici secondo la valuta.
 */
public interface ICurrency extends Serializable {

    /**
     * Restituisce il nome della valuta.
     *
     * @return il nome della valuta (es. "Euro", "Dollaro Statunitense")
     */
    String getName();

    /**
     * Restituisce il simbolo associato alla valuta.
     *
     * @return il simbolo della valuta (es. "€", "$")
     */
    String getSymbol();

    /**
     * Restituisce una rappresentazione formattata del valore specificato,
     * secondo le convenzioni della valuta.
     *
     * @param value il valore numerico da formattare
     * @return la stringa formattata con simbolo e decimali adeguati
     */
    String format(double value);
}