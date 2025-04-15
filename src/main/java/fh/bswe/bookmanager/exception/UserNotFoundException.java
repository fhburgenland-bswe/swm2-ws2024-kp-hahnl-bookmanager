package fh.bswe.bookmanager.exception;

/**
 * Exception thrown when attempting to find a user but was not found.
 */
public class UserNotFoundException extends Exception {
    /**
     * Constructs a new {@code UserExistsException} with a default message indicating
     * that the user was not found.
     */
    public UserNotFoundException() {
        super("User was not found");
    }
}
