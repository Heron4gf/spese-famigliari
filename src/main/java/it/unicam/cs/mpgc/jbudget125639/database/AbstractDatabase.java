package it.unicam.cs.mpgc.jbudget125639.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import lombok.Getter;
import lombok.NonNull;

import java.sql.SQLException;

public abstract class AbstractDatabase implements Database {

    @Getter
    @NonNull
    protected ConnectionSource connectionSource;

    protected abstract String getDatabaseUrl();

    @Override
    public void load() throws Exception {
        connectionSource = new JdbcConnectionSource(getDatabaseUrl());
    }

    @Override
    public void unload() throws Exception {
        connectionSource.close();
    }

    @Override
    public <T> Dao<T, ?> getDao(@NonNull Class<T> clazz) throws SQLException {
        return DaoManager.createDao(connectionSource, clazz);
    }
}
