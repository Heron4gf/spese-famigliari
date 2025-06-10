package it.unicam.cs.mpgc.jbudget125639.database;

import com.j256.ormlite.support.ConnectionSource;

public interface Database {
    void load() throws Exception;
    void unload() throws Exception;

    ConnectionSource getConnectionSource();
}
