
import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.modules.DatabaseModule;
import it.unicam.cs.mpgc.jbudget125639.modules.GlobalModule;
import it.unicam.cs.mpgc.jbudget125639.modules.JavaFXModule;
import it.unicam.cs.mpgc.jbudget125639.modules.ModulesHandler;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;

import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PersistenceTest {

    private static ModulesManager modulesManager;

    @SneakyThrows // Usato perché @Cleanup richiede gestione eccezioni, anche se il costruttore non le lancia
    @BeforeAll
    public static void start() {
        modulesManager = new ModulesHandler();
        modulesManager.addAndLoad(new DatabaseModule());
        modulesManager.addAndLoad(new GlobalModule(modulesManager));
    }

    @Test
    public void addUser() {
        User user = new User("test");
        Transaction a = new Transaction(TransactionDirection.IN, new MoneyAmount(10d, Currency.EUR), "description");

        user.addTransaction(a);
        assertDoesNotThrow(() -> modulesManager.getModule(GlobalModule.class).saveState());
    }
}