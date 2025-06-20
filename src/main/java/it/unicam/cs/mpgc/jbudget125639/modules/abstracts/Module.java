package it.unicam.cs.mpgc.jbudget125639.modules.abstracts;

/**
 * Definisce l'interfaccia per un modulo generico con funzionalità di caricamento e scaricamento.
 */
public interface Module {

    /**
     * Restituisce il nome del modulo.
     *
     * @return il nome del modulo.
     */
    String name();

    /**
     * Carica e inizializza il modulo.
     *
     * @throws Exception se si verifica un errore durante il caricamento del modulo.
     */
    void load() throws Exception;

    /**
     * Scarica il modulo, rilasciando eventuali risorse.
     *
     * @throws Exception se si verifica un errore durante lo scaricamento del modulo.
     */
    void unload() throws Exception;

}