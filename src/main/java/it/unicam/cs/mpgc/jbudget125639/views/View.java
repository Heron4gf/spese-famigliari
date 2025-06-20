package it.unicam.cs.mpgc.jbudget125639.views;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import lombok.NonNull;

import java.util.Collection;

public interface View {

    double total(IFilter... filters);

    String getName();

    Collection<Transaction> ordered(@NonNull IFilter... filters);

    Collection<Transaction> getFiltered(@NonNull IFilter... filters);

    void removeTransaction(@NonNull Transaction transaction);
}
