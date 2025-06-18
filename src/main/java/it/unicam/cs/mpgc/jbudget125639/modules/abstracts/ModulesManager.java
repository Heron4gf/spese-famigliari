package it.unicam.cs.mpgc.jbudget125639.modules.abstracts;

import lombok.NonNull;

public interface ModulesManager extends it.unicam.cs.mpgc.jbudget125639.modules.abstracts.Module {
    <T extends Module> T getModule(Class<T> moduleClass);

    void add(@NonNull Module module);
    void addAndLoad(@NonNull Module module);

}
