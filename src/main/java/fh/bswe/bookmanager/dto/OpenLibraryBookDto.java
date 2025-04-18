package fh.bswe.bookmanager.dto;

import java.util.List;
import java.util.Map;

/**
 * Represents a book retrieved from the OpenLibrary API.
 * <p>
 * This class contains metadata about a book, such as title, identifiers,
 * publishers, cover image references, and revision history.
 */
@SuppressWarnings({
        "PMD.TooManyFields",
        "PMD.LongVariable",
        "PMD.UnusedPrivateField",
        "PMD.FieldNamingConventions",
        "PMD.MethodNamingConventions",
        "PMD.FormalParameterNamingConventions",
        "PMD.ExcessivePublicCount"
})
public class OpenLibraryBookDto {
    private String title;
    private List<Author> authors;
    private String publish_date;
    private Type type;
    private List<Work> works;
    private List<String> publishers;
    private String physical_format;
    private String full_title;
    private List<Integer> covers;
    private String key;
    private Integer number_of_pages;
    private Map<String, Object> identifiers;
    private String edition_name;
    private List<Contributor> contributors;
    private List<Language> languages;
    private String description;
    private String ocaid;
    private List<String> isbn_10;
    private List<String> isbn_13;
    private Map<String, Object> classifications;
    private int latest_revision;
    private int revision;
    private Created created;
    private LastModified last_modified;
    private String subtitle;
    private List<String> subjects;

    /**
     * Represents an author of the book.
     * <p>
     * Contains a reference key to the author entity in the OpenLibrary system.
     */
    public static class Author {
        private String key;

        /**
         * Returns the OpenLibrary key for the author.
         *
         * @return the author key
         */
        public String getKey() {
            return key;
        }

        /**
         * Sets the OpenLibrary key for the author.
         *
         * @param key the author key to set
         */
        public void setKey(final String key) {
            this.key = key;
        }
    }

    /**
     * Represents a contributor to the book (e.g. editor, illustrator).
     */
    public static class Contributor {
        private String role;
        private String name;

        /**
         * Returns the role of the contributor (e.g., "Editor", "Illustrator").
         *
         * @return the contributor's role
         */
        public String getRole() {
            return role;

        }

        /**
         * Sets the role of the contributor.
         *
         * @param role the contributor's role to set
         */
        public void setRole(final String role) {
            this.role = role;
        }

        /**
         * Returns the name of the contributor.
         *
         * @return the contributor's name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the name of the contributor.
         *
         * @param name the contributor's name to set
         */
        public void setName(final String name) {
            this.name = name;
        }
    }

    /**
     * Returns the list of authors associated with the book.
     *
     * @return the list of authors
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * Sets the list of authors associated with the book.
     *
     * @param authors the list of authors to set
     */
    public void setAuthors(final List<Author> authors) {
        this.authors = authors;
    }

    /**
     * Returns the physical format of the book (e.g., "Paperback", "Hardcover").
     *
     * @return the physical format
     */
    public String getPhysical_format() {
        return physical_format;
    }

    /**
     * Sets the physical format of the book.
     *
     * @param physical_format the format to set
     */
    public void setPhysical_format(final String physical_format) {
        this.physical_format = physical_format;
    }

    /**
     * Returns the full title of the book, including any subtitles or extra formatting.
     *
     * @return the full title
     */
    public String getFull_title() {
        return full_title;
    }

    /**
     * Sets the full title of the book.
     *
     * @param full_title the full title to set
     */
    public void setFull_title(final String full_title) {
        this.full_title = full_title;
    }

    /**
     * Returns the total number of pages in the book.
     *
     * @return the number of pages
     */
    public Integer getNumber_of_pages() {
        return number_of_pages;
    }

    /**
     * Sets the total number of pages in the book.
     *
     * @param number_of_pages the number of pages to set
     */
    public void setNumber_of_pages(final Integer number_of_pages) {
        this.number_of_pages = number_of_pages;
    }

    /**
     * Returns the name of the edition of the book.
     *
     * @return the edition name
     */
    public String getEdition_name() {
        return edition_name;
    }

    /**
     * Sets the name of the edition of the book.
     *
     * @param edition_name the edition name to set
     */
    public void setEdition_name(final String edition_name) {
        this.edition_name = edition_name;
    }

