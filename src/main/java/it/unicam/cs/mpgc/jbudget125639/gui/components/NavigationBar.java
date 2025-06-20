package it.unicam.cs.mpgc.jbudget125639.gui.components;

import it.unicam.cs.mpgc.jbudget125639.gui.screens.HomeScreen;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.ScreenManager;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.StatsScreen;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 * Barra di navigazione che permette di cambiare tra le diverse schermate dell'applicazione.
 * Utilizza il ScreenManager per gestire il cambio di schermata in modo pulito e modulare.
 */
public class NavigationBar {
    
    private final HBox container;
    private final ScreenManager screenManager;
    private final ToggleButton homeButton;
    private final ToggleButton statsButton;

    /**
     * Costruttore della barra di navigazione.
     * 
     * @param screenManager il gestore delle schermate
     */
    public NavigationBar(ScreenManager screenManager) {
        this.screenManager = screenManager;
        
        container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(0);
        container.getStyleClass().add("nav-box");

        ToggleGroup toggleGroup = new ToggleGroup();

        homeButton = new ToggleButton("Home");
        homeButton.setToggleGroup(toggleGroup);
        homeButton.setSelected(true);
        homeButton.getStyleClass().add("nav-button");

        statsButton = new ToggleButton("Statistiche");
        statsButton.setToggleGroup(toggleGroup);
        statsButton.getStyleClass().add("nav-button");

        setupEventHandlers();
        container.getChildren().addAll(homeButton, statsButton);
    }
    
    private void setupEventHandlers() {
        homeButton.setOnAction(e -> {
            if (homeButton.isSelected()) {
                screenManager.switchToScreen(HomeScreen.SCREEN_ID);
            }
        });
        
        statsButton.setOnAction(e -> {
            if (statsButton.isSelected()) {
                screenManager.switchToScreen(StatsScreen.SCREEN_ID);
            }
        });
    }
    
    /**
     * Aggiorna la selezione della barra di navigazione per riflettere la schermata attiva.
     * 
     * @param screenId l'ID della schermata attualmente attiva
     */
    public void updateSelection(String screenId) {
        switch (screenId) {
            case HomeScreen.SCREEN_ID:
                homeButton.setSelected(true);
                break;
            case StatsScreen.SCREEN_ID:
                statsButton.setSelected(true);
                break;
            default:
                // Mantieni la selezione corrente se l'ID non è riconosciuto
                break;
        }
    }

    public Node getNode() {
        return container;
    }
}
