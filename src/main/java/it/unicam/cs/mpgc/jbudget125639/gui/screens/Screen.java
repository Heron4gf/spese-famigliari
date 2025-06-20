package it.unicam.cs.mpgc.jbudget125639.gui.screens;

import it.unicam.cs.mpgc.jbudget125639.views.View;
import javafx.scene.Node;

/**
 * Interfaccia che definisce il contratto per tutte le schermate dell'applicazione.
 * Ogni schermata deve fornire un nodo JavaFX e gestire la propria inizializzazione
 * e aggiornamento dei dati.
 */
public interface Screen {
    
    /**
     * Restituisce il nodo JavaFX che rappresenta questa schermata.
     * 
     * @return il nodo principale della schermata
     */
    Node getNode();
    
    /**
     * Inizializza la schermata. Questo metodo viene chiamato una sola volta
     * quando la schermata viene creata.
     */
    void initialize();
    
    /**
     * Aggiorna i dati della schermata. Questo metodo viene chiamato ogni volta
     * che la schermata diventa attiva o quando i dati sottostanti cambiano.
     */
    void updateData();
    
    /**
     * Chiamato quando la schermata diventa attiva (visibile).
     */
    void onActivate();
    
    /**
     * Chiamato quando la schermata diventa inattiva (nascosta).
     */
    void onDeactivate();
    
    /**
     * Restituisce l'identificatore univoco di questa schermata.
     * 
     * @return l'ID della schermata
     */
    String getScreenId();

    void setViewer(View view);
}
