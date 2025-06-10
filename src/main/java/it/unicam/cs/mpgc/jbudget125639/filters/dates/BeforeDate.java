package it.unicam.cs.mpgc.jbudget125639.filters.dates;

import java.util.Date;

public class BeforeDate extends DateFilter {
    public BeforeDate(Date date) {
        super(date);
    }

    @Override
    protected boolean shouldPass(Date date) {
        return date.before(this.date);
    }
}
