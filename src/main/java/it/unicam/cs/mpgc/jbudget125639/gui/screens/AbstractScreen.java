package it.unicam.cs.mpgc.jbudget125639.gui.screens;

import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.gui.services.ServiceFactory;
import javafx.scene.Node;

/**
 * Classe astratta che fornisce l'implementazione base per tutte le schermate.
 * Gestisce la logica comune come i servizi, l'utente corrente e il ciclo di vita base.
 */
public abstract class AbstractScreen implements Screen {
    
    protected final ServiceFactory.ServiceBundle services;
    protected final String screenId;
    protected User currentUser;
    protected boolean initialized = false;
    
    /**
     * Costruttore per la schermata astratta.
     * 
     * @param services i servizi dell'applicazione
     * @param screenId l'identificatore univoco della schermata
     */
    protected AbstractScreen(ServiceFactory.ServiceBundle services, String screenId) {
        this.services = services;
        this.screenId = screenId;
    }
    
    @Override
    public final String getScreenId() {
        return screenId;
    }
    
    @Override
    public final void initialize() {
        if (!initialized) {
            createContent();
            setupEventHandlers();
            initialized = true;
        }
    }
    
    @Override
    public void updateData() {
        if (currentUser != null) {
            refreshContent();
        }
    }
    
    @Override
    public void onActivate() {
        getNode().setVisible(true);
        updateData();
    }
    
    @Override
    public void onDeactivate() {
        getNode().setVisible(false);
    }
    
    /**
     * Imposta l'utente corrente per questa schermata.
     * 
     * @param user l'utente corrente
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (initialized) {
            updateData();
        }
    }
    
    /**
     * Restituisce l'utente corrente.
     * 
     * @return l'utente corrente o null se non impostato
     */
    protected User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Crea il contenuto della schermata. Questo metodo deve essere implementato
     * dalle classi concrete per definire la struttura UI specifica.
     */
    protected abstract void createContent();
    
    /**
     * Configura gli event handlers specifici della schermata.
     * L'implementazione di default è vuota, le classi concrete possono sovrascriverla.
     */
    protected void setupEventHandlers() {
        // Implementazione di default vuota
    }
    
    /**
     * Aggiorna il contenuto della schermata con i dati correnti.
     * Questo metodo viene chiamato quando i dati cambiano.
     */
    protected abstract void refreshContent();
}
