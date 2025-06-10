package it.unicam.cs.mpgc.jbudget125639.filters.dates;

import java.util.Date;

public class AfterDate extends DateFilter {
    public AfterDate(Date date) {
        super(date);
    }

    @Override
    protected boolean shouldPass(Date date) {
        return date.after(this.date);
    }
}
