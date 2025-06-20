package it.unicam.cs.mpgc.jbudget125639.gui.services;

import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.modules.GlobalModule;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import it.unicam.cs.mpgc.jbudget125639.views.ViewsHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ViewService {

    private final ModulesManager modulesManager;
    private View selectedView;

    private ViewsHandler getViewsHandler() {
        return modulesManager.getModule(GlobalModule.class).getGlobal();
    }

    public View getCurrentView() {
        if (selectedView == null) {
            selectedView = fallBackView();
        }
        return selectedView;
    }

    public void setCurrentView(@NonNull String viewName) {
        this.selectedView = getViewsHandler().getView(viewName);
        safeUpdate();
    }

    public ObservableList<String> getViewNames() {
        return getViewsHandler().getViews().stream()
                .map(View::getName)
                .sorted()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public void deleteView(@NonNull String viewName) {
        View view = getViewsHandler().getView(viewName);
        getViewsHandler().getViews().remove(view);
        safeUpdate();
    }

    private View fallBackView() {
        ViewsHandler handler = getViewsHandler();
        return handler.getViews().stream()
                .filter(v -> v.getName().equalsIgnoreCase("global"))
                .findFirst()
                .orElseGet(() -> handler.getViews().stream().findFirst()
                        .orElseThrow(() -> new IllegalStateException("Nessuna view disponibile.")));
    }

    private void safeUpdate() {
        if (this.selectedView == null) {
            this.selectedView = fallBackView();
        }
    }

    public boolean isCurrentViewUser() {
        return getCurrentView() instanceof User;
    }

    public User getCurrentViewAsUser() {
        View current = getCurrentView();
        if (!(current instanceof User)) {
            throw new IllegalStateException("Current view is not a User");
        }
        return (User) current;
    }
}