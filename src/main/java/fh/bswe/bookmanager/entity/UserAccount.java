package fh.bswe.bookmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a user account entity in the book management system.
 * This entity is mapped to the database table {@code user_account} and stores
 * basic user profile information such as username, first name, and last name.
 *
 * <p>
 * The {@code id} field is the primary key and is automatically generated.
 * The {@code username} is unique and required.
 * </p>
 */
@Entity
@Table(name = "user_account")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(length = 20, nullable = false)
    private String firstname;

    @Column(length = 20, nullable = false)
    private String lastname;

    /**
     * Returns the unique identifier of the user account.
     *
     * @return the ID of the user
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user account.
     * This method is usually used internally by the persistence provider.
     *
     * @param id the ID to set
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     * This must be a unique value and should not exceed 20 characters.
     *
     * @param username the username to set
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Returns the first name of the user.
     *
     * @return the first name of the user
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstname the first name to set
     */
    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    /**
     * Returns the last name of the user.
     *
     * @return the last name of the user
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastname the last name to set
     */
    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }
}
