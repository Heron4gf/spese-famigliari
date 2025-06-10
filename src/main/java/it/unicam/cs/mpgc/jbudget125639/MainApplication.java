package it.unicam.cs.mpgc.jbudget125639;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.money.Eur;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import it.unicam.cs.mpgc.jbudget125639.views.Global;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

public class MainApplication extends Application {

    private Global global;
    private static Collection<User> allUsersList; // Static reference to pass to controllers

    @Override
    public void start(Stage stage) throws IOException {
        User user1 = new User("Alice");
        User user2 = new User("Bob");

        // Example Transactions (updated to use IN/OUT and NamedTag)
        user1.addTransaction(new Transaction(user1, 1, TransactionDirection.IN, new MoneyAmount(1000.00, new Eur()), "Monthly Salary", Date.from(Instant.now().minusSeconds(86400 * 5)), Collections.emptySet()));
        user1.addTransaction(new Transaction(user1, 2, TransactionDirection.OUT, new MoneyAmount(75.50, new Eur()), "Weekly Groceries", Date.from(Instant.now().minusSeconds(86400 * 2)), new HashSet<>(Arrays.asList(NamedTag.ALIMENTARI))));
        user1.addTransaction(new Transaction(user1, 3, TransactionDirection.OUT, new MoneyAmount(15.20, new Eur()), "Coffee and Cake", Date.from(Instant.now()), new HashSet<>(Arrays.asList(NamedTag.INTRATTENIMENTO))));

        user2.addTransaction(new Transaction(user2, 4, TransactionDirection.IN, new MoneyAmount(500.00, new Eur()), "Freelance Payment", Date.from(Instant.now().minusSeconds(86400 * 10)), Collections.emptySet()));
        user2.addTransaction(new Transaction(user2, 5, TransactionDirection.OUT, new MoneyAmount(200.00, new Eur()), "Electricity Bill", Date.from(Instant.now().minusSeconds(86400 * 7)), new HashSet<>(Arrays.asList(NamedTag.UTENZE))));
        user2.addTransaction(new Transaction(user2, 6, TransactionDirection.OUT, new MoneyAmount(45.00, new Eur()), "Restaurant Dinner", Date.from(Instant.now().minusSeconds(86400 * 1)), new HashSet<>(Arrays.asList(NamedTag.ALIMENTARI, NamedTag.INTRATTENIMENTO))));

        allUsersList = Arrays.asList(user1, user2); // Store all users
        global = new Global(allUsersList);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        MainController controller = fxmlLoader.getController();
        controller.setViewsHandler(global);
        controller.setAllUsers(allUsersList); // Pass all users to MainController for dialog

        stage.setTitle("JBudget");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}