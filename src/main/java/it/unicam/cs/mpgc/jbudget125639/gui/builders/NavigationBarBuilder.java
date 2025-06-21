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
        private final ToggleButton homeButton;
        private final ToggleButton statsButton;

        /**
         * Costruttore della barra di navigazione.
         *
         * @param screenManager il gestore delle schermate
         */
        public NavigationBarComponent(ScreenManager screenManager) {
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

        @Override
        public Node getNode() {
            return container;
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
    }
}
