package fh.bswe.bookmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) used to update a user account's firstname and lastname.
 * <p>
 * Both fields are required and must meet the following constraints:
 * </p>
 * <ul>
 *   <li>Not blank</li>
 *   <li>3 to 20 characters long</li>
 *   <li>Letters only (no digits, spaces, or symbols)</li>
 * </ul>
 */
public class UserAccountUpdateDto {
    @NotBlank(message = "Must not be empty")
    @Size(min = 3, max = 20, message = "The length must be between 3 and 20 characters!")
    @Pattern(regexp = "^[a-zA-Z]{3,20}$", message = "Firstname must be 3-20 letters only")
    private String firstname;

    @NotBlank(message = "Must not be empty")
    @Size(min = 3, max = 20, message = "The length must be between 3 and 20 characters!")
    @Pattern(regexp = "^[a-zA-Z]{3,20}$", message = "Lastname must be 3-20 letters only")
    private String lastname;

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

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserAccountUpdateDto that = (UserAccountUpdateDto) o;
        return Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname);
    }
}
