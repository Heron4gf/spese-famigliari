package it.unicam.cs.mpgc.jbudget125639.modules;

import com.j256.ormlite.dao.Dao;
import it.unicam.cs.mpgc.jbudget125639.database.Database;
import it.unicam.cs.mpgc.jbudget125639.database.SQLiteDatabase;
import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.TransactionTag;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.PriorityTag;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.Module;
import lombok.Getter;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;

public class DatabaseModule implements Module {
    private final File DATABASE_FILE = new File("database.db");
    @Getter
    private final Database database = new SQLiteDatabase(DATABASE_FILE, User.class, Transaction.class, PriorityTag.class, TransactionTag.class);

    @Override
    public String name() {
        return "Database";
    }

    @Override
    public void load() throws Exception {
        database.load();
        createDefaultTags();
    }

    @Override
    public void unload() throws Exception {
        database.unload();
    }

    private void createDefaultTags() throws Exception {
        Dao<PriorityTag, ?> tagDao = database.getDao(PriorityTag.class);
        Arrays.stream(NamedTag.values())
                .map(namedTag -> new PriorityTag(namedTag.getName(), namedTag.getPriority()))
                .forEach(tag -> createTagIfNotExists(tagDao, tag));
    }

    private void createTagIfNotExists(Dao<PriorityTag, ?> dao, PriorityTag tag) {
        try {
            dao.createIfNotExists(tag);
        } catch (SQLException e) {
            throw new RuntimeException("Could not create default tag: " + tag.getName(), e);
        }
    }
}