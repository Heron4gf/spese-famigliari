package it.unicam.cs.mpgc.jbudget125639.modules.headerbarinit;

import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.AbstractModule;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;

/*
 * Modulo inizializzazione container
 */
@RequiredArgsConstructor
public class InitContainerModule extends AbstractModule {

    private final HBox container;

    @Override
    protected void internalLoad() {
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(10, 20, 10, 20));
        container.getStyleClass().add("header");
    }

    @Override
    protected void internalUnload() {

    }

    @Override
    public String name() {
        return "InitContainer";
    }
}
