package it.unicam.cs.mpgc.jbudget125639.filters.dates;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
public class DateCalculator {

    private final int amount;
    private final TimeUnit timeUnit;
    private Date date;

    private Instant instant() {
        return date.toInstant();
    }

    private long millis() {
        return timeUnit.toMillis(amount);
    }

    public Date inFuture() {
        return Date.from(instant().plusMillis(millis()));
    }

    public Date inPast() {
        return Date.from(instant().minusMillis(millis()));
    }
}
