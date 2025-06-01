package it.unicam.cs.spesefamigliari.views;

import it.unicam.cs.spesefamigliari.entities.Transaction;
import it.unicam.cs.spesefamigliari.filters.IFilter;
import lombok.NonNull;

import java.util.Collection;

public interface View {

    double total(IFilter... filters);

    String getName();

    Collection<Transaction> ordered(@NonNull IFilter... filters);

    Collection<Transaction> getFiltered(@NonNull IFilter... filters);

}
