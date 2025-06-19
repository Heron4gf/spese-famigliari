import it.unicam.cs.mpgc.jbudget125639.money.Currency;
import it.unicam.cs.mpgc.jbudget125639.money.MoneyAmount;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolationException; // Importa questa eccezione
import org.junit.jupiter.api.BeforeAll; // Per inizializzare il validatore una volta
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow; // Utile per i casi di successo
import static org.junit.jupiter.api.Assertions.assertTrue; // Per verificare l'assenza di violazioni

public class ValidationTest {

    // Il validatore di Jakarta Validation
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        // Inizializza il validatore una sola volta prima che vengano eseguiti tutti i test
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void moneyValidationFailsForInvalidValues() {
        // Caso 1: Valore negativo (-5d) - Deve fallire per @Positive
        assertThrows(ConstraintViolationException.class, () -> {
            MoneyAmount amount = new MoneyAmount(-5d, Currency.EUR);
            // Esegui la validazione e raccogli le violazioni
            Set<ConstraintViolation<MoneyAmount>> violations = validator.validate(amount);
            if (!violations.isEmpty()) {
                // Se ci sono violazioni, lancia una ConstraintViolationException
                throw new ConstraintViolationException(violations);
            }
        }, "Dovrebbe lanciare ConstraintViolationException per valore negativo.");

        // Caso 2: Troppe cifre decimali (3.001) - Deve fallire per @Digits
        assertThrows(ConstraintViolationException.class, () -> {
            MoneyAmount amount = new MoneyAmount(3.001, Currency.USD);
            Set<ConstraintViolation<MoneyAmount>> violations = validator.validate(amount);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }, "Dovrebbe lanciare ConstraintViolationException per troppe cifre decimali.");

        // Caso 3: Valuta null (gestito da Lombok @NonNull sul costruttore)
        // Lombok @NonNull genera una NullPointerException direttamente al momento della costruzione
        assertThrows(NullPointerException.class, () -> new MoneyAmount(2.0, null),
                "Dovrebbe lanciare NullPointerException per valuta null.");

        // Caso 4: Valore negativo (-19d) - Deve fallire per @Positive
        assertThrows(ConstraintViolationException.class, () -> {
            MoneyAmount amount = new MoneyAmount(-19d, Currency.USD);
            Set<ConstraintViolation<MoneyAmount>> violations = validator.validate(amount);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }, "Dovrebbe lanciare ConstraintViolationException per un altro valore negativo.");
    }

    @Test
    public void moneyValidationSucceedsForValidValues() {
        // Caso 5: Valori validi - non dovrebbero generare eccezioni di validazione
        assertDoesNotThrow(() -> {
            MoneyAmount amount = new MoneyAmount(10.50, Currency.EUR);
            Set<ConstraintViolation<MoneyAmount>> violations = validator.validate(amount);
            // Assicurati che non ci siano violazioni per i valori validi
            assertTrue(violations.isEmpty(), "Non dovrebbero esserci violazioni per valori validi.");
        }, "Non dovrebbe lanciare eccezioni per un valore valido (10.50).");

        assertDoesNotThrow(() -> {
            MoneyAmount amount = new MoneyAmount(0.01, Currency.USD);
            Set<ConstraintViolation<MoneyAmount>> violations = validator.validate(amount);
            assertTrue(violations.isEmpty(), "Non dovrebbero esserci violazioni per valori validi.");
        }, "Non dovrebbe lanciare eccezioni per un valore valido (0.01).");
    }
}