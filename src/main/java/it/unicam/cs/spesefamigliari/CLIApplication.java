package it.unicam.cs.spesefamigliari;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import it.unicam.cs.spesefamigliari.database.Database;
import it.unicam.cs.spesefamigliari.database.SQLiteDatabase;
import it.unicam.cs.spesefamigliari.entities.Transaction;
import it.unicam.cs.spesefamigliari.entities.User;
import it.unicam.cs.spesefamigliari.filters.TimeSpan;
import it.unicam.cs.spesefamigliari.filters.TransactionDirection;
import it.unicam.cs.spesefamigliari.views.Global;
import it.unicam.cs.spesefamigliari.views.View;
import it.unicam.cs.spesefamigliari.views.ViewsHandler;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;

public class CLIApplication {

    private static ViewsHandler viewsHandler;
    private static File DATABASE_PATH = new File(".","database.db");

    private static Database database;

    public static void main(String[] args) throws SQLException {
        database = new SQLiteDatabase(DATABASE_PATH, User.class, Transaction.class);
        viewsHandler = new Global(loadUsers());

        for(View view : viewsHandler.getViews()) {
            System.out.println(view.getName());
            for(Transaction transaction : view.getFiltered(TimeSpan.EVER, TransactionDirection.IN)) {
                System.out.println(transaction.getDescription());
            }
        }
    }

    private static Collection<User> loadUsers() throws SQLException {
        Dao<User, Integer> userDao = DaoManager.createDao(database.getConnectionSource(), User.class);
        return userDao.queryForAll();
    }

}
