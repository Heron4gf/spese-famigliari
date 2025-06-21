package it.unicam.cs.mpgc.jbudget125639.database;

import com.j256.ormlite.table.TableUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

/*
 * Classe astratta che rappresenta un Database SQL
 */
@RequiredArgsConstructor
public abstract class SQLDatabase extends AbstractDatabase {

    @NonNull
    private Collection<Class<?>> classes;

    public SQLDatabase(Class<?>... classes) {
        this.classes = Arrays.asList(classes);
    }

    @Override
    public void load() throws Exception {
        super.load();
        loadTables();
    }

    protected void loadTables() throws SQLException {
        for (Class<?> clazz : classes) {
            TableUtils.createTableIfNotExists(connectionSource, clazz);
        }
    }
}
