package it.unicam.cs.mpgc.jbudget125639.filters;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class EmptyFilter implements IFilter {

    private String name;

    @Override
    public boolean pass(Transaction transaction) {
        return true;
    }
}
