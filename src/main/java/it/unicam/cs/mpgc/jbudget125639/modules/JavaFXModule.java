package it.unicam.cs.mpgc.jbudget125639.modules;

import it.unicam.cs.mpgc.jbudget125639.gui.JBudgetApp;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.RequiresModulesManagerModule;
import javafx.application.Application;

public class JavaFXModule extends RequiresModulesManagerModule {

    public JavaFXModule(ModulesManager modulesManager) {
        super(modulesManager);
    }

    @Override
    public String name() {
        return "JavaFX GUI";
    }

    /**
     * Avvia l'applicazione JavaFX.
     * Questo metodo è bloccante: il thread principale attenderà qui finché
     * l'applicazione grafica non verrà chiusa.
     */
    @Override
    protected void internalLoad() {
        JBudgetApp.setModulesManager(modulesManager);
        Application.launch(JBudgetApp.class);
    }

    /**
     * In questo contesto, lo scaricamento è gestito dalla chiusura
     * dell'applicazione stessa, quindi non è necessaria alcuna azione specifica.
     */
    @Override
    protected void internalUnload() {

    }
}
