package fh.bswe.bookmanager.exception;

/**
 * Exception thrown when attempting to add a book that already exists in the system.
 * <p>
 * This exception is typically used to indicate a constraint violation on
 * unique book data, such as ISBN.
 */
public class BookExistsException extends RuntimeException {

    /**
     * Constructs a new {@code BookExistsException} with a default message indicating
     * that the book already exists.
     */
    public BookExistsException() {
        super("Book already exists");
    }
}
