package it.unicam.cs.mpgc.jbudget125639.gui.services.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.NonNull;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service responsible for object validation using Bean Validation.
 * Follows Single Responsibility Principle by handling only validation concerns.
 * Uses exceptions as the primary mechanism for reporting validation failures.
 */
public class ValidationService {
    
    private final Validator validator;
    
    public ValidationService() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    
    /**
     * Validates an object and throws exception if validation fails.
     * 
     * @param object the object to validate
     * @param <T> the type of the object
     * @return the validated object
     * @throws ValidationException if validation fails
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
    
    /**
     * Validates an object without returning it.
     * Useful when you only need to check validity.
     * 
     * @param object the object to validate
     * @param <T> the type of the object
     * @throws ValidationException if validation fails
     */
    public <T> void validate(@NonNull T object) throws ValidationException {
        validateAndReturn(object);
    }
    
    /**
     * Checks if an object is valid without throwing exceptions.
     * 
     * @param object the object to validate
     * @param <T> the type of the object
     * @return true if valid, false otherwise
     */
    public <T> boolean isValid(@NonNull T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        return violations.isEmpty();
    }
}
