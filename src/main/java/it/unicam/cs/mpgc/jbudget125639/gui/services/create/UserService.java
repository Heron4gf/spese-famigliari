package it.unicam.cs.mpgc.jbudget125639.gui.services.create;

import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.gui.services.validation.ValidationException;
import it.unicam.cs.mpgc.jbudget125639.gui.services.validation.ValidationService;
import it.unicam.cs.mpgc.jbudget125639.modules.GlobalModule;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
    
    private final ValidationService validationService;
    private final ModulesManager modulesManager;
    
    /**
     * Creates and validates a new user.
     * 
     * @param name the user name
     * @return the created and validated user
     * @throws ValidationException if validation fails
     */
    public User createUser(@NonNull String name) throws ValidationException {
        String trimmedName = name.trim();
        User user = validationService.validateAndReturn(new User(trimmedName));
        return user;
    }
    
    /**
     * Adds a user to the global module.
     * 
     * @param user the user to add
     * @throws ValidationException if validation fails
     */
    public void addUserToGlobal(@NonNull User user) throws ValidationException {
        validationService.validateAndReturn(user);
        modulesManager.getModule(GlobalModule.class).getGlobal().addView(user);
    }
    
    /**
     * Creates a new user and adds it to the global module.
     * 
     * @param name the user name
     * @return the created user
     * @throws ValidationException if validation fails
     */
    public User createAndAddUser(@NonNull String name) throws ValidationException {
        User user = createUser(name);
        addUserToGlobal(user);
        return user;
    }
}
