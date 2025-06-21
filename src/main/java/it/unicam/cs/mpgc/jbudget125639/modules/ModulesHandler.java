package it.unicam.cs.mpgc.jbudget125639.modules;

import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.Module;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Log
public class ModulesHandler implements ModulesManager {

    @NonNull
    private Collection<Module> modules = new LinkedList<>();

    @Override
    public void add(@NonNull Module... modules) {
        Stream.of(modules).forEach(this.modules::add);
    }

    @Override
    public void addAndLoad(@NonNull Module... modules) {
        add(modules);
        Stream.of(modules).forEach(this::loadModule);
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
            log.info("Loaded module: " + module.name());
        } catch (Exception e) {
            log.warning("Failed to load module " + module.name() + ": " + e.getMessage());
        }
    }

    @Override
    public void unload() {
        modules.forEach(this::unloadModule);
    }

    private void unloadModule(Module module) {
        try {
            module.unload();
            log.info("Unloaded module: " + module.name());
        } catch (Exception e) {
            log.warning("Failed to unload module " + module.name() + ": " + e.getMessage());
        }
    }
}