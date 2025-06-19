package it.unicam.cs.mpgc.jbudget125639.money;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class MoneyAmount implements Serializable {

    @NonNull
    @Positive
    @Digits(integer = 20, fraction = 2)
    private final Double value;

    @NonNull
    private final ICurrency currency;

    @Override
    public String toString() {
        return currency.format(value);
    }
}
