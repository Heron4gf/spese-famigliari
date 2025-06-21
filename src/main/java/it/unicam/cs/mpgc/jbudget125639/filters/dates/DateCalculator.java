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

    /**
     * Restituisce una data futura ottenuta aggiungendo l'intervallo specificato
     * (espresso tramite {@code amount} e {@code timeUnit}) alla data attuale.
     *
     * @return una {@link Date} nel futuro rispetto alla data di riferimento
     */
    public Date inFuture() {
        return Date.from(instant().plusMillis(millis()));
    }

    /**
     * Restituisce una data passata ottenuta sottraendo l'intervallo specificato
     * (espresso tramite {@code amount} e {@code timeUnit}) alla data attuale.
     *
     * @return una {@link Date} nel passato rispetto alla data di riferimento
     */
    public Date inPast() {
        return Date.from(instant().minusMillis(millis()));
    }
}
