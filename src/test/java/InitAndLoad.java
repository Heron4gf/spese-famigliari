import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import it.unicam.cs.spesefamigliari.Main;
import it.unicam.cs.spesefamigliari.database.Database;
import it.unicam.cs.spesefamigliari.database.SQLiteDatabase;
import it.unicam.cs.spesefamigliari.entities.Transaction;
import it.unicam.cs.spesefamigliari.entities.User;
import it.unicam.cs.spesefamigliari.filters.TransactionDirection;
import it.unicam.cs.spesefamigliari.money.Eur;
import it.unicam.cs.spesefamigliari.money.MoneyAmount;
import it.unicam.cs.spesefamigliari.views.Global;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

public class InitAndLoad {

    private static Database database;
    private User user1;
    private User user2;
    private Global global;

    private Validator validator;


    @BeforeEach
    public void setUp() {

    }

    @BeforeAll
    public static void initDB() {
        database = new SQLiteDatabase(Main.DATABASE_PATH, User.class, Transaction.class);
        assertDoesNotThrow(() -> database.load());
    }

    @Test
    public void testMoneyAmount() {
        //assertThrows(Exception.class, () -> new MoneyAmount(-10d, new Eur()));
        //assertThrows(Exception.class, () -> new MoneyAmount(0, new Eur()));
    }

    @Test
    public void createUser() {
        user1 = new User("aldo");
        user2 = new User("francesco");

        Transaction transaction1 = new Transaction(TransactionDirection.IN, new MoneyAmount(5.0, new Eur()), "test");
        user1.addTransaction(transaction1);

        global = new Global(Arrays.asList(user1, user2));
    }

    @Test
    public void saveUsers() {
        user1 = new User("aldo");
        user2 = new User("francesco");

        Transaction transaction1 = new Transaction(TransactionDirection.IN, new MoneyAmount(5.0, new Eur()), "test");
        user1.addTransaction(transaction1);

        try {
            Dao<User, Integer> dao = DaoManager.createDao(database.getConnectionSource(), User.class);
            dao.createOrUpdate(user1);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        for(Transaction t : user1.getTransactions()) {
            try {
                Dao<Transaction, Integer> dao = DaoManager.createDao(database.getConnectionSource(), Transaction.class);
                dao.createOrUpdate(t);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
    }

}
