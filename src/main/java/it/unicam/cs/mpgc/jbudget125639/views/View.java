package it.unicam.cs.mpgc.jbudget125639.views;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import lombok.NonNull;

import java.util.Collection;

/**
 * Rappresenta una vista che consente di visualizzare e manipolare un insieme di transazioni.
 */
public interface View {

    /**
     * Calcola l'importo totale delle transazioni che soddisfano i filtri specificati.
     *
     * @param filters i filtri da applicare per selezionare le transazioni.
     * @return la somma totale degli importi delle transazioni filtrate.
     */
    double total(IFilter... filters);

    /**
     * Restituisce il nome della vista.
     *
     * @return il nome della vista.
     */
    String getName();

    /**
     * Restituisce una collezione ordinata di transazioni che soddisfano i filtri specificati.
     *
     * @param filters i filtri da applicare per selezionare le transazioni.
     * @return una collezione ordinata delle transazioni filtrate.
     * @throws NullPointerException se l'array dei filtri è nullo.
     */
    Collection<Transaction> ordered(@NonNull IFilter... filters);

    /**
     * Restituisce una collezione di transazioni che soddisfano i filtri specificati.
     *
     * @param filters i filtri da applicare per selezionare le transazioni.
     * @return una collezione delle transazioni filtrate.
     * @throws NullPointerException se l'array dei filtri è nullo.
     */
    Collection<Transaction> getFiltered(@NonNull IFilter... filters);

    /**
     * Rimuove una transazione specifica dalla vista.
     *
     * @param transaction la transazione da rimuovere.
     * @throws NullPointerException se la transazione è nulla.
     */
    void removeTransaction(@NonNull Transaction transaction);
}