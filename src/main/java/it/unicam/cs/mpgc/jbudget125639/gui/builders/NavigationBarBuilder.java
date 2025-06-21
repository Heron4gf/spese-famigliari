package it.unicam.cs.mpgc.jbudget125639.gui.builders;

import it.unicam.cs.mpgc.jbudget125639.gui.screens.HomeScreen;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.ScreenManager;
import it.unicam.cs.mpgc.jbudget125639.gui.screens.StatsScreen;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;

import static it.unicam.cs.mpgc.jbudget125639.gui.builders.BuilderUtils.requireNonNull;

/**
 * Builder for creating NavigationBar components with required dependencies.
 */
public class NavigationBarBuilder implements ComponentBuilder<NavigationBarBuilder.NavigationBarComponent, NavigationBarBuilder> {

    private ScreenManager screenManager;

    /**
     * Sets the screen manager dependency.
     *
     * @param screenManager the screen manager
     * @return this builder for method chaining
     */
    public NavigationBarBuilder withScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
        return self();
    }

    @Override
    public NavigationBarComponent build() {
        return new NavigationBarComponent(requireNonNull(screenManager, "Screen manager"));
    }

    @Override
    public NavigationBarBuilder self() {
        return this;
    }

    @Getter
    public static class NavigationBarComponent implements NodeBuilder {
        private final HBox container;
        private final ScreenManager screenManager;
        private final ToggleGroup toggleGroup = new ToggleGroup();
        private final LinkedHashMap<String, String> screenMap = new LinkedHashMap<>();

        /**
         * Costruttore della barra di navigazione.
         *
         * @param screenManager il gestore delle schermate
         */
        public NavigationBarComponent(ScreenManager screenManager) {
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
                    .toList(); // oppure collect(Collectors.toList()) se usi Java < 16

            buttons.forEach(container.getChildren()::add);

            if (!buttons.isEmpty()) {
                ToggleButton firstButton = buttons.getFirst();
                firstButton.setSelected(true);
                // Don't switch to screen immediately - let the application handle this after screens are registered
            }
        }
        /**
         * Selects the first button and switches to its corresponding screen.
         * This should be called after all screens have been registered.
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
}
