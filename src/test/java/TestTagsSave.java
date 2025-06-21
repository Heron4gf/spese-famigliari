import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.TransactionTag;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.modules.DatabaseModule;
import it.unicam.cs.mpgc.jbudget125639.modules.GlobalModule;
import it.unicam.cs.mpgc.jbudget125639.modules.ModulesHandler;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class TestTagsSave {

    private static ModulesManager manager = new ModulesHandler();

    @BeforeAll
    public static void init() {
        manager.addAndLoad(
                new DatabaseModule(),
                new GlobalModule(manager));
    }

    @Test
    public void saveTransactionWithTags() {
        User a = new User("test");

        Transaction aT = new Transaction(
                "descrizione",
                new MoneyAmount(10d, Currency.EUR),
                TransactionDirection.IN,
                new Date(Instant.now().toEpochMilli()),
                List.of(
                        NamedTag.ABBIGLIAMENTO,
                        NamedTag.SALUTE
                )
        );

        a.addTransaction(aT);
        manager.getModule(GlobalModule.class).getGlobal().addView(a);
        try {
            manager.getModule(GlobalModule.class).saveState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
