package fh.bswe.bookmanager.exception;

/**
 * Thrown to indicate that a connection to a website failed.
 * <p>
 * This exception should be used when there is a problem establishing
 * or maintaining a connection to an external web resource.
 */
public class ConnectionErrorException extends RuntimeException {
    /**
     * Constructs a new {@code ConnectionErrorException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ConnectionErrorException(final String message) {
        super(message);
    }
}
