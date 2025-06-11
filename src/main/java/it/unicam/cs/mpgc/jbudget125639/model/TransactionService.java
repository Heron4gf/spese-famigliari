package it.unicam.cs.mpgc.jbudget125639.model;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.entities.User;

import java.util.Collection;

public interface TransactionService {
    void addTransaction(Transaction transaction);
    Collection<User> getAllUsers();
    void setAllUsers(Collection<User> users);
}