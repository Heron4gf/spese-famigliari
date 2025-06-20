package it.unicam.cs.mpgc.jbudget125639.modules.backend;

import com.j256.ormlite.dao.Dao;
import it.unicam.cs.mpgc.jbudget125639.database.Database;
import it.unicam.cs.mpgc.jbudget125639.database.SQLiteDatabase;
import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.TransactionTag;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.PriorityTag;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.AbstractModule;
import lombok.*;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;

@NoArgsConstructor
@AllArgsConstructor
public class DatabaseModule extends AbstractModule {
    private final File DATABASE_FILE = new File("database.db");

    @Getter @NonNull
    private Database database = new SQLiteDatabase(DATABASE_FILE, User.class, Transaction.class, PriorityTag.class, TransactionTag.class);

    @Override
    public String name() {
        return "Database";
    }

    @Override
    public void internalLoad() throws Exception {
        database.load();
        createDefaultTags();
    }

    @Override
    public void internalUnload() throws Exception {
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
            if (dao.queryForEq("name", tag.getName()).isEmpty()) {
                dao.create(tag);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not create or query for default tag: " + tag.getName(), e);
        }
    }
}