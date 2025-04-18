package fh.bswe.bookmanager.dto;

import java.util.List;

/**
 * Data Transfer Object representing an author retrieved from the OpenLibrary API.
 * <p>
 * Contains metadata about the author such as name, key, type, creation date,
 * last modification, and source records.
 */
@SuppressWarnings({
        "PMD.TooManyFields",
        "PMD.LongVariable",
        "PMD.UnusedPrivateField",
        "PMD.FieldNamingConventions",
        "PMD.MethodNamingConventions",
        "PMD.FormalParameterNamingConventions"
})
public class OpenLibraryAuthorDto {

    private Type type;
    private String name;
    private String key;
    private List<String> source_records;
    private int latest_revision;
    private int revision;
    private Created created;
    private LastModified last_modified;

    /**
     * Represents the type of the OpenLibrary entity (e.g., "author").
     */
    public static class Type {
        private String key;

        /**
         * Gets the type key.
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
     * Represents the creation metadata.
     */
    public static class Created {
        private String type;
        private String value;

        /**
         * Gets the type of the creation timestamp.
         *
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the type of the creation timestamp.
         *
         * @param type the type to set
         */
        public void setType(final String type) {
            this.type = type;
        }

        /**
         * Gets the creation timestamp value.
         *
         * @return the creation value
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the creation timestamp value.
         *
         * @param value the value to set
         */
        public void setValue(final String value) {
            this.value = value;
        }
    }

    /**
     * Represents the last modified metadata.
     */
    public static class LastModified {
        private String type;
        private String value;

        /**
         * Gets the type of the last modification timestamp.
         *
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the type of the last modification timestamp.
         *
         * @param type the type to set
         */
        public void setType(final String type) {
            this.type = type;
        }

        /**
         * Gets the last modification timestamp value.
         *
         * @return the last modification value
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the last modification timestamp value.
         *
         * @param value the value to set
         */
        public void setValue(final String value) {
            this.value = value;
        }
    }

    /**
     * Gets the type of the author entity.
     *
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of the author entity.
     *
     * @param type the type to set
     */
    public void setType(final Type type) {
        this.type = type;
    }

    /**
     * Gets the author's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the author's name.
     *
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the unique author key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the unique author key.
     *
     * @param key the key to set
     */
    public void setKey(final String key) {
        this.key = key;
    }

    /**
     * Gets the list of source records.
     *
     * @return the source records
     */
    public List<String> getSource_records() {
        return source_records;
    }

    /**
     * Sets the list of source records.
     *
     * @param source_records the source records to set
     */
    public void setSource_records(final List<String> source_records) {
        this.source_records = source_records;
    }

    /**
     * Gets the latest revision number.
     *
     * @return the latest revision
     */
    public int getLatest_revision() {
        return latest_revision;
    }

    /**
     * Sets the latest revision number.
     *
     * @param latest_revision the latest revision to set
     */
    public void setLatest_revision(final int latest_revision) {
        this.latest_revision = latest_revision;
    }

    /**
     * Gets the current revision number.
     *
     * @return the revision
     */
    public int getRevision() {
        return revision;
    }

    /**
     * Sets the current revision number.
     *
     * @param revision the revision to set
     */
    public void setRevision(final int revision) {
        this.revision = revision;
    }

    /**
     * Gets the creation metadata.
     *
     * @return the created metadata
     */
    public Created getCreated() {
        return created;
    }

    /**
     * Sets the creation metadata.
     *
     * @param created the created metadata to set
     */
    public void setCreated(final Created created) {
        this.created = created;
    }

    /**
     * Gets the last modification metadata.
     *
     * @return the last modified metadata
     */
    public LastModified getLast_modified() {
        return last_modified;
    }

    /**
     * Sets the last modification metadata.
     *
     * @param last_modified the last modified metadata to set
     */
    public void setLast_modified(final LastModified last_modified) {
        this.last_modified = last_modified;
    }
}
