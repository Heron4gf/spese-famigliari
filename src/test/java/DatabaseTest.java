import com.j256.ormlite.dao.Dao;
import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import it.unicam.cs.mpgc.jbudget125639.modules.DatabaseModule;
import org.junit.jupiter.api.*;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseTest {

    private DatabaseModule databaseModule;
    private Dao<User, Integer> userDao;
    private Dao<Transaction, Integer> transactionDao;

    // Modifica a DatabaseModule per accettare un file di database per i test
    // public DatabaseModule(File databaseFile) { ... }

    @BeforeAll
    void setup() throws Exception {
        // Utilizza un database separato per i test per non interferire con quello di produzione
        databaseModule = new DatabaseModule();
        databaseModule.load();
        userDao = (Dao<User, Integer>) databaseModule.getDatabase().getDao(User.class);
        transactionDao = (Dao<Transaction, Integer>) databaseModule.getDatabase().getDao(Transaction.class);
    }

    @AfterAll
    void teardown() throws Exception {
        databaseModule.unload();
        // Elimina il file del database di test
        new File("test_budget.db").delete();
    }

    @BeforeEach
    void cleanup() throws Exception {
        // Pulisce le tabelle prima di ogni test per garantire l'isolamento
        transactionDao.deleteBuilder().delete();
        userDao.deleteBuilder().delete();
    }

    @Test
    @DisplayName("Crea e recupera un utente")
    void shouldCreateAndRetrieveUser() throws SQLException {
        User user = new User("testUser");
        userDao.create(user);

        User retrievedUser = userDao.queryForId(user.getId());

        assertNotNull(retrievedUser, "L'utente non dovrebbe essere nullo dopo il recupero");
        assertEquals("testUser", retrievedUser.getName(), "Il nome dell'utente non corrisponde");
    }

    @Test
    @DisplayName("Aggiunge una transazione a un utente")
    void shouldAddTransactionToUser() throws SQLException {
        // 1. Creazione e salvataggio dell'utente
        User user = new User("transactionUser");
        userDao.create(user);

        // 2. Creazione della transazione e associazione tramite il metodo di convenienza
        Transaction transaction = new Transaction(TransactionDirection.IN, new MoneyAmount(150.50, Currency.EUR), "Stipendio");
        user.addTransaction(transaction);

        // 3. Salvataggio della transazione (che ora ha il riferimento all'utente)
        transactionDao.create(transaction);

        // 4. Refresh dell'oggetto utente per caricare la ForeignCollection
        userDao.refresh(user);

        // 5. Verifiche
        assertFalse(user.getTransactions().isEmpty(), "La collezione di transazioni non dovrebbe essere vuota");
        assertEquals(1, user.getTransactions().size(), "Ci dovrebbe essere una sola transazione");
        assertEquals("Stipendio", user.getTransactions().iterator().next().getDescription(), "La descrizione della transazione non è corretta");
    }

    @Test
    @DisplayName("Elimina una transazione senza eliminare l'utente")
    void shouldDeleteTransactionWithoutDeletingUser() throws SQLException {
        User user = new User("userWithTwoTransactions");
        userDao.create(user);

        Transaction t1 = new Transaction(TransactionDirection.OUT, new MoneyAmount(50.0, Currency.EUR), "Cena");
        Transaction t2 = new Transaction(TransactionDirection.OUT, new MoneyAmount(20.0, Currency.EUR), "Cinema");
        user.addTransaction(t1);
        user.addTransaction(t2);
        transactionDao.create(t1);
        transactionDao.create(t2);

        userDao.refresh(user);
        assertEquals(2, user.getTransactions().size(), "L'utente dovrebbe avere 2 transazioni prima dell'eliminazione");

        // Elimina solo una transazione
        transactionDao.delete(t1);

        // Refresh dell'utente per aggiornare la collezione
        userDao.refresh(user);

        assertEquals(1, user.getTransactions().size(), "L'utente dovrebbe avere 1 transazione dopo l'eliminazione");
        assertNotNull(userDao.queryForId(user.getId()), "L'utente non dovrebbe essere stato eliminato");
    }

    @Test
    @DisplayName("Calcola il totale delle transazioni in entrata usando un filtro")
    void shouldCalculateTotalForIncomingTransactionsWithFilter() throws SQLException {
        User user = new User("filteringUser");
        userDao.create(user);

        Transaction t1 = new Transaction(TransactionDirection.IN, new MoneyAmount(1000.0, Currency.EUR), "Stipendio");
        Transaction t2 = new Transaction(TransactionDirection.OUT, new MoneyAmount(75.0, Currency.EUR), "Spesa");
        Transaction t3 = new Transaction(TransactionDirection.IN, new MoneyAmount(200.0, Currency.EUR), "Bonus");

        user.addTransaction(t1);
        user.addTransaction(t2);
        user.addTransaction(t3);

        transactionDao.create(List.of(t1, t2, t3));
        userDao.refresh(user);

        // Filtro per le sole transazioni in entrata
        IFilter incomeFilter = (transaction) -> transaction.getDirection() == TransactionDirection.IN;

        // Calcola il totale usando il filtro
        double total = user.total(incomeFilter);

        assertEquals(1200.0, total, 0.001, "Il totale delle entrate non è corretto");
    }

}