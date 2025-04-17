package fh.bswe.bookmanager.exception;

/**
 * Thrown to indicate that an error occurred during a web request.
 * <p>
 * This exception should be used when an HTTP request to an external
 * service fails or returns an unexpected response.
 */
public class WebRequestErrorException extends RuntimeException {
    /**
     * Constructs a new {@code WebRequestErrorException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public WebRequestErrorException(final String message) {
        super(message);
    }
}
