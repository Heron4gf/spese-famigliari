package it.unicam.cs.spesefamigliari.filters;

import it.unicam.cs.spesefamigliari.entities.Transaction;

public enum TransactionDirection implements IFilter {
    IN, OUT;

    @Override
    public boolean pass(Transaction transaction) {
        return transaction.getDirection() == this;
    }
}
