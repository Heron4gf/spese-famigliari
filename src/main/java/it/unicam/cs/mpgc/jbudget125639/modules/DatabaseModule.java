package it.unicam.cs.mpgc.jbudget125639.modules;

import it.unicam.cs.mpgc.jbudget125639.database.Database;
import it.unicam.cs.mpgc.jbudget125639.database.SQLiteDatabase;
import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.AbstractModule;
import lombok.Getter;

import java.io.File;

public class DatabaseModule extends AbstractModule {

    private final File DATABASE_FILE = new File(".", "database.db");

    @Getter
    private Database database = new SQLiteDatabase(DATABASE_FILE, User.class, Transaction.class);

    @Override
    protected void internalLoad() throws Exception {
        database.load();
    }

    @Override
    protected void internalUnload() throws Exception {
        database.unload();
    }

    @Override
    public String name() {
        return "Database";
    }
}
