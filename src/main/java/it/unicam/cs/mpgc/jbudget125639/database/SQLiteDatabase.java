package it.unicam.cs.mpgc.jbudget125639.database;

import java.io.File;

/*
 * Classe concreta che rappresenta un database SQLite (driver JDBC)
 */
public class SQLiteDatabase extends SQLDatabase {

    private File databasePath;

    public SQLiteDatabase(File file, Class<?>... classes) {
        super(classes);
        this.databasePath = file;
    }

    @Override
    protected String getDatabaseUrl() {
        return "jdbc:sqlite:" + databasePath.toString();
    }
}

