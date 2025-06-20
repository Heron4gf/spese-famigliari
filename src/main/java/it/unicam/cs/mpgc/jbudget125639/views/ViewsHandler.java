package it.unicam.cs.mpgc.jbudget125639.views;

import lombok.NonNull;

import java.util.Collection;

/**
 * Definisce i metodi per la gestione di un insieme di viste (View).
 */
public interface ViewsHandler {
    /**
     * Restituisce una collezione di tutte le viste gestite.
     *
     * @return la collezione di tutte le viste.
     */
    Collection<View> getViews();

    /**
     * Restituisce la vista con il nome specificato.
     *
     * @param name il nome della vista da recuperare.
     * @return la vista corrispondente, o null se non viene trovata.
     * @throws NullPointerException se il nome è nullo.
     */
    View getView(@NonNull String name);

    /**
     * Aggiunge una nuova vista al gestore.
     *
     * @param view la vista da aggiungere.
     * @throws NullPointerException se la vista è nulla.
     */
    void addView(@NonNull View view);
}