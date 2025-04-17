package fh.bswe.bookmanager.exception;

/**
 * Thrown to indicate that a book could not be found.
 * <p>
 * This exception should be used when a requested book is not present
 * in the system or external source (e.g., a database or web service).
 */
public class BookNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code BookNotFoundException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public BookNotFoundException(final String message) {
        super(message);
    }
}
