package it.unicam.cs.mpgc.jbudget125639.modules.abstracts;

import lombok.NonNull;

/**
 * Definisce l'interfaccia per un gestore di moduli, che è esso stesso un modulo.
 */
public interface ModulesManager extends it.unicam.cs.mpgc.jbudget125639.modules.abstracts.Module {
    /**
     * Recupera un modulo gestito in base alla sua classe.
     *
     * @param <T> il tipo del modulo.
     * @param moduleClass la classe del modulo da recuperare.
     * @return l'istanza del modulo del tipo richiesto, o null se non trovato.
     */
    <T extends Module> T getModule(Class<T> moduleClass);

    /**
     * Aggiunge un nuovo modulo al gestore senza caricarlo.
     *
     * @param module il modulo da aggiungere.
     * @throws NullPointerException se il modulo fornito è nullo.
     */
    void add(@NonNull Module module);

    /**
     * Aggiunge un nuovo modulo al gestore e lo carica immediatamente.
     *
     * @param module il modulo da aggiungere e caricare.
     * @throws NullPointerException se il modulo fornito è nullo.
     * @throws Exception se si verifica un errore durante il caricamento del modulo.
     */
    void addAndLoad(@NonNull Module module);

}