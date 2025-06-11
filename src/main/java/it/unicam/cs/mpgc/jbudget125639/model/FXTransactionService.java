package it.unicam.cs.mpgc.jbudget125639.model;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;

import java.util.Collection;

public class FXTransactionService implements TransactionService {

    private Collection<User> allUsers;

    @Override
    public void setAllUsers(Collection<User> users) {
        this.allUsers = users;
    }

    @Override
    public Collection<User> getAllUsers() {
        return allUsers;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        if (allUsers == null || transaction.getUser() == null) {
            return;
        }

        allUsers.stream()
                .filter(user -> user.getName().equals(transaction.getUser().getName()))
                .findFirst()
                .ifPresent(user -> user.addTransaction(transaction));
    }
}