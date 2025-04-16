package fh.bswe.bookmanager.controller;

import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.dto.UserAccountUpdateDto;
import fh.bswe.bookmanager.exception.UserExistsException;
import fh.bswe.bookmanager.exception.UserNotFoundException;
import fh.bswe.bookmanager.service.UserAccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing user accounts.
 * <p>
 * This controller handles incoming HTTP requests related to user accounts.
 * It delegates business logic to the {@link UserAccountService} and returns appropriate
 * HTTP responses depending on the outcome.
 * </p>
 */
@Validated
@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    /**
     * Constructs a new {@code UserAccountController} with the given service.
     *
     * @param userAccountService the service used for user account operations
     */
    public UserAccountController(final UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    /**
     * Creates a new user account.
     * <p>
     * Accepts a {@link UserAccountDto} as input and delegates the creation to the service layer.
     * <ul>
     *      <li>Returns {@code 201 Created} with the created user data if successful.</li>
     *      <li>Returns {@code 409 Conflict} if the username already exists.</li>
     *      <li>Returns {@code 422 Unprocessable Entity} if the input fails validation rules.</li>
     * </ul>
     * </p>
     *
     * @param userAccountDto the user account data to create
     * @return a {@link ResponseEntity} containing the created user or an error message
     */
    @PostMapping("")
    public ResponseEntity<?> createUserAccount(@Valid @RequestBody final UserAccountDto userAccountDto) {
        try {
            final UserAccountDto userAccountDtoCreated = userAccountService.save(userAccountDto);
            return new ResponseEntity<>(userAccountDtoCreated, HttpStatus.CREATED);
        } catch (UserExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Handles HTTP GET requests to retrieve a user account by username.
     * <p>
     * This endpoint expects a valid username as a path variable and returns the corresponding
     * {@link UserAccountDto} if found.
     * </p>
     * <ul>
     *      <li>Returns {@code 200 Successful} with the user data if successful.</li>
     *      <li>Returns {@code 400 Bad Request} if the username was not found.</li>
     *      <li>Returns {@code 422 Unprocessable Entity} if the input fails validation rules.</li>
     * </ul>
     * @param username the username to look up
     * @return {@link ResponseEntity} containing the user data or an error message
     */
    @GetMapping("{username}")
    public ResponseEntity<?> readUserAccount(
            @NotBlank
            @PathVariable("username")
            @Size(min = 5, max = 20, message = "The length must be between 5 and 20 characters")
            @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must be 5-20 characters and contain only letters, numbers, and underscores")
            final String username) {
        try {
            final UserAccountDto userAccountDto = userAccountService.findUserAccountByUsername(username);
            return new ResponseEntity<>(userAccountDto, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Handles HTTP PUT requests to update the firstname and lastname of a user account.
     * <p>
     * The username is passed as a path variable and must be 5–20 characters long,
     * containing only letters, numbers, or underscores.
     * </p>
     * <p>
     * The update data is provided in the request body as a {@link UserAccountUpdateDto},
     * which is validated for firstname and lastname constraints.
     * </p>
     * <ul>
     *      <li>Returns {@code 200 Successful} with the updated user information on success.</li>
     *      <li>Returns {@code 400 Bad Request} if the username was not found.</li>
     *      <li>Returns {@code 422 Unprocessable Entity} if the input fails validation rules.</li>
     * </ul>
     *
     * @param username the username of the user to update (path variable)
     * @param userAccountUpdateDto the new firstname and lastname values (request body)
     * @return the updated user information or an error response
     */
    @PutMapping("{username}")
    public ResponseEntity<?> updateUserAccount(
            @NotBlank
            @PathVariable("username")
            @Size(min = 5, max = 20, message = "The length must be between 5 and 20 characters")
            @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must be 5-20 characters and contain only letters, numbers, and underscores")
            final String username,
            @Valid @RequestBody final UserAccountUpdateDto userAccountUpdateDto) {
        try {
            final UserAccountDto userAccountDto = userAccountService.updateUserAccount(username, userAccountUpdateDto);
            return new ResponseEntity<>(userAccountDto, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Handles HTTP DELETE requests to remove a user account by username.
     * <p>
     * The {@code username} must be between 5 and 20 characters and can contain only letters,
     * numbers, and underscores. The request will return:
     * </p>
     * <ul>
     *     <li>HTTP 200 (OK) if the user is successfully deleted</li>
     *     <li>HTTP 400 (Bad Request) if the user is not found</li>
     * </ul>
     *
     * @param username the username of the user account to delete (path variable)
     * @return a {@link ResponseEntity} with appropriate HTTP status code
     */
    @DeleteMapping("{username}")
    public ResponseEntity<?> deleteUserAccount(
            @NotBlank
            @PathVariable("username")
            @Size(min = 5, max = 20, message = "The length must be between 5 and 20 characters")
            @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must be 5-20 characters and contain only letters, numbers, and underscores")
            final String username) {
        try {
            userAccountService.deleteUserAccountByUsername(username);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
