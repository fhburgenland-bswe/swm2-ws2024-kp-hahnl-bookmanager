package fh.bswe.bookmanager.exception;

/**
 * Thrown to indicate that a cover image for a book could not be found.
 * <p>
 * This exception should be used when a book's cover image is missing or
 * could not be retrieved from an external service or database.
 */
public class CoverNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code CoverNotFoundException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public CoverNotFoundException(final String message) {
        super(message);
    }
}
