package it.unicam.cs.mpgc.jbudget125639.gui.builders;

import javafx.scene.Node;

/**
 * Interfaccia che definisce un costruttore di nodi per l'interfaccia grafica.
 */
public interface NodeBuilder {
    /**
     * Restituisce un oggetto Node di JavaFX, che rappresenta un elemento dell'interfaccia utente.
     * @return un nodo JavaFX
     */
    Node getNode();
}