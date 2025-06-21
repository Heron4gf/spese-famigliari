package it.unicam.cs.mpgc.jbudget125639.gui.services.validation;

import lombok.experimental.StandardException;

/**
 * Eccezione generata quando la validazione fallisce.
 * Fornisce una chiara separazione tra gli errori di validazione e le altre eccezioni di runtime.
 */
@StandardException
public class ValidationException extends Exception {
}
