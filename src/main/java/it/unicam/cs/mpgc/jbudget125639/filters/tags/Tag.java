package it.unicam.cs.mpgc.jbudget125639.filters.tags;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;

/**
 * Rappresenta un'etichetta (tag) che può essere associata a una transazione per categorizzarla e filtrarla.
 */
public interface Tag extends IFilter {

    /**
     * Restituisce la priorità del tag.
     * @return la priorità come valore intero.
     */
    Integer getPriority();

    /**
     * @return il nome del tag.
     */
    String getName();

    /**
     * Verifica se questo tag ha una priorità maggiore o uguale a quella dei tag associati a una transazione.
     * @param transaction la transazione con cui confrontare la priorità.
     * @return true se questo tag ha una priorità maggiore o uguale, false altrimenti.
     */
    boolean hasMoreOrSamePriority(Transaction transaction);

}