package it.unicam.cs.mpgc.jbudget125639.modules.abstracts;

public interface ModulesManager extends it.unicam.cs.mpgc.jbudget125639.modules.abstracts.Module {
    <T extends Module> T getModule(Class<T> moduleClass);
}
