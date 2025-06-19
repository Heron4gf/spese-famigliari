package it.unicam.cs.mpgc.jbudget125639.filters.dates;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class DateFilter implements IFilter {

    protected Date date;

    protected abstract boolean shouldPass(Date date);

    @Override
    public boolean pass(Transaction transaction) {
        return shouldPass(transaction.getDate());
    }
}
