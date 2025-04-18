package fh.bswe.bookmanager.dto;

/**
 * Data Transfer Object (DTO) that represents a user's interaction with a book.
 * <p>
 * Contains information about the book such as ISBN, title, and author,
 * as well as user-specific metadata like rating and comment.
 * This DTO is typically used to display or transfer user-book relationships
 * without exposing internal entity structures.
 */
public class UserBookDto {
    private String isbn;
    private String title;
    private String author;
    private Integer rating;
    private String comment;

    /**
     * Returns the ISBN of the book.
     *
     * @return the book's ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn the book's ISBN to set
     */
    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns the title of the book.
     *
     * @return the book's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title the book's title to set
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Returns the author(s) of the book.
     *
     * @return the book's author(s)
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author(s) of the book.
     *
     * @param author the book's author(s) to set
     */
    public void setAuthor(final String author) {
        this.author = author;
    }

    /**
     * Returns the user's rating for the book.
     *
     * @return the rating value (e.g., from 1 to 5)
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * Sets the user's rating for the book.
     *
     * @param rating the rating value to set (e.g., from 1 to 5)
     */
    public void setRating(final Integer rating) {
        this.rating = rating;
    }

    /**
     * Returns the user's comment about the book.
     *
     * @return the user's comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the user's comment about the book.
     *
     * @param comment the user's comment to set
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }
}
