package it.unicam.cs.mpgc.jbudget125639.filters;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;

public interface IFilter {

    boolean pass(Transaction transaction);

}
