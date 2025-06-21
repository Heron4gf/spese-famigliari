package it.unicam.cs.mpgc.jbudget125639.gui.services;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.gui.services.validation.ValidationException;
import it.unicam.cs.mpgc.jbudget125639.gui.services.validation.ValidationService;
import it.unicam.cs.mpgc.jbudget125639.modules.GlobalModule;
import it.unicam.cs.mpgc.jbudget125639.modules.abstracts.ModulesManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionService {
    
    private final ValidationService validationService;
    private final ModulesManager modulesManager;

    /**
     * Aggiunge la transazione all'utente a seguito della validazione
     * 
     * @param user utente
     * @param transaction la transazione da aggiungere
     * @throws ValidationException se invalida (ad esempio trans. con soldi negativi)
     */
    public void addTransactionToUser(@NonNull User user, @NonNull Transaction transaction) 
            throws ValidationException {

        validationService.validateAndReturn(user);
        validationService.validateAndReturn(transaction);

        user.addTransaction(transaction);
        try {
            GlobalModule globalModule = modulesManager.getModule(GlobalModule.class);
            globalModule.saveState();
        } catch (Exception e) {
            throw new ValidationException("Errore durante il salvataggio nel database: " + e.getMessage());
        }
    }

}
