package it.unicam.cs.spesefamigliari.database;

import java.io.File;

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

