package fh.bswe.bookmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;


/**
 * Data Transfer Object (DTO) for transferring user account data between client and server.
 * <p>
 * This class is used to encapsulate and validate user input when creating or updating user accounts.
 * It includes validation annotations to ensure the input meets certain constraints.
 * </p>
 */
public class UserAccountDto {
    private Integer id;

    @NotNull
    @NotBlank(message = "Must not be empty")
    @Size(min = 5, max = 20, message = "The length must be between 5 and 20 characters!")
    @Pattern(regexp = "^[a-zA-Z0-9_]{5,20}$", message = "Username must be 5-20 characters and contain only letters, numbers, and underscores")
    private String username;

    @NotNull
    @NotBlank(message = "Must not be empty")
    @Size(min = 3, max = 20, message = "The length must be between 3 and 20 characters!")
    @Pattern(regexp = "^[a-zA-Z]{3,20}$", message = "Firstname must be 3-20 letters only")
    private String firstname;

    @NotNull
    @NotBlank(message = "Must not be empty")
    @Size(min = 3, max = 20, message = "The length must be between 3 and 20 characters!")
    @Pattern(regexp = "^[a-zA-Z]{3,20}$", message = "Lastname must be 3-20 letters only")
    private String lastname;

    /**
     * Returns the ID of the user account.
     *
     * @return the user ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the user account.
     *
     * @param id the user ID to set
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Returns the first name of the user.
     *
     * @return the first name
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
     * @return the last name
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
        final UserAccountDto that = (UserAccountDto) o;
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
        return Objects.hash(id);
    }
}
