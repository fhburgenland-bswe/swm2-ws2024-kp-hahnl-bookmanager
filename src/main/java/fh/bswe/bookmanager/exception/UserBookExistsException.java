package fh.bswe.bookmanager.exception;

/**
 * Thrown to indicate that a {@code UserBook} entry already exists for a
 * given user and book combination.
 * <p>
 * This exception is typically used to prevent duplicate associations between
 * a {@code UserAccount} and a {@code Book}.
 */
public class UserBookExistsException extends RuntimeException {

  /**
   * Constructs a new {@code UserBookExistsException} with the specified detail message.
   *
   * @param message the detail message explaining the cause of the exception
   */
    public UserBookExistsException(final String message) {
        super(message);
    }
}
