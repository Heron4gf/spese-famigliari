package it.unicam.cs.mpgc.jbudget125639.modules;

import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.Module;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.*;
import java.util.logging.Logger;

@AllArgsConstructor
@NoArgsConstructor
public class ModulesHandler implements ModulesManager {

    @NonNull
    private Logger logger = Logger.getGlobal();
    @NonNull
    private Collection<Module> modules = new LinkedList<>();

    public ModulesHandler(Module... modules) {
        this(Logger.getGlobal(), Arrays.asList(modules));
    }

    @Override
    public void add(@NonNull Module module) {
        this.modules.add(module);
    }

    @Override
    public void addAndLoad(@NonNull Module module) {
        add(module);
        loadModule(module);
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