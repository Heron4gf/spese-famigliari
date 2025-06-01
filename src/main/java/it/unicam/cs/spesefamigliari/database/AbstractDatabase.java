package it.unicam.cs.spesefamigliari.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import lombok.Getter;

public abstract class AbstractDatabase implements Database {

    @Getter
    protected ConnectionSource connectionSource;

    protected abstract String getDatabaseUrl();

    @Override
    public void load() throws Exception {
        connectionSource = new JdbcConnectionSource(getDatabaseUrl());
    }

    @Override
    public void unload() throws Exception {
        if (connectionSource != null) {
            connectionSource.close();
        }
    }
}
