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
import org.junit.jupiter.api.Assertions; // Import Assertions class

import java.io.File; // Import File
import java.util.Arrays;
import java.util.Collection; // Import Collection
import java.util.List; // Import List

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
        // No specific setup needed here if @BeforeAll handles DB clearing
    }

    @BeforeAll
    public static void initDB() {
        database = new SQLiteDatabase(Main.DATABASE_PATH, User.class, Transaction.class);
        assertDoesNotThrow(() -> database.load()); // This will recreate the tables
    }

    @Test
    public void testMoneyAmount() {
        //assertThrows(Exception.class, () -> new MoneyAmount(-10d, new Eur()));
        //assertThrows(Exception.class, () -> new MoneyAmount(0, new Eur()));
    }

    @Test
    public void createUser() {
        user1 = new User("aldo_test_create"); // Use a unique name for this test if it might interact with DB
        user2 = new User("francesco_test_create");

        Transaction transaction1 = new Transaction(TransactionDirection.IN, new MoneyAmount(5.0, new Eur()), "test transaction for create user");
        user1.addTransaction(transaction1);

        global = new Global(Arrays.asList(user1, user2));
    }

    @Test
    public void saveUsers() {
        // Since @BeforeAll clears the DB, this "aldo" should be new for this test execution.
        user1 = new User("aldo");
        user2 = new User("francesco"); // user2 is not used in this specific test, but kept for context

        try {
            Dao<User, Integer> userDao = DaoManager.createDao(database.getConnectionSource(), User.class);
            // 1. Save the User object first to ensure it gets a generated ID from the database.
            userDao.createOrUpdate(user1);
            // At this point, user1.getId() will return a valid integer ID from the database.

            // 2. Create the Transaction object and associate it with the now-persisted User.
            // The addTransaction method will call transaction.setUser(this),
            // and 'this' (user1) now has its ID populated, correctly linking the transaction.
            Transaction transaction1 = new Transaction(TransactionDirection.IN, new MoneyAmount(5.0, new Eur()), "initial test transaction");
            user1.addTransaction(transaction1);

            // 3. Save the Transaction objects.
            // Now, when 't' is saved, 't.getUser().getId()' will correctly provide the user's ID,
            // allowing ORMLite to store it in the user_id foreign key.
            Dao<Transaction, Integer> transactionDao = DaoManager.createDao(database.getConnectionSource(), Transaction.class);
            for (Transaction t : user1.getTransactions()) {
                transactionDao.createOrUpdate(t);
            }

            // --- Verification Section ---
            // Load the user again to ensure its transactions are properly linked (if foreignAutoRefresh is used)
            // Or query transactions directly by user_id
            User loadedUser = userDao.queryForId(user1.getId());
            Assertions.assertNotNull(loadedUser, "Loaded user should not be null");
            Assertions.assertEquals("aldo", loadedUser.getName(), "Loaded user's name should match");

            // Query transactions associated with the saved user's ID
            List<Transaction> dbTransactions = transactionDao.queryForEq("user_id", user1.getId());

            Assertions.assertNotNull(dbTransactions, "Database transactions list should not be null");
            Assertions.assertFalse(dbTransactions.isEmpty(), "There should be at least one transaction in the database for user 'aldo'");
            Assertions.assertEquals(1, dbTransactions.size(), "Should have exactly one transaction for user 'aldo'");

            Transaction loadedTransaction = dbTransactions.get(0);
            Assertions.assertNotNull(loadedTransaction, "Loaded transaction should not be null");
            Assertions.assertNotNull(loadedTransaction.getUser(), "Loaded transaction's user should not be null");
            Assertions.assertEquals(user1.getId(), loadedTransaction.getUser().getId(), "Transaction's user_id should match the saved user's ID");
            Assertions.assertEquals(5.0, loadedTransaction.getAmount().toDouble(), "Transaction amount should match");
            Assertions.assertEquals("initial test transaction", loadedTransaction.getDescription(), "Transaction description should match");


        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to save users or transactions: " + e.getMessage());
        }
    }

    @Test
    public void testLoadAllUsersAndTransactions() {
        try {
            // First, ensure some data is in the DB for this test to load
            Dao<User, Integer> userDao = DaoManager.createDao(database.getConnectionSource(), User.class);
            Dao<Transaction, Integer> transactionDao = DaoManager.createDao(database.getConnectionSource(), Transaction.class);

            User userA = new User("UserA");
            userDao.createOrUpdate(userA); // Save userA to get an ID
            userA.addTransaction(new Transaction(TransactionDirection.IN, new MoneyAmount(100.0, new Eur()), "Salary UserA"));
            userA.addTransaction(new Transaction(TransactionDirection.OUT, new MoneyAmount(25.0, new Eur()), "Groceries UserA"));
            for (Transaction t : userA.getTransactions()) {
                transactionDao.createOrUpdate(t);
            }

            User userB = new User("UserB");
            userDao.createOrUpdate(userB); // Save userB to get an ID
            userB.addTransaction(new Transaction(TransactionDirection.IN, new MoneyAmount(50.0, new Eur()), "Gift UserB"));
            for (Transaction t : userB.getTransactions()) {
                transactionDao.createOrUpdate(t);
            }

            System.out.println("\n--- Loading All Users and their Transactions ---");

            // Query all users
            List<User> allUsers = userDao.queryForAll();

            Assertions.assertFalse(allUsers.isEmpty(), "There should be users in the database.");
            System.out.println("Total users found: " + allUsers.size());

            // Iterate through each user and their transactions
            for (User user : allUsers) {
                System.out.println("User ID: " + user.getId() + ", Name: " + user.getName());

                Collection<Transaction> userTransactions = user.getTransactions();
                if (userTransactions != null && !userTransactions.isEmpty()) {
                    System.out.println("  Transactions:");
                    for (Transaction transaction : userTransactions) {
                        System.out.println(
                                "    - ID: " + transaction.getId() +
                                        ", Direction: " + transaction.getDirection() +
                                        ", Amount: " + transaction.getAmount().toDouble() + " " + transaction.getAmount().getCurrency().name() +
                                        ", Description: " + transaction.getDescription() +
                                        ", Date: " + transaction.getDate() +
                                        ", User ID (from transaction object): " + (transaction.getUser() != null ? transaction.getUser().getId() : "N/A")
                        );
                        // Also verify that the user object linked in the transaction is the correct one
                        Assertions.assertNotNull(transaction.getUser(), "Transaction should have a linked user.");
                        Assertions.assertEquals(user.getId(), transaction.getUser().getId(), "Linked user ID should match parent user ID.");
                    }
                } else {
                    System.out.println("  No transactions found for this user.");
                }
            }
            System.out.println("--- End of Load All Users and their Transactions ---");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to load users and transactions: " + e.getMessage());
        }
    }
}