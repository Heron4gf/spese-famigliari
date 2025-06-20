package it.unicam.cs.mpgc.jbudget125639;

import it.unicam.cs.mpgc.jbudget125639.modules.*;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import lombok.Cleanup;
import lombok.SneakyThrows;

public class JBudgetLauncher {

    @SneakyThrows // Usato perché @Cleanup richiede gestione eccezioni, anche se il costruttore non le lancia
    public static void main(String[] args) {
        @Cleanup("unload") ModulesManager modulesManager = new ModulesHandler();
        modulesManager.addAndLoad(new DatabaseModule());
        modulesManager.addAndLoad(new GlobalModule(modulesManager));
        modulesManager.addAndLoad(new ViewChoiceHandler(modulesManager));
        modulesManager.addAndLoad(new JavaFXModule(modulesManager));
    }

}
