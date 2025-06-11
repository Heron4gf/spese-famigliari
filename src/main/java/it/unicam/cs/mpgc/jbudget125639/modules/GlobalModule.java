package it.unicam.cs.mpgc.jbudget125639.modules;

import com.j256.ormlite.dao.Dao;
import it.unicam.cs.mpgc.jbudget125639.database.Database;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.Module;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import it.unicam.cs.mpgc.jbudget125639.views.Global;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Objects;

@RequiredArgsConstructor
public class GlobalModule implements Module {
    private final ModulesManager modulesManager;
    @Getter
    private Global global;

    @Override
    public String name() {
        return "Global";
    }

    @Override
    public void load() throws Exception {
        DatabaseModule dbModule = modulesManager.getModule(DatabaseModule.class);
        Objects.requireNonNull(dbModule, "DatabaseModule dependency not loaded");

        Database database = dbModule.getDatabase();
        Dao<User, ?> userDao = database.getDao(User.class);
        this.global = new Global(userDao.queryForAll());
    }

    @Override
    public void unload() {
        this.global = null;
    }
}