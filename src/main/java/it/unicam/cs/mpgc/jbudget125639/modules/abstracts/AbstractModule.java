package it.unicam.cs.mpgc.jbudget125639.modules.abstracts;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AbstractModule implements Module {

    private boolean loaded = false;

    @Override
    public void load() throws Exception {
        if(loaded) return;
        internalLoad();
        loaded = true;
    }

    @Override
    public void unload() throws Exception {
        if(!loaded) return;
        internalUnload();
        loaded = false;
    }

    protected abstract void internalLoad() throws Exception;
    protected abstract void internalUnload() throws Exception;
}
