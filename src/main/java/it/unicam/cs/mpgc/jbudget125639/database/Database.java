package it.unicam.cs.mpgc.jbudget125639.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Definisce l'interfaccia per la gestione di un database.
 */
public interface Database {
    /**
     * Carica e inizializza il database, stabilendo la connessione e creando le tabelle.
     *
     * @throws Exception se si verifica un errore durante l'inizializzazione del database.
     */
    void load() throws Exception;

    /**
     * Scarica il database, chiudendo la connessione e rilasciando le risorse.
     *
     * @throws Exception se si verifica un errore durante la chiusura della connessione.
     */
    void unload() throws Exception;

    /**
     * Restituisce la sorgente di connessione al database.
     *
     * @return la sorgente di connessione (`ConnectionSource`).
     */
    ConnectionSource getConnectionSource();

    /**
     * Restituisce un Data Access Object (DAO) per la classe di entità specificata.
     *
     * @param <T> il tipo dell'entità.
     * @param clazz la classe dell'entità per cui ottenere il DAO.
     * @return il DAO per l'entità specificata.
     * @throws Exception se si verifica un errore durante la creazione del DAO.
     */
    <T> Dao<T, ?> getDao(Class<T> clazz) throws Exception;
}