package it.unicam.cs.mpgc.jbudget125639.gui.services.create;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.TransactionTag;
import it.unicam.cs.mpgc.jbudget125639.entities.User;
import it.unicam.cs.mpgc.jbudget125639.filters.TransactionDirection;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.NamedTag;
import it.unicam.cs.mpgc.jbudget125639.filters.tags.PriorityTag;
import it.unicam.cs.mpgc.jbudget125639.gui.services.validation.ValidationException;
import it.unicam.cs.mpgc.jbudget125639.gui.services.validation.ValidationService;
import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class TransactionService {
    
    private final ValidationService validationService;

    /**
     * Creates and validates a new transaction with tags.
     * 
     * @param direction the transaction direction
     * @param amount the monetary amount
     * @param currency the currency
     * @param description the transaction description
     * @param selectedTags the list of selected tags
     * @return the created and validated transaction with tags
     * @throws ValidationException if validation fails
     */
    public Transaction createTransactionWithTags(
            @NonNull TransactionDirection direction,
            double amount,
            @NonNull Currency currency,
            @NonNull String description,
            @NonNull List<NamedTag> selectedTags) throws ValidationException {
        
        MoneyAmount moneyAmount = validationService.validateAndReturn(
                new MoneyAmount(amount, currency)
        );
        
        // First create the transaction without tags
        Transaction transaction = validationService.validateAndReturn(
                new Transaction(direction, moneyAmount, description)
        );
        
        // Then create TransactionTag objects from NamedTags
        Collection<TransactionTag> transactionTags = new ArrayList<>();
        for (NamedTag namedTag : selectedTags) {
            PriorityTag priorityTag = new PriorityTag(namedTag.getName(), namedTag.getPriority());
            TransactionTag transactionTag = new TransactionTag(transaction, priorityTag);
            transactionTags.add(transactionTag);
        }
        
        // Create a new transaction with tags
        Transaction transactionWithTags = validationService.validateAndReturn(
                new Transaction(direction, moneyAmount, description, transactionTags)
        );
        
        return transactionWithTags;
    }
    
    /**
     * Adds a transaction to a user after validation.
     * 
     * @param user the user to add the transaction to
     * @param transaction the transaction to add
     * @throws ValidationException if validation fails
     */
    public void addTransactionToUser(@NonNull User user, @NonNull Transaction transaction) 
            throws ValidationException {
        validationService.validateAndReturn(user);
        validationService.validateAndReturn(transaction);
        user.addTransaction(transaction);
    }
    
    /**
     * Removes a transaction from a user.
     * 
     * @param user the user to remove the transaction from
     * @param transaction the transaction to remove
     */
    public void removeTransactionFromUser(@NonNull User user, @NonNull Transaction transaction) {
        user.removeTransaction(transaction);
    }
}
