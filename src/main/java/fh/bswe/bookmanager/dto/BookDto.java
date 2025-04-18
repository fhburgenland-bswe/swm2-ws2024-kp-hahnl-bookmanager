package fh.bswe.bookmanager.dto;

/**
 * Data Transfer Object (DTO) representing the metadata of a book.
 * This class encapsulates information such as ISBN, title, authors,
 * publication details, cover image data, and language.
 */
public class BookDto {
    private Integer id;
    private String isbn;
    private String title;
    private String authors;
    private String publishDate;
    private String publishers;
    private String coverKey;
    private String coverLink;
    private byte[] coverImage;
    private String language;

    /**
     * Returns the internal database ID of the book.
     *
     * @return the book ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the internal database ID of the book.
     *
     * @param id the book ID
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Returns the ISBN of the book.
     *
     * @return the ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn the ISBN
     */
    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns the title of the book.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title the title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Returns the authors of the book.
     *
     * @return the authors
     */
    public String getAuthors() {
        return authors;
    }

    /**
     * Sets the authors of the book.
     *
     * @param authors the authors
     */
    public void setAuthors(final String authors) {
        this.authors = authors;
    }

    /**
     * Returns the publish date of the book.
     *
     * @return the publish date
     */
    public String getPublishDate() {
        return publishDate;
    }

    /**
     * Sets the publish date of the book.
     *
     * @param publishDate the publish date
     */
    public void setPublishDate(final String publishDate) {
        this.publishDate = publishDate;
    }

    /**
     * Returns the publishers of the book.
     *
     * @return the publishers
     */
    public String getPublishers() {
        return publishers;
    }

    /**
     * Sets the publishers of the book.
     *
     * @param publishers the publishers
     */
    public void setPublishers(final String publishers) {
        this.publishers = publishers;
    }

    /**
     * Returns the cover key used by OpenLibrary to locate cover images.
     *
     * @return the cover key
     */
    public String getCoverKey() {
        return coverKey;
    }

    /**
     * Sets the cover key used by OpenLibrary to locate cover images.
     *
     * @param coverKey the cover key
     */
    public void setCoverKey(final String coverKey) {
        this.coverKey = coverKey;
    }

    /**
     * Returns the URL or internal reference to the cover image.
     *
     * @return the cover link
     */
    public String getCoverLink() {
        return coverLink;
    }

    /**
     * Sets the URL or internal reference to the cover image.
     *
     * @param coverLink the cover link
     */
    public void setCoverLink(final String coverLink) {
        this.coverLink = coverLink;
    }

    /**
     * Returns a defensive copy of the cover image byte array.
     *
     * @return the cover image data, or an empty array if none
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
     * Sets the cover image as a byte array.
     * A defensive copy is created to avoid external modifications.
     *
     * @param coverImage the cover image data to set
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
     * Returns the language of the book.
     *
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language of the book.
     *
     * @param language the language
     */
    public void setLanguage(final String language) {
        this.language = language;
    }
}
