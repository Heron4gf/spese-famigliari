package it.unicam.cs.mpgc.jbudget125639.gui.services.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.NonNull;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servizio responsabile della validazione degli oggetti usando Bean Validation.
 * Segue il Principio di Responsabilità Unica occupandosi solo degli aspetti legati alla validazione.
 * Utilizza le eccezioni come meccanismo principale per segnalare i fallimenti di validazione.
 */
public class ValidationService {
    
    private final Validator validator;
    
    public ValidationService() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    /**
     * Valida un oggetto e lancia un'eccezione se la validazione fallisce.
     *
     * @param object l’oggetto da validare
     * @param <T> il tipo dell’oggetto
     * @return l’oggetto validato
     * @throws ValidationException se la validazione fallisce
     */
    public <T> T validateAndReturn(@NonNull T object) throws ValidationException {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("\n"));
            throw new ValidationException(errorMessage);
        }
        
        return object;
    }
}
