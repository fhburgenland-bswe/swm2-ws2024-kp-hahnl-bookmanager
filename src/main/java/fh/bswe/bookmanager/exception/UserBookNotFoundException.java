package fh.bswe.bookmanager.exception;

/**
 * Thrown to indicate that a {@code UserBook} entry doesn't exist for a
 * given user and book combination.
 */
public class UserBookNotFoundException extends RuntimeException {

  /**
   * Constructs a new {@code UserBookExistsException} with the specified detail message.
   *
   * @param message the detail message explaining the cause of the exception
   */
    public UserBookNotFoundException(final String message) {
        super(message);
    }
}
