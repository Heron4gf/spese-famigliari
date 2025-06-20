package it.unicam.cs.mpgc.jbudget125639.modules;

import com.j256.ormlite.dao.Dao;
import it.unicam.cs.mpgc.jbudget125639.database.Database;
import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.TransactionTag;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.PriorityTag;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.RequiresModulesManagerModule;
import it.unicam.cs.mpgc.jbudget125639.views.Global;
import it.unicam.cs.mpgc.jbudget125639.views.View;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class GlobalModule extends RequiresModulesManagerModule {

    private Global global;

    public GlobalModule(ModulesManager modulesManager) {
        super(modulesManager);
    }

    @Override
    public String name() {
        return "Global";
    }

    @Override
    public void internalLoad() throws Exception {
        DatabaseModule dbModule = modulesManager.getModule(DatabaseModule.class);
        Objects.requireNonNull(dbModule, "DatabaseModule dependency not loaded");

        Database database = dbModule.getDatabase();
        Dao<User, ?> userDao = database.getDao(User.class);

        List<View> views = userDao.queryForAll().stream()
                .map(u -> (View) u)
                .collect(Collectors.toList());

        this.global = new Global(views);
    }


    public void saveState() throws Exception {
        if (global == null) return;

        DatabaseModule dbModule = modulesManager.getModule(DatabaseModule.class);
        Objects.requireNonNull(dbModule, "DatabaseModule dependency not loaded");

        Database database = dbModule.getDatabase();
        Dao<User, ?> userDao = database.getDao(User.class);
        Dao<Transaction, ?> transactionDao = database.getDao(Transaction.class);
        Dao<TransactionTag, ?> transactionTagDao = database.getDao(TransactionTag.class);
        Dao<PriorityTag, ?> priorityTagDao = database.getDao(PriorityTag.class);

        global.getViews().stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .forEach(user -> saveUser(userDao, transactionDao, transactionTagDao, priorityTagDao, user));
    }

    @SneakyThrows
    private void saveUser(Dao<User, ?> userDao, Dao<Transaction, ?> transactionDao, 
                         Dao<TransactionTag, ?> transactionTagDao, Dao<PriorityTag, ?> priorityTagDao, User user) {
        userDao.createOrUpdate(user);
        user.getTransactions().forEach(transaction -> {
            try {
                transactionDao.createOrUpdate(transaction);
                
                // Save transaction tags
                for (TransactionTag transactionTag : transaction.getTransactionTags()) {
                    // Ensure the PriorityTag exists in database
                    PriorityTag priorityTag = transactionTag.getTag();
                    List<PriorityTag> existingTags = priorityTagDao.queryForEq("name", priorityTag.getName());
                    
                    if (existingTags.isEmpty()) {
                        // Create new tag if it doesn't exist
                        priorityTagDao.createOrUpdate(priorityTag);
                    } else {
                        // Use existing tag from database
                        priorityTag = existingTags.get(0);
                        transactionTag = new TransactionTag(transaction, priorityTag);
                    }
                    
                    // Save the transaction tag relationship
                    transactionTagDao.createOrUpdate(transactionTag);
                }
                
            } catch (SQLException e) {
                throw new RuntimeException("Errore durante il salvataggio della transazione e dei suoi tag", e);
            }
        });
    }


    @Override
    public void internalUnload() throws Exception {
        saveState();
    }
}
