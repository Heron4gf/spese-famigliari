package it.unicam.cs.spesefamigliari.views;

import it.unicam.cs.spesefamigliari.entities.Transaction;
import it.unicam.cs.spesefamigliari.filters.IFilter;
import lombok.NonNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public abstract class AbstractView implements View {

    @Override
    public Collection<Transaction> ordered(@NonNull IFilter... filters) {
        return getFiltered(filters).stream()
                .sorted(Comparator.comparing(Transaction::getDate))
                .collect(Collectors.toList());
    }
}
