package it.unicam.cs.mpgc.jbudget125639.filters.dates;

import it.unicam.cs.mpgc.jbudget125639.entities.Transaction;
import it.unicam.cs.mpgc.jbudget125639.filters.IFilter;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Enum che rappresenta diversi intervalli temporali predefiniti.
 * Implementa il filtro {@link IFilter} per filtrare transazioni in base alla data.
 */
@AllArgsConstructor
public enum TimeSpan implements IFilter {
    ONE_DAY(new DefinedTimeSpan(1, TimeUnit.DAYS)),
    ONE_WEEK(new DefinedTimeSpan(7, TimeUnit.DAYS)),
    ONE_MONTH(new DefinedTimeSpan(30, TimeUnit.DAYS)),
    ONE_YEAR(new DefinedTimeSpan(365, TimeUnit.DAYS)),
    EVER(new DefinedTimeSpan(Integer.MAX_VALUE, TimeUnit.DAYS));

    private DateFilter dateFilter;

    /**
     * Verifica se la transazione specificata rientra nell'intervallo temporale di questo filtro.
     *
     * @param transaction la transazione da valutare
     * @return {@code true} se la transazione soddisfa il filtro, altrimenti {@code false}
     */
    @Override
    public boolean pass(Transaction transaction) {
        return dateFilter.pass(transaction);
    }

    public static DateFilter of(int amount, @NonNull TimeUnit timeUnit) {
        return new DefinedTimeSpan(amount, timeUnit);
    }

    protected static class DefinedTimeSpan extends AfterDate {
        public DefinedTimeSpan(int amount, @NonNull TimeUnit timeUnit) {
            super(new DateCalculator(amount, timeUnit, Date.from(Instant.now())).inPast());
        }
    }

}