    /**
     * Returns the list of contributors to the book.
     *
     * @return the list of contributors
     */
    public List<Contributor> getContributors() {
        return contributors;
    }

    /**
     * Sets the list of contributors to the book.
     *
     * @param contributors the list of contributors to set
     */
    public void setContributors(final List<Contributor> contributors) {
        this.contributors = contributors;
    }

    /**
     * Returns the book description or summary.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the book description or summary.
     *
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns the subtitle of the book.
     *
     * @return the subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Sets the subtitle of the book.
     *
     * @param subtitle the subtitle to set
     */
    public void setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * Returns the list of subjects or genres associated with the book.
     *
     * @return the list of subjects
     */
    public List<String> getSubjects() {
        return subjects;
    }

    /**
     * Sets the list of subjects or genres for the book.
     *
     * @param subjects the list of subjects to set
     */
    public void setSubjects(final List<String> subjects) {
        this.subjects = subjects;
    }

    /**
     * A reference to related works associated with the book.
     */
    public static class Work {
        private String key;

        /**
         * Returns the key identifying the work.
         *
         * @return the work key
         */
        public String getKey() {
            return key;
        }

        /**
         * Sets the key identifying the work.
         *
         * @param key the work key to set
         */
        public void setKey(final String key) {
            this.key = key;
        }
    }

    /**
     * Represents the type of the book entity (e.g., edition, work).
     */
    public static class Type {
        private String key;

        /**
         * Returns the type key.
         *
         * @return the type key
         */
        public String getKey() {
            return key;
        }

        /**
         * Sets the type key.
         *
         * @param key the type key to set
         */
        public void setKey(final String key) {
            this.key = key;
        }
    }

    /**
     * Represents a language in which the book is available.
     */
    public static class Language {
        private String key;

        /**
         * Returns the language key.
         *
         * @return the language key
         */
        public String getKey() {
            return key;
        }

        /**
         * Sets the language key.
         *
         * @param key the language key to set
         */
        public void setKey(final String key) {
            this.key = key;
        }
    }

    /**
     * Metadata about the creation time of the book entry.
     */
    public static class Created {
        private String type;
        private String value;

        /**
         * Returns the type of the created field.
         *
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the type of the created field.
         *
         * @param type the type to set
         */
        public void setType(final String type) {
            this.type = type;
        }

        /**
         * Returns the value of the created field (typically a timestamp).
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the created field.
         *
         * @param value the value to set
         */
        public void setValue(final String value) {
            this.value = value;
        }
    }

    /**
     * Metadata about the last modification time of the book entry.
     */
    public static class LastModified {
        private String type;
        private String value;

        /**
         * Returns the type of the last_modified field.
         *
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the type of the last_modified field.
         *
         * @param type the type to set
         */
        public void setType(final String type) {
            this.type = type;
        }

        /**
         * Returns the value of the last_modified field (typically a timestamp).
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the last_modified field.
         *
         * @param value the value to set
         */
        public void setValue(final String value) {
            this.value = value;
        }
    }

    /**
     * Returns the list of works associated with this book.
     *
     * @return the list of works
     */
    public List<Work> getWorks() {
        return works;
    }

    /**
     * Set the list of works associated with this book.
     * @param works     list of works
     */
    public void setWorks(final List<Work> works) {
        this.works = works;
    }

    /**
     * Returns the book title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the book.
     * @param title     title of the book.
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Returns the list of publishers.
     *
     * @return the list of publisher names
     */
    public List<String> getPublishers() {
        return publishers;
    }

    /**
     * Set the list of publishers.
     * @param publishers        list of publishers
     */
    public void setPublishers(final List<String> publishers) {
        this.publishers = publishers;
    }

    /**
     * Returns the book's publication date as a string.
     *
     * @return the publication date
     */
    public String getPublish_date() {
        return publish_date;
    }

    /**
     * Set the book's publication date as string
     * @param publish_date  the publication date
     */
    public void setPublish_date(final String publish_date) {
        this.publish_date = publish_date;
    }

    /**
     * Returns the unique key for this book entry.
     *
     * @return the book key
     */
    public String getKey() {
        return key;
    }

    /**
     * Set the unique key for this book entry.
     * @param key       the key for this book entry
     */
    public void setKey(final String key) {
        this.key = key;
    }

