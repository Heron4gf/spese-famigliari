package it.unicam.cs.spesefamigliari.filters;

import it.unicam.cs.spesefamigliari.entities.Transaction;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Data
@RequiredArgsConstructor
public class TimeSpan implements IFilter, Enumeration<TimeSpan> {

    public static final TimeSpan ONE_DAY = new TimeSpan(1, TimeUnit.DAYS);
    public static final TimeSpan ONE_WEEK = new TimeSpan(7, TimeUnit.DAYS);
    public static final TimeSpan ONE_MONTH = new TimeSpan(30, TimeUnit.DAYS);
    public static final TimeSpan ONE_YEAR = new TimeSpan(365, TimeUnit.DAYS);
    public static final TimeSpan EVER = new TimeSpan(Integer.MAX_VALUE, TimeUnit.DAYS);

    private static final List<TimeSpan> VALUES = List.of(ONE_DAY, ONE_WEEK, ONE_MONTH, ONE_YEAR);

    @NonNull private Integer amount;
    @NonNull private TimeUnit timeUnit;

    private int index = 0;

    @Override
    public boolean pass(Transaction transaction) {
        Instant threshold = Instant.now().minus(Duration.ofMillis(timeUnit.toMillis(amount)));
        return transaction.getDate().toInstant().isAfter(threshold);
    }

    @Override
    public String toString() {
        if(this.equals(EVER)) return "ever";
        return String.format("%d %s", amount, timeUnit.name().toLowerCase().replaceAll("s$", ""));
    }

    @Override
    public boolean hasMoreElements() {
        return index < VALUES.size();
    }

    @Override
    public TimeSpan nextElement() {
        return IntStream.range(0, VALUES.size())
                .filter(i -> i == index++)
                .mapToObj(VALUES::get)
                .findFirst()
                .orElseGet(() -> {
                    index = 0;
                    return VALUES.get(index++);
                });
    }
}