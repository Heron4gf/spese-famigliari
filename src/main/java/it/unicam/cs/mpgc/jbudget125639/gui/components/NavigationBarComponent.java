package it.unicam.cs.mpgc.jbudget125639.gui.components;

import it.unicam.cs.mpgc.jbudget125639.gui.builders.NodeBuilder;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.HomeScreen;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.ScreenManager;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.StatsScreen;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Componente per la barra di navigazione.
 * La sua istanza viene creata tramite il builder generato da Lombok.
 * Esempio: NavigationBarComponent.builder().screenManager(manager).build();
 */
@Getter
public class NavigationBarComponent implements NodeBuilder {
    private final HBox container;
    @NonNull private final ScreenManager screenManager;
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final LinkedHashMap<String, String> screenMap = new LinkedHashMap<>();

    /**
     * Costruttore della barra di navigazione.
     * Annotato con @Builder per generare un builder che richiede lo ScreenManager.
     * @param screenManager il gestore delle schermate, non può essere null.
     */
    @Builder
    public NavigationBarComponent(@NonNull ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(0);
        container.getStyleClass().add("nav-box");

        screenMap.put("Home", HomeScreen.SCREEN_ID);
        screenMap.put("Statistiche", StatsScreen.SCREEN_ID);

        initializeButtons();
    }

    private void initializeButtons() {
        List<ToggleButton> buttons = screenMap.entrySet().stream()
                .map(entry -> createNavButton(entry.getKey(), entry.getValue()))
                .toList();

        container.getChildren().addAll(buttons);

        if (!buttons.isEmpty()) {
            buttons.getFirst().setSelected(true);
        }
    }

    /**
     * Seleziona il primo pulsante e passa alla schermata corrispondente.
     * Dovrebbe essere chiamato dopo che tutte le schermate sono state registrate.
     */
    public void activateFirstScreen() {
        if (!screenMap.isEmpty()) {
            String firstScreenId = screenMap.values().iterator().next();
            if (screenManager.isScreenRegistered(firstScreenId)) {
                screenManager.switchToScreen(firstScreenId);
            }
        }
    }

    private ToggleButton createNavButton(String label, String screenId) {
        ToggleButton button = new ToggleButton(label);
        button.setToggleGroup(toggleGroup);
        button.getStyleClass().add("nav-button");

        button.setOnAction(e -> {
            if (button.isSelected()) {
                screenManager.switchToScreen(screenId);
            }
        });

        return button;
    }

    @Override
    public Node getNode() {
        return container;
    }
}