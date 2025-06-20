package it.unicam.cs.mpgc.jbudget125639.gui.screens;

import it.unicam.cs.mpgc.jbudget125639.views.View;
import javafx.scene.layout.StackPane;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Gestore delle schermate dell'applicazione. Si occupa di registrare le schermate,
 * gestire il cambio tra di esse e mantenere lo stato della schermata attiva.
 */
public class ScreenManager {
    
    private final Map<String, Screen> screens = new HashMap<>();
    private final StackPane container;
    private Screen currentScreen;
    
    /**
     * Costruttore del gestore delle schermate.
     * 
     * @param container il contenitore JavaFX che ospiterà le schermate
     */
    public ScreenManager(StackPane container) {
        this.container = container;
    }
    
    /**
     * Registra una nuova schermata nel gestore.
     * 
     * @param screen la schermata da registrare
     * @throws IllegalArgumentException se una schermata con lo stesso ID è già registrata
     */
    public void registerScreen(@NonNull Screen screen) {
        if (isScreenRegistered(screen.getScreenId())) {
            throw new IllegalArgumentException("Screen with ID '" + screen.getScreenId() + "' is already registered");
        }
        
        screens.put(screen.getScreenId(), screen);
        screen.initialize();
        
        // Aggiungi il nodo al container ma rendilo invisibile
        container.getChildren().add(screen.getNode());
        screen.getNode().setVisible(false);
    }
    
    /**
     * Cambia alla schermata specificata dall'ID.
     * 
     * @param screenId l'ID della schermata da attivare
     * @throws IllegalArgumentException se la schermata non è registrata
     */
    public void switchToScreen(@NonNull String screenId) {
        @NonNull Screen targetScreen = screens.get(screenId);

        if (currentScreen != null) {
            currentScreen.onDeactivate();
        }
        currentScreen = targetScreen;
        currentScreen.onActivate();
    }
    
    /**
     * Restituisce la schermata attualmente attiva.
     *
     * @return la schermata corrente o Optional.empty() se nessuna schermata è attiva
     */
    public Optional<Screen> getCurrentScreen() {
        return Optional.ofNullable(currentScreen);
    }
    
    /**
     * Restituisce una schermata registrata per ID.
     * 
     * @param screenId l'ID della schermata
     * @return la schermata o Optional.empty() se non trovata
     */
    public Optional<Screen> getScreen(String screenId) {
        return Optional.ofNullable(screens.get(screenId));
    }
    
    /**
     * Aggiorna l'utente corrente per tutte le schermate registrate.
     * 
     * @param view l'utente corrente
     */
    public void setViewer(View view) {
        screens.values().forEach(screen -> {
            screen.setViewer(view);
        });
    }
    
    /**
     * Aggiorna i dati di tutte le schermate registrate.
     */
    public void updateAllScreens() {
        screens.values().forEach(Screen::updateData);
    }
    
    /**
     * Aggiorna i dati della schermata corrente.
     */
    public void updateCurrentScreen() {
        if(getCurrentScreen().isPresent()) {
            currentScreen.updateData();
        }
    }
    
    /**
     * Verifica se una schermata è registrata.
     * 
     * @param screenId l'ID della schermata
     * @return true se la schermata è registrata, false altrimenti
     */
    public boolean isScreenRegistered(String screenId) {
        return screens.containsKey(screenId);
    }
    
    /**
     * Restituisce il numero di schermate registrate.
     * 
     * @return il numero di schermate
     */
    public int getScreenCount() {
        return screens.size();
    }
}
