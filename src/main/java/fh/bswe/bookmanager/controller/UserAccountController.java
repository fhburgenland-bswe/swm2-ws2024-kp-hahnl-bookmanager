package fh.bswe.bookmanager.controller;

import fh.bswe.bookmanager.dto.UserAccountDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
     * Returns {@code 201 Created} with the created user data if successful.
     * Returns {@code 409 Conflict} if the username already exists.
     * Returns {@code 422 Unprocessable Entity} if the input fails validation rules.
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
     * Returns {@code 200 Created} with the created user data if successful.
     * Returns {@code 400 Bad Request} if the username was not found.
     * Returns {@code 422 Unprocessable Entity} if the input fails validation rules.
     * </p>
     *
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
}
