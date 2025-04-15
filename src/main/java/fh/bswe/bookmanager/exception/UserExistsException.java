package fh.bswe.bookmanager.exception;

/**
 * Exception thrown when attempting to create a user account with a username that already exists.
 */
public class UserExistsException extends Exception {
    /**
     * Constructs a new {@code UserExistsException} with a default message indicating
     * that the user already exists.
     */
    public UserExistsException() {
        super("User already exists");
    }
}
