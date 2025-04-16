package fh.bswe.bookmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

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

    /**
     * Checks whether this {@code UserAccountDto} is equal to another object.
     * <p>
     * Two {@code UserAccountDto} instances are considered equal if all of the following fields are equal:
     * <ul>
     *     <li>{@code id}</li>
     * </ul>
     * </p>
     *
     * @param o the object to compare with this instance
     * @return {@code true} if the given object is a {@code UserAccountDto} and all
     *         fields match; {@code false} otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserAccount that = (UserAccount) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Computes the hash code for this {@code UserAccountDto} based on its fields.
     * <p>
     * The hash code is computed using:
     * {@code id}.
     * This ensures consistency with the {@link #equals(Object)} method.
     * </p>
     *
     * @return the computed hash code for this object
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
