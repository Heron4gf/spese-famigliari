package it.unicam.cs.mpgc.jbudget125639.gui.screens;

import it.unicam.cs.mpgc.jbudget125639.gui.services.ServiceFactory;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Classe astratta che fornisce l'implementazione base per tutte le schermate.
 * Gestisce la logica comune come i servizi, l'utente corrente e il ciclo di vita base.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractScreen implements Screen {
    
    protected final ServiceFactory.ServiceBundle services;
    protected final String screenId;

    @Getter
    protected View currentView;
    protected boolean initialized = false;

    
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
        if (currentView != null) {
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

    @Override
    public void setViewer(View view) {
        this.currentView = view;
        if (initialized) {
            updateData();
        }
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
