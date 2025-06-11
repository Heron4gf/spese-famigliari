package it.unicam.cs.mpgc.jbudget125639.modules;

import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.Module;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class ModulesHandler implements ModulesManager {
    private final Logger logger;
    private final List<Module> modules = new ArrayList<>();

    public void addModule(Module module) {
        this.modules.add(module);
    }

    @Override
    public <T extends Module> T getModule(Class<T> moduleClass) {
        return modules.stream()
                .filter(moduleClass::isInstance)
                .map(moduleClass::cast)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String name() {
        return "ModulesManager";
    }

    @Override
    public void load() {
        modules.forEach(this::loadModule);
    }

    private void loadModule(Module module) {
        try {
            module.load();
            logger.info("Loaded module: " + module.name());
        } catch (Exception e) {
            logger.warning("Failed to load module " + module.name() + ": " + e.getMessage());
        }
    }

    @Override
    public void unload() {
        modules.forEach(this::unloadModule);
    }

    private void unloadModule(Module module) {
        try {
            module.unload();
            logger.info("Unloaded module: " + module.name());
        } catch (Exception e) {
            logger.warning("Failed to unload module " + module.name() + ": " + e.getMessage());
        }
    }
}