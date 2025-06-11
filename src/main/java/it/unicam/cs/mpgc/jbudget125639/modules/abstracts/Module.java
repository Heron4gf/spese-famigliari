package it.unicam.cs.mpgc.jbudget125639.modules.abstracts;

public interface Module {

    String name();

    void load() throws Exception;

    void unload() throws Exception;

}
