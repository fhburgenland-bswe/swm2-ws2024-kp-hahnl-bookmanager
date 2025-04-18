package fh.bswe.bookmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


/**
 * Configuration class that maps OpenLibrary-related properties from the application configuration.
 * <ul>
 *     <li>{@code openlibrary.book_url} – the base URL for retrieving book metadata</li>
 *     <li>{@code openlibrary.cover_url} – the base URL for retrieving book cover images</li>
 * </ul>
 */
@ConfigurationProperties("openlibrary")
@ConfigurationPropertiesScan
public class OpenLibraryConfig {
    private String bookUrl;
    private String coverUrl;
    private String authorUrl;

    /**
     * Returns the base URL used to retrieve book metadata from OpenLibrary.
     *
     * @return the book metadata base URL
     */
    public String getBookUrl() {
        return bookUrl;
    }

    /**
     * Sets the base URL used to retrieve book metadata from OpenLibrary.
     *
     * @param bookUrl the book metadata base URL to set
     */
    public void setBookUrl(final String bookUrl) {
        this.bookUrl = bookUrl;
    }

    /**
     * Returns the base URL used to retrieve book cover images from OpenLibrary.
     *
     * @return the cover image base URL
     */
    public String getCoverUrl() {
        return coverUrl;
    }

    /**
     * Sets the base URL used to retrieve book cover images from OpenLibrary.
     *
     * @param coverUrl the cover image base URL to set
     */
    public void setCoverUrl(final String coverUrl) {
        this.coverUrl = coverUrl;
    }

    /**
     * Returns the base URL used to retrieve author data from OpenLibrary.
     *
     * @return the author data base URL
     */
    public String getAuthorUrl() {
        return authorUrl;
    }

    /**
     * Sets the base URL used to retrieve author data from OpenLibrary.
     *
     * @param authorUrl the author data base URL to set
     */
    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }
}
