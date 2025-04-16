package fh.bswe.bookmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents the relationship between a {@link UserAccount} and a {@link Book}.
 * <p>
 * This entity models a user's interaction with a book, including user-specific metadata
 * such as rating and comment. It is mapped to the {@code user_book} table in the database.
 * </p>
 */
@Entity
@Table(name = "user_book")
public class UserBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column
    private Integer rating;

    @Lob
    @Column
    private String comment;

    /**
     * Returns the unique ID of the user-book entry.
     *
     * @return the entry ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the user-book entry.
     *
     * @param id the entry ID to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Returns the user account associated with this entry.
     *
     * @return the {@link UserAccount} instance
     */
    public UserAccount getUserAccount() {
        return userAccount;
    }

    /**
     * Sets the user account for this entry.
     *
     * @param userAccount the {@link UserAccount} to associate
     */
    public void setUser(final UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    /**
     * Returns the book associated with this entry.
     *
     * @return the {@link Book} instance
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the book associated with this entry.
     *
     * @param book the {@link Book} to associate
     */
    public void setBook(final Book book) {
        this.book = book;
    }

    /**
     * Returns the user's rating for the book.
     *
     * @return the rating value
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * Sets the user's rating for the book.
     *
     * @param rating the rating value to set
     */
    public void setRating(final Integer rating) {
        this.rating = rating;
    }

    /**
     * Returns the user's comment about the book.
     *
     * @return the comment text
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the user's comment about the book.
     *
     * @param comment the comment text to set
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }
}