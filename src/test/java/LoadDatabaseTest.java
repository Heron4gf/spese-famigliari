import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.modules.backend.DatabaseModule;
import it.unicam.cs.mpgc.jbudget125639.modules.GlobalModule;
import it.unicam.cs.mpgc.jbudget125639.modules.ModulesHandler;
import it.unicam.cs.mpgc.jbudget125639.views.Global;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa il caricamento dei dati dal database, considerando che la collezione di View
 * contiene sia gli User caricati sia la vista Global stessa.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoadDatabaseTest {

    private final File TEST_DB_FILE = new File("database.db");

    /**
     * Prepara il database di test con dati iniziali prima dell'esecuzione dei test.
     */
    @BeforeAll
    void populateDatabase() throws Exception {
        // Usa un DatabaseModule temporaneo solo per inserire i dati
        DatabaseModule setupDbModule = new DatabaseModule();
        setupDbModule.load();

        var userDao = setupDbModule.getDatabase().getDao(User.class);
        // Pulisce la tabella per garantire un ambiente pulito
        userDao.deleteBuilder().delete();
        // Popola con dati di esempio
        userDao.create(new User("Alice"));
        userDao.create(new User("Bob"));
        userDao.create(new User("Charlie"));

        setupDbModule.unload();
    }

    /**
     * Rimuove il file del database di test dopo che tutti i test sono stati completati.
     */
    @AfterAll
    void cleanup() {
        TEST_DB_FILE.delete();
    }

    @Test
    @DisplayName("Dovrebbe caricare le view, distinguere gli utenti, e stamparli")
    void shouldLoadDataFromDatabaseAndPrint() {
        // 1. Inizializzazione dei moduli e del gestore
        DatabaseModule databaseModule = new DatabaseModule();
        ModulesHandler modulesManager = new ModulesHandler();
        GlobalModule globalModule = new GlobalModule(modulesManager);

        // 2. Aggiunta dei moduli al gestore nell'ordine di dipendenza corretto
        modulesManager.add(databaseModule);
        modulesManager.add(globalModule);

        // 3. Esecuzione del caricamento
        assertDoesNotThrow(modulesManager::load, "Il caricamento dei moduli non dovrebbe lanciare eccezioni");

        // 4. Verifica dei risultati
        GlobalModule loadedGlobalModule = modulesManager.getModule(GlobalModule.class);
        assertNotNull(loadedGlobalModule, "GlobalModule dovrebbe essere recuperabile dal gestore");

        Global global = loadedGlobalModule.getGlobal();
        assertNotNull(global, "L'oggetto Global non dovrebbe essere nullo dopo il caricamento");

        // Recupera le viste
        Collection<View> views = global.getViews();

        // Verifica che ci siano 4 viste in totale (3 Utenti + 1 Global)
        assertEquals(4, views.size(), "Dovrebbero esserci 4 viste totali (3 utenti + Global)");

        // Filtra per contare solo gli utenti e verifica che siano 3
        long userCount = views.stream().filter(view -> view instanceof User).count();
        assertEquals(3, userCount, "Il numero di utenti caricati non è corretto");

        // 5. Stampa dei dati caricati, gestendo i diversi tipi di View
        System.out.println("View caricate con successo dal database:");
        for (View view : views) {
            System.out.println("Trovata view: " + view.getName() + " di tipo " + view.getClass().getSimpleName());
            if (view instanceof User user) {
                // Questo blocco viene eseguito solo per gli utenti
                System.out.println("  -> È un utente: " + user.getName());
                assertNotNull(user.getName());
            }
        }

        // Verifica che i nomi degli utenti siano presenti nella collezione di View
        assertTrue(views.stream().anyMatch(v -> v.getName().equals("Alice")));
        assertTrue(views.stream().anyMatch(v -> v.getName().equals("Bob")));
        assertTrue(views.stream().anyMatch(v -> v.getName().equals("Charlie")));
        // Opzionale: verifica che anche la vista Global sia presente
        assertTrue(views.stream().anyMatch(v -> v instanceof Global), "La vista Global dovrebbe essere inclusa");

        // 6. Scaricamento dei moduli
        assertDoesNotThrow(modulesManager::unload, "Lo scaricamento non dovrebbe lanciare eccezioni");
        assertNull(loadedGlobalModule.getGlobal(), "L'oggetto Global dovrebbe essere nullo dopo lo scaricamento");
    }
}