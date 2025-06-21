package it.unicam.cs.mpgc.jbudget125639.filters;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;

/*
 *  Direzione della transazione
 */
public enum TransactionDirection implements IFilter {
    IN, OUT;

    @Override
    public boolean pass(Transaction transaction) {
        return transaction.getDirection() == this;
    }
}
