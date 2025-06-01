package it.unicam.cs.spesefamigliari.database;

import com.j256.ormlite.support.ConnectionSource;

public interface Database {
    void load() throws Exception;
    void unload() throws Exception;

    ConnectionSource getConnectionSource();
}
