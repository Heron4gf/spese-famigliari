package it.unicam.cs.mpgc.jbudget125639.gui.services;

import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.gui.services.validation.ValidationException;
import it.unicam.cs.mpgc.jbudget125639.gui.services.validation.ValidationService;
import it.unicam.cs.mpgc.jbudget125639.modules.GlobalModule;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Servizio per la gestione degli utenti.
 * Fornisce metodi per creare, validare e aggiungere utenti al modulo globale.
 */
@RequiredArgsConstructor
public class UserService {

    private final ValidationService validationService;
    private final ModulesManager modulesManager;

    /**
     * Crea e valida un nuovo utente.
     *
     * @param name il nome dell'utente
     * @return l'utente creato e validato
     * @throws ValidationException se la validazione fallisce
     */
    public User createUser(@NonNull String name) throws ValidationException {
        String trimmedName = name.trim();
        User user = validationService.validateAndReturn(new User(trimmedName));
        return user;
    }

    /**
     * Aggiunge un utente al modulo globale.
     *
     * @param user l'utente da aggiungere
     * @throws ValidationException se la validazione fallisce
     */
    public void addUserToGlobal(@NonNull User user) throws ValidationException {
        validationService.validateAndReturn(user);
        modulesManager.getModule(GlobalModule.class).getGlobal().addView(user);
    }

    /**
     * Crea un nuovo utente e lo aggiunge al modulo globale.
     *
     * @param name il nome dell'utente
     * @return l'utente creato
     * @throws ValidationException se la validazione fallisce
     */
    public User createAndAddUser(@NonNull String name) throws ValidationException {
        User user = createUser(name);
        addUserToGlobal(user);
        return user;
    }
}