    /**
     * Returns the type of this book entry.
     *
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the type of this book entry.
     * @param type      the type of this book entry
     */
    public void setType(final Type type) {
        this.type = type;
    }

    /**
     * Returns a map of identifiers such as ISBNs or OpenLibrary IDs.
     *
     * @return the map of identifiers
     */
    public Map<String, Object> getIdentifiers() {
        return identifiers;
    }

    /**
     * Set a map of identifiers such as ISBNs or OpenLibrary IDs.
     * @param identifiers       map of identifiers such as ISBNs or OpenLibrary IDs
     */
    public void setIdentifiers(final Map<String, Object> identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * Returns a list of cover image IDs.
     *
     * @return the list of cover image identifiers
     */
    public List<Integer> getCovers() {
        return covers;
    }

    /**
     * Set a list of cover images IDs.
     * @param covers    list of cover image identifiers
     */
    public void setCovers(final List<Integer> covers) {
        this.covers = covers;
    }

    /**
     * Returns the Open Content Alliance identifier (if available).
     *
     * @return the ocaid
     */
    public String getOcaid() {
        return ocaid;
    }

    /**
     * Set the Open Content Alliance identifier
     * @param ocaid     the Open Content Alliance identifier
     */
    public void setOcaid(final String ocaid) {
        this.ocaid = ocaid;
    }

    /**
     * Returns the list of ISBN-13 numbers.
     *
     * @return the list of ISBN-13 codes
     */
    public List<String> getIsbn_13() {
        return isbn_13;
    }

    /**
     * Set the list of ISBN-13 numbers.
     * @param isbn_13   the list of ISBN-13 codes
     */
    public void setIsbn_13(final List<String> isbn_13) {
        this.isbn_13 = isbn_13;
    }

    /**
     * Returns the list of ISBN-10 numbers.
     *
     * @return the list of ISBN-10 codes
     */
    public List<String> getIsbn_10() {
        return isbn_10;
    }

    /**
     * Set the list of ISBN-10 numbers.
     * @param isbn_10   the list of ISBN-10 codes
     */
    public void setIsbn_10(final List<String> isbn_10) {
        this.isbn_10 = isbn_10;
    }

    /**
     * Returns the classification data of the book.
     *
     * @return a map of classification entries
     */
    public Map<String, Object> getClassifications() {
        return classifications;
    }

    /**
     * Set the classification data of the book
     * @param classifications   a map of classification entries
     */
    public void setClassifications(final Map<String, Object> classifications) {
        this.classifications = classifications;
    }

    /**
     * Returns the list of languages the book is available in.
     *
     * @return the list of languages
     */
    public List<Language> getLanguages() {
        return languages;
    }

    /**
     * Set the lsit of languages the book is available in.
     * @param languages     the list of languages
     */
    public void setLanguages(final List<Language> languages) {
        this.languages = languages;
    }

    /**
     * Returns the latest revision number of this book entry.
     *
     * @return the latest revision number
     */
    public int getLatest_revision() {
        return latest_revision;
    }

    /**
     * Set the latest revision number of this book entry.
     * @param latest_revision   the latest revision number
     */
    public void setLatest_revision(final int latest_revision) {
        this.latest_revision = latest_revision;
    }

    /**
     * Returns the current revision number of this book entry.
     *
     * @return the revision number
     */
    public int getRevision() {
        return revision;
    }

    /**
     * Set the current revision number of this book entry.
     * @param revision      the revision number
     */
    public void setRevision(final int revision) {
        this.revision = revision;
    }

    /**
     * Returns metadata about when this entry was created.
     *
     * @return the created metadata
     */
    public Created getCreated() {
        return created;
    }

    /**
     * Set metadata about when this entry was created.
     * @param created   the created metadata
     */
    public void setCreated(final Created created) {
        this.created = created;
    }

    /**
     * Returns metadata about when this entry was last modified.
     *
     * @return the last modified metadata
     */
    public LastModified getLast_modified() {
        return last_modified;
    }

    /**
     * Set metadata about when this entry was last modified.
     * @param last_modified     the last modified metadata
     */
    public void setLast_modified(final LastModified last_modified) {
        this.last_modified = last_modified;
    }
}
