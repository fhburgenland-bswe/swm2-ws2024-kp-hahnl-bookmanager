package fh.bswe.bookmanager.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
import java.util.Objects;

/**
 * Represents a book entity in the system.
 * <p>
 * This entity stores metadata about a book, including its ISBN, title, authors,
 * publisher, cover information, and language. It is mapped to the database table {@code book}.
 * </p>
 */
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(length = 13, nullable = false)
    private String isbn;

    @Lob
    @Column
    private String title;

    @Lob
    @Column
    private String authors;

    @Column(name = "publish_date", length = 30)
    private String publishDate;

    @Lob
    @Column
    private String publishers;

    @Column(name = "cover_key", length = 30)
    private String coverKey;

    @Lob
    @Column(name = "cover_link")
    private String coverLink;

    @Lob
    @Column(name = "cover_image")
    private byte[] coverImage;

    @Column(length = 10)
    private String language;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBook> userBooks;

    /**
     * Returns the ID of the book (primary key).
     *
     * @return the unique ID of the book
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the book.
     *
     * @param id the unique ID to set
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Returns the ISBN of the book.
     *
     * @return the ISBN string (max. 13 characters)
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn the ISBN to set (expected to be 10 or 13 digits)
     */
    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns the title of the book.
     *
     * @return the full title (can be a large string)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title the title to set
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Gets the list of authors of the book as a comma-separated string.
     *
     * @return the authors
     */
    public String getAuthors() {
        return authors;
    }

    /**
     * Sets the authors of the book.
     *
     * @param authors comma-separated list of authors
     */
    public void setAuthors(final String authors) {
        this.authors = authors;
    }

    /**
     * Gets the date the book was published.
     *
     * @return the publication date
     */
    public String getPublishDate() {
        return publishDate;
    }

    /**
     * Sets the date the book was published.
     *
     * @param publishDate the publication date to set
     */
    public void setPublishDate(final String publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * Gets the publisher(s) of the book.
     *
     * @return the publishers
     */
    public String getPublishers() {
        return publishers;
    }

    /**
     * Sets the publisher(s) of the book.
     *
     * @param publishers the publishers to set
     */
    public void setPublishers(final String publishers) {
        this.publishers = publishers;
    }

    /**
     * Gets the cover key of the book (used to reference cover images externally).
     *
     * @return the cover key
     */
    public String getCoverKey() {
        return coverKey;
    }

    /**
     * Sets the cover key of the book.
     *
     * @param coverKey the cover key to set
     */
    public void setCoverKey(final String coverKey) {
        this.coverKey = coverKey;
    }

    /**
     * Gets the cover image link (e.g., a URL to a cover image).
     *
     * @return the cover link
     */
    public String getCoverLink() {
        return coverLink;
    }

    /**
     * Sets the cover image link.
     *
     * @param coverLink the cover link to set
     */
    public void setCoverLink(final String coverLink) {
        this.coverLink = coverLink;
    }

    /**
     * Returns the binary image data of the book cover.
     * <p>
     * A defensive copy is returned to prevent external modification.
     * </p>
     *
     * @return byte array of the cover image, or an empty array if none
     */
    public byte[] getCoverImage() {
        if (coverImage == null) {
            return new byte[0];
        } else {
            final byte[] tempCoverImage = new byte[coverImage.length];
            System.arraycopy(coverImage, 0, tempCoverImage, 0, coverImage.length);
            return tempCoverImage;
        }
    }

    /**
     * Sets the cover image data.
     * <p>
     * A defensive copy is created to avoid exposing internal state.
     * </p>
     *
     * @param coverImage the cover image byte array
     */
    public void setCoverImage(final byte[] coverImage) {
        if (coverImage == null) {
            this.coverImage = new byte[0];
        } else {
            final byte[] tempCoverImage = new byte[coverImage.length];
            System.arraycopy(coverImage, 0, tempCoverImage, 0, coverImage.length);
            this.coverImage = tempCoverImage;
        }
    }

    /**
     * Gets the language code of the book (e.g., "en", "de").
     *
     * @return the language code
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language code of the book.
     *
     * @param language the language code to set
     */
    public void setLanguage(final String language) {
        this.language = language;
    }

    /**
     * Returns the list of {@link UserBook} entries associated with this book.
     * <p>
     * This represents the many-to-one relationship between users and this book.
     * Each {@link UserBook} instance links a {@link UserAccount} to this book with
     * additional metadata like rating or comment.
     * </p>
     *
     * @return list of user-book relations referencing this book
     */
    public List<UserBook> getUserBooks() {
        return userBooks;
    }

    /**
     * Sets the list of {@link UserBook} entries associated with this book.
     * <p>
     * This replaces the current list of user-book relations for this book.
     * Typically used when managing or updating user associations programmatically.
     * </p>
     *
     * @param userBooks the list of user-book relations to associate with this book
     */
    public void setUserBooks(final List<UserBook> userBooks) {
        this.userBooks = userBooks;
    }

    /**
     * Checks whether this {@code Book} is equal to another object.
     * <p>
     * Two {@code Book} instances are considered equal if both their {@code id}
     * and {@code isbn} fields are equal.
     * </p>
     *
     * @param o the object to compare with
     * @return {@code true} if the objects are considered equal, otherwise {@code false}
     */
    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(isbn, book.isbn);
    }

    /**
     * Computes a hash code based on the {@code id} and {@code isbn} of the book.
     *
     * @return the computed hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, isbn);
    }
}