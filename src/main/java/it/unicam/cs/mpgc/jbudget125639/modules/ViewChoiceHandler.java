package it.unicam.cs.mpgc.jbudget125639.modules;

import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.RequiresModulesManagerModule;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import it.unicam.cs.mpgc.jbudget125639.views.ViewsHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class ViewChoiceHandler extends RequiresModulesManagerModule {

    private ViewsHandler viewsHandler;

    @Getter @Setter
    @NonNull
    private View selectedView;

    public ViewChoiceHandler(ModulesManager modulesManager) {
        super(modulesManager);
    }

    public void setView(@NonNull String string) {
        @NonNull View view = viewsHandler.getView(string);
        selectedView(view);
    }

    @Override
    protected void internalLoad() {
        viewsHandler = modulesManager.getModule(GlobalModule.class).getGlobal();
        selectedView = viewsHandler.getViews().stream().toList().getFirst();
    }

    @Override
    protected void internalUnload() {

    }

    @Override
    public String name() {
        return "ViewChoiceHandler";
    }
}
