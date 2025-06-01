package it.unicam.cs.spesefamigliari.filters;

import it.unicam.cs.spesefamigliari.entities.Transaction;

public interface IFilter {

    boolean pass(Transaction transaction);

}
