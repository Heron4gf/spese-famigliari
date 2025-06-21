package it.unicam.cs.mpgc.jbudget125639.money;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class MoneyAmount implements Serializable {

    @NonNull
    @Positive(message = "Il valore deve essere positivo.") // controlli sull'input con jakarta validation e hibernate
    @Digits(integer = 20, fraction = 2, message = "Il numero deve avere al massimo 20 cifre intere e 2 decimali.")
    private final Double value;

    @NonNull
    private final ICurrency currency;

    @Override
    public String toString() {
        return currency.format(value);
    }
}
