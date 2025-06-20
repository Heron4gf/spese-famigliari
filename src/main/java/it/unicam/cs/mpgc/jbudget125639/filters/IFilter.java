package it.unicam.cs.mpgc.jbudget125639.filters;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;

public interface IFilter {

    /**
     * Verifica se una transazione soddisfa i criteri di questo filtro.
     *
     * @param transaction la transazione da verificare.
     * @return true se la transazione soddisfa i criteri del filtro, false altrimenti.
     */
    boolean pass(Transaction transaction);

}