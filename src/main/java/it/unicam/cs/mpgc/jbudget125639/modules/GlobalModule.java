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

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * gestore viste e dati
 */
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
        Dao<PriorityTag, ?> tagDao = database.getDao(PriorityTag.class);

        global.getViews().stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .forEach(user -> {
                    try {
                        saveUser(userDao, transactionDao, transactionTagDao, tagDao, user);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void saveUser(Dao<User, ?> userDao, Dao<Transaction, ?> transactionDao, Dao<TransactionTag, ?> transactionTagDao, Dao<PriorityTag, ?> tagDao, User user) throws SQLException {
        userDao.createOrUpdate(user);

        for (Transaction transaction : user.getTransactions()) {
            transactionDao.createOrUpdate(transaction);

            for (TransactionTag transactionTag : transaction.getTransactionTags()) {
                PriorityTag persistedTag = getOrCreateTag(tagDao, transactionTag.getTag());
                transactionTag.setTransaction(transaction);
                transactionTag.setTag(persistedTag);
                transactionTagDao.createOrUpdate(transactionTag);
            }
        }
    }

    private PriorityTag getOrCreateTag(Dao<PriorityTag, ?> tagDao, PriorityTag tag) {
        try {
            PriorityTag existing = tagDao.queryBuilder().where().eq("name", tag.getName()).queryForFirst();
            if (existing != null) {
                return existing;
            }

            tagDao.create(tag);
            return tag;

        } catch (SQLException e) {
            throw new RuntimeException("Error while saving or retrieving tag: " + tag.getName(), e);
        }
    }


    @Override
    public void internalUnload() throws Exception {
        saveState();
    }
}