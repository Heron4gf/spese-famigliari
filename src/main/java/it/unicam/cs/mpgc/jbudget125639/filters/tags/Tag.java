package it.unicam.cs.mpgc.jbudget125639.filters.tags;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;

public interface Tag extends IFilter {

    Integer getPriority();
    String getName();

    boolean hasMoreOrSamePriority(Transaction transaction);

}
