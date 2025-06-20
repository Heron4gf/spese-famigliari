package it.unicam.cs.mpgc.jbudget125639.gui.services.validation;

import lombok.experimental.StandardException;

/**
 * Exception thrown when validation fails.
 * Provides a clear separation between validation errors and other runtime exceptions.
 */
@StandardException
public class ValidationException extends Exception {
}
