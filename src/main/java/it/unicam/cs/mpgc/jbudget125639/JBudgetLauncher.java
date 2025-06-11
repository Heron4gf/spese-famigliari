package it.unicam.cs.mpgc.jbudget125639;

import it.unicam.cs.mpgc.jbudget125639.modules.DatabaseModule;
import it.unicam.cs.mpgc.jbudget125639.modules.FrontendModule;
import it.unicam.cs.mpgc.jbudget125639.modules.GlobalModule;
import it.unicam.cs.mpgc.jbudget125639.modules.ModulesHandler;
import javafx.application.Application;

import java.util.logging.Logger;

public class JBudgetLauncher {
    public static ModulesHandler modulesManager;

    public static void main(String[] args) {
        Logger logger = Logger.getGlobal();
        modulesManager = new ModulesHandler(logger);

        initializeBackendModules();
        loadModules(logger);
        addShutdownHook();

        Application.launch(FrontendModule.class, args);
    }

    private static void initializeBackendModules() {
        modulesManager.addModule(new DatabaseModule());
        modulesManager.addModule(new GlobalModule(modulesManager));
    }

    private static void loadModules(Logger logger) {
        try {
            modulesManager.load();
        } catch (Exception e) {
            logger.severe("Failed to start application: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                modulesManager.unload();
            } catch (Exception e) {
                Logger.getGlobal().severe("Error during shutdown: " + e.getMessage());
            }
        }));
    }
}