package fh.bswe.bookmanager.exception;

/**
 * Exception thrown when validation of a user input fails outside of the standard
 * Bean Validation framework.
 */
public class ValidationException extends RuntimeException {
    /**
     * Constructs a new {@code ValidationException} with the specified detail message.
     *
     * @param message the detail message describing the validation error
     */
    public ValidationException(final String message) {
        super(message);
    }
}
