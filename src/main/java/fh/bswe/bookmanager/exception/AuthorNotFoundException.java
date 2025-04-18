package fh.bswe.bookmanager.exception;

/**
 * Exception thrown when an author cannot be found in the system.
 * <p>
 * Typically used in service or controller layers when a lookup by author ID or name fails.
 */
public class AuthorNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code AuthorNotFoundException} with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public AuthorNotFoundException(final String message) {
        super(message);
    }
}
