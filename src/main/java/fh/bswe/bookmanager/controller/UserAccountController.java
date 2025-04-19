package fh.bswe.bookmanager.controller;

import fh.bswe.bookmanager.dto.BookDto;
import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.dto.UserAccountUpdateDto;
import fh.bswe.bookmanager.dto.UserBookDto;
import fh.bswe.bookmanager.exception.BookNotFoundException;
import fh.bswe.bookmanager.exception.UserBookExistsException;
import fh.bswe.bookmanager.exception.UserBookNotFoundException;
import fh.bswe.bookmanager.exception.UserExistsException;
import fh.bswe.bookmanager.exception.UserNotFoundException;
import fh.bswe.bookmanager.service.UserAccountService;
import fh.bswe.bookmanager.service.UserBookService;
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

import java.util.List;

/**
 * REST controller for managing user accounts.
 * <p>
 * This controller handles incoming HTTP requests related to user accounts.
 * </p>
 */
@SuppressWarnings({
        "PMD.AvoidDuplicateLiterals"
})
@Validated
@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final UserBookService userBookService;

    /**
     * Constructs a new {@code UserAccountController} with the given service.
     *
     * @param userAccountService the service used for user account operations
     * @param userBookService    the service used for user-book operations.
     */
    public UserAccountController(final UserAccountService userAccountService,
                                 final UserBookService userBookService) {
        this.userAccountService = userAccountService;
        this.userBookService = userBookService;
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
    @GetMapping("/{username}")
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
    @PutMapping("/{username}")
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
    @DeleteMapping("/{username}")
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

    /**
     * Adds a book to the library of a specific user.
     * <p>
     * This endpoint attempts to associate a book, identified by its ISBN, with the given user.
     * If the user or book does not exist, or if the book is already assigned to the user, an appropriate
     * error response is returned.
     * </p>
     * <ul>
     *     <li>{@code 201 CREATED} with the {@link BookDto} if the book is successfully added</li>
     *     <li>{@code 400 BAD REQUEST} if the user or book was not found</li>
     *     <li>{@code 409 CONFLICT} if the book already exists in the user's library</li>
     *     <li>{@code 500 INTERNAL SERVER ERROR} for any unexpected errors</li>
     * </ul>
     * @param username the username of the user whose library the book will be added to.
     *                 Must be 5–20 characters long and contain only letters, numbers, and underscores.
     * @param isbn     the ISBN number of the book to add.
     *                 Must be 10 or 13 digits long and consist only of digits.
     * @return a {@link ResponseEntity} and the added book data
     */
    @PostMapping("/{username}/books/{isbn}")
    public ResponseEntity<?> addBookToUserLibrary(
            @NotBlank
            @PathVariable("username")
            @Size(min = 5, max = 20, message = "The length must be between 5 and 20 characters")
            @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must be 5-20 characters and contain only letters, numbers, and underscores")
            final String username,
            @NotBlank
            @PathVariable("isbn")
            @Size(min = 10, max = 13, message = "The length must be between 10 and 13 digits")
            @Pattern(regexp = "^[0-9]{10,13}$", message = "ISBN must be 10 or 13 digits and contain only digits")
            final String isbn) {
        try {
            final BookDto bookDto = userBookService.storeBookToUserLibrary(username, isbn);
            return new ResponseEntity<>(bookDto, HttpStatus.CREATED);
        } catch (UserNotFoundException | BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (UserBookExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Removes a specific book from a user's personal library.
     * <p>
     * This endpoint is accessed via HTTP DELETE at {@code /{username}/books/{isbn}} and deletes
     * the association between the given user and the specified book (by ISBN). If the user or book
     * does not exist, or the book is not associated with the user, an appropriate error message
     * is returned.
     * </p>
     *
     * @param username the username of the user whose library the book should be removed from;
     *                 must be 5–20 alphanumeric characters or underscores
     * @param isbn     the ISBN of the book to remove; must be a 10- or 13-digit number
     * @return {@link ResponseEntity} with:
     *         <ul>
     *             <li>{@code 200 OK} if the removal was successful</li>
     *             <li>{@code 400 Bad Request} with an error message if the user, book,
     *             or association was not found</li>
     *         </ul>
     * @throws UserNotFoundException       if the user does not exist in the database
     * @throws BookNotFoundException       if the book does not exist in the database
     * @throws UserBookNotFoundException   if the book is not associated with the user
     */
    @DeleteMapping("/{username}/books/{isbn}")
    public ResponseEntity<?> removeBookToUserLibrary(
            @NotBlank
            @PathVariable("username")
            @Size(min = 5, max = 20, message = "The length must be between 5 and 20 characters")
            @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must be 5-20 characters and contain only letters, numbers, and underscores")
            final String username,
            @NotBlank
            @PathVariable("isbn")
            @Size(min = 10, max = 13, message = "The length must be between 10 and 13 digits")
            @Pattern(regexp = "^[0-9]{10,13}$", message = "ISBN must be 10 or 13 digits and contain only digits")
            final String isbn) {
        try {
            userBookService.removeBookFromUserLibrary(username, isbn);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException | BookNotFoundException | UserBookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Retrieves the list of books associated with a specific user.
     * <p>
     * This endpoint returns a list of {@link UserBookDto}
     * entries representing the books stored in a user's personal library.
     * </p>
     *
     * @param username the username of the user whose book library is to be retrieved;
     *                 must be 5–20 characters long and contain only letters, numbers, and underscores
     * @return {@link ResponseEntity} with:
     *         <ul>
     *             <li>HTTP 200 OK and an array of {@link UserBookDto} if successful</li>
     *             <li>HTTP 400 Bad Request if the user does not exist</li>
     *         </ul>
     */
    @GetMapping("/{username}/books")
    public ResponseEntity<?> readUserBooksLibrary(
            @NotBlank
            @PathVariable("username")
            @Size(min = 5, max = 20, message = "The length must be between 5 and 20 characters")
            @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must be 5-20 characters and contain only letters, numbers, and underscores")
            final String username) {
        try {
            final List<UserBookDto> userBookDtos = userBookService.readUserBooks(username);
            return new ResponseEntity<>(userBookDtos.toArray(), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
