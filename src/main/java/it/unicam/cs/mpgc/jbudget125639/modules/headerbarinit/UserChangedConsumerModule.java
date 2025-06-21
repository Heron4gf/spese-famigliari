package it.unicam.cs.mpgc.jbudget125639.modules.headerbarinit;

import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.AbstractModule;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class UserChangedConsumerModule extends AbstractModule {

    private final Consumer<String> onUserChanged;
    private final ComboBox<String> userComboBox;

    private Runnable listenerRemover;

    @Override
    protected void internalLoad() {
        EventHandler<ActionEvent> userChangeHandler = event -> {
            String selected = userComboBox.getValue();
            if (selected != null) {
                onUserChanged.accept(selected);
            }
        };

        userComboBox.setOnAction(userChangeHandler);

        listenerRemover = () -> userComboBox.setOnAction(null);
    }

    @Override
    protected void internalUnload() {
        if (listenerRemover != null) {
            listenerRemover.run();
        }
    }

    @Override
    public String name() {
        return "UserChangedConsumerModule";
    }
}