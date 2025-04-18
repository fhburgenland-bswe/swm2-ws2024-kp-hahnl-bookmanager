package fh.bswe.bookmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import fh.bswe.bookmanager.controller.UserAccountController;
import fh.bswe.bookmanager.dto.BookDto;
import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.dto.UserAccountUpdateDto;
import fh.bswe.bookmanager.dto.UserBookDto;
import fh.bswe.bookmanager.exception.BookNotFoundException;
import fh.bswe.bookmanager.exception.ConnectionErrorException;
import fh.bswe.bookmanager.exception.UserBookExistsException;
import fh.bswe.bookmanager.exception.UserBookNotFoundException;
import fh.bswe.bookmanager.exception.UserExistsException;
import fh.bswe.bookmanager.exception.UserNotFoundException;
import fh.bswe.bookmanager.service.UserAccountService;
import fh.bswe.bookmanager.service.UserBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Unit tests for the {@link UserAccountController}, focusing on HTTP endpoint behavior.
 * <p>
 * The tests are performed using Spring's {@link WebMvcTest} setup and {@link MockMvc}
 * to simulate HTTP requests and responses. The {@link UserAccountService} is mocked
 * using {@code @MockitoBean} to isolate the controller logic.
 * </p>
 */
@WebMvcTest(UserAccountController.class)
public class UserAccountControllerTest {

    /**
     * Mocks HTTP requests and responses without starting a real web server.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Serializes and deserializes JSON objects during request and response simulation.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Mocked business service injected into the controller.
     */
    @MockitoBean
    private UserAccountService userAccountService;

    @MockitoBean
    private UserBookService userBookService;

    /**
     * Tests successful user creation with valid input.
     * Expects HTTP 201 Created and returns the user data in JSON format.
     */
    @Test
    void testCreateUserAccount() throws Exception {
        UserAccountDto request = new UserAccountDto();
        request.setUsername("validuser");
        request.setFirstname("John");
        request.setLastname("Doe");

        UserAccountDto response = new UserAccountDto();
        response.setId(1);
        response.setUsername("validuser");
        response.setFirstname("John");
        response.setLastname("Doe");

        when(userAccountService.save(any())).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("validuser"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));
    }

    /**
     * Tests user creation with a duplicate username.
     * Expects HTTP 409 Conflict when {@link UserExistsException} is thrown by the service.
     */
    @Test
    void testCreateUserAccountUserExists() throws Exception {
        UserAccountDto request = new UserAccountDto();
        request.setUsername("existinguser");
        request.setFirstname("John");
        request.setLastname("Doe");

        when(userAccountService.save(any()))
                .thenThrow(new UserExistsException());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    /**
     * Tests user creation with an invalid username (contains illegal characters).
     * Expects HTTP 400 Bad Request and a validation error message for the "username" field.
     */
    @Test
    void testCreateUserAccountValidationFailsUsername() throws Exception {
        UserAccountDto request = new UserAccountDto();
        request.setUsername("wronguser-");
        request.setFirstname("John");
        request.setLastname("Doe");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username")
                        .value("Username must be 5-20 characters and contain only letters, numbers, and underscores"));
    }

    /**
     * Tests user creation with an invalid first name (contains illegal characters).
     * Expects HTTP 400 Bad Request and a validation error message for the "firstname" field.
     */
    @Test
    void testCreateUserAccountValidationFailsFirstname() throws Exception {
        UserAccountDto request = new UserAccountDto();
        request.setUsername("wronguser");
        request.setFirstname("John-");
        request.setLastname("Doe");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstname")
                        .value("Firstname must be 3-20 letters only"));
    }

    /**
     * Tests user creation with an invalid last name (contains illegal characters).
     * Expects HTTP 400 Bad Request and a validation error message for the "lastname" field.
     */
    @Test
    void testCreateUserAccountValidationFailsLastname() throws Exception {
        UserAccountDto request = new UserAccountDto();
        request.setUsername("wronguser");
        request.setFirstname("John");
        request.setLastname("Doe-");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lastname")
                        .value("Lastname must be 3-20 letters only"));
    }

    /**
     * Tests successful retrieval of a user account by username via HTTP GET.
     * <p>
     * Verifies that the {@code /api/users/{username}} endpoint returns a valid JSON response
     * containing the correct user data when the username exists.
     * </p>
     * <p>
     * Also checks that the HTTP status is 200 (OK) and the returned fields match the expected values.
     * </p>
     *
     * @throws Exception if the request processing fails
     */
    @Test
    void testReadUserAccount() throws Exception {
        UserAccountDto request = new UserAccountDto();
        request.setId(1);
        request.setUsername("validuser");
        request.setFirstname("John");
        request.setLastname("Doe");

        when(userAccountService.findUserAccountByUsername("validuser")).thenReturn(request);

        mockMvc.perform(get("/api/users/validuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.username").value("validuser"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));
    }

    /**
     * Tests validation failure when an invalid username is passed as a path variable.
     * <p>
     * This test verifies that the controller rejects usernames not matching the required pattern
     * or length (e.g., "va-") and responds with HTTP 422 (Unprocessable Entity).
     * </p>
     * <p>
     * It also checks that the validation error message is included in the JSON response body.
     * </p>
     *
     * @throws Exception if the request processing fails
     */
    @Test
    void testReadUserAccountValidationFailsUsername() throws Exception {
        mockMvc.perform(get("/api/users/va"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['readUserAccount.username']")
                        .value("The length must be between 5 and 20 characters"));
    }

    /**
     * Tests behavior when a requested user account does not exist.
     * <p>
     * This test verifies that the controller throws a {@link fh.bswe.bookmanager.exception.UserNotFoundException}
     * when the username is not found and responds with HTTP 400 (Bad Request).
     * </p>
     * <p>
     * The repository call is mocked to return an empty result, triggering the exception.
     * </p>
     *
     * @throws Exception if the request processing fails
     */
    @Test
    void testReadUserAccountNotFound() throws Exception {
        when(userAccountService.findUserAccountByUsername("notfound"))
                .thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/api/users/notfound"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Integration test for a successful HTTP PUT request to update a user account.
     * <p>
     * Verifies that a valid request with a known username returns a 200 OK response
     * and the updated user data in the response body.
     * </p>
     *
     * @throws Exception if the request processing fails
     */
    @Test
    void testUpdateUserAccount() throws Exception {
        UserAccountUpdateDto request = new UserAccountUpdateDto();
        request.setFirstname("John");
        request.setLastname("Smith");

        UserAccountDto response = new UserAccountDto();
        response.setId(1);
        response.setUsername("validuser");
        response.setFirstname("John");
        response.setLastname("Smith");

        when(userAccountService.updateUserAccount("validuser", request)).thenReturn(response);

        mockMvc.perform(put("/api/users/validuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("validuser"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Smith"));
    }

    /**
     * Integration test for updating a user account that does not exist.
     * <p>
     * Expects the controller to return a 400 Bad Request when the service
     * throws a {@link fh.bswe.bookmanager.exception.UserNotFoundException}.
     * </p>
     *
     * @throws Exception if the request processing fails
     */
    @Test
    void testUpdateUserAccountNotFound() throws Exception {
        UserAccountUpdateDto request = new UserAccountUpdateDto();
        request.setFirstname("John");
        request.setLastname("Smith");

        when(userAccountService.updateUserAccount("validuser", request))
                .thenThrow(new UserNotFoundException());

        mockMvc.perform(put("/api/users/validuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Integration test for validation failure on firstname during user update.
     * <p>
     * Verifies that the controller returns a 422 Unprocessable Entity
     * when the firstname does not match the pattern constraint.
     * </p>
     *
     * @throws Exception if the request processing fails
     */
    @Test
    void testUpdateUserAccountValidationFirstname() throws Exception {
        UserAccountUpdateDto request = new UserAccountUpdateDto();
        request.setFirstname("John-");
        request.setLastname("Smith");

        mockMvc.perform(put("/api/users/validuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstname")
                        .value("Firstname must be 3-20 letters only"));
    }

    /**
     * Tests successful deletion of a user account via HTTP DELETE request.
     * <p>
     * Mocks the service to do nothing when {@code deleteUserAccountByUsername} is called
     * with a valid username, and verifies that the controller responds with HTTP 200 (OK).
     * </p>
     *
     * @throws Exception if request processing fails
     */
    @Test
    void testDeleteUserAccount() throws Exception {
        doNothing().when(userAccountService).deleteUserAccountByUsername("validuser");

        mockMvc.perform(delete("/api/users/validuser"))
                .andExpect(status().isOk());
    }

    /**
     * Tests validation failure when deleting a user account with an invalid username.
     * <p>
     * Sends a DELETE request with a username that does not meet validation constraints
     * (e.g. too short). Expects HTTP 422 (Unprocessable Entity) and a structured
     * validation error response with the correct field and message.
     * </p>
     *
     * @throws Exception if request processing fails
     */
    @Test
    void testDeleteUserAccountValidationFailsUsername() throws Exception {
        mockMvc.perform(delete("/api/users/va"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['deleteUserAccount.username']")
                        .value("The length must be between 5 and 20 characters"));
    }

    /**
     * Tests deletion of a non-existent user account.
     * <p>
     * Mocks the service to throw a {@link fh.bswe.bookmanager.exception.UserNotFoundException}
     * when the username is not found. Verifies that the controller returns HTTP 400 (Bad Request).
     * </p>
     *
     * @throws Exception if request processing fails
     */
    @Test
    void testDeleteUserAccountNotFound() throws Exception {
        doThrow(new UserNotFoundException()).when(userAccountService).deleteUserAccountByUsername("notfound");

        mockMvc.perform(delete("/api/users/notfound"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests successful addition of a book to a user's library.
     * <p>
     * Simulates a valid POST request to add a book by ISBN to the given username.
     * Verifies that the controller responds with HTTP 201 (Created) and the correct book details.
     *
     * @throws Exception if the request fails
     */
    @Test
    void testAddBookToUserLibrary() throws Exception {
        BookDto response = new BookDto();
        response.setId(1);
        response.setPublishers("Test publishers");
        response.setAuthors("Test authors");
        response.setTitle("Test title");
        response.setIsbn("0123456789");

        when(userBookService.storeBookToUserLibrary("validuser", "0123456789")).thenReturn(response);

        mockMvc.perform(post("/api/users/validuser/books/0123456789"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.publishers").value("Test publishers"))
                .andExpect(jsonPath("$.authors").value("Test authors"))
                .andExpect(jsonPath("$.title").value("Test title"))
                .andExpect(jsonPath("$.isbn").value("0123456789"));
    }

    /**
     * Tests behavior when the specified user does not exist.
     * <p>
     * Expects the service to throw {@link UserNotFoundException}, and the controller
     * to respond with HTTP 400 (Bad Request).
     *
     * @throws Exception if the request fails
     */
    @Test
    void testAddBookToUserLibraryUserNotFound() throws Exception {
        when(userBookService.storeBookToUserLibrary("validuser", "0123456789")).thenThrow(new UserNotFoundException());

        mockMvc.perform(post("/api/users/validuser/books/0123456789"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests behavior when the specified book is not found.
     * <p>
     * Expects the service to throw {@link BookNotFoundException}, and the controller
     * to respond with HTTP 400 (Bad Request).
     *
     * @throws Exception if the request fails
     */
    @Test
    void testAddBookToUserLibraryBookNotFound() throws Exception {
        when(userBookService.storeBookToUserLibrary("validuser", "0123456789")).thenThrow(new BookNotFoundException("not found"));

        mockMvc.perform(post("/api/users/validuser/books/0123456789"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests behavior when a user tries to add a book that already exists in their library.
     * <p>
     * Expects the service to throw {@link UserBookExistsException}, and the controller
     * to respond with HTTP 409 (Conflict).
     *
     * @throws Exception if the request fails
     */
    @Test
    void testAddBookToUserLibraryUserBookExists() throws Exception {
        when(userBookService.storeBookToUserLibrary("validuser", "0123456789")).thenThrow(new UserBookExistsException("already exists"));

        mockMvc.perform(post("/api/users/validuser/books/0123456789"))
                .andExpect(status().isConflict());
    }

    /**
     * Tests handling of a connection error during book addition.
     * <p>
     * Simulates a failure to reach the OpenLibrary service and expects the controller
     * to return HTTP 500 (Internal Server Error) when {@link ConnectionErrorException} is thrown.
     *
     * @throws Exception if the request fails
     */
    @Test
    void testAddBookToUserLibraryConnectionError() throws Exception {
        when(userBookService.storeBookToUserLibrary("validuser", "0123456789")).thenThrow(new ConnectionErrorException("connection error"));

        mockMvc.perform(post("/api/users/validuser/books/0123456789"))
                .andExpect(status().isInternalServerError());
    }

    /**
     * Tests successful remove of a book from a user's library.
     * <p>
     * Simulates a valid DELETE request to remove a book by ISBN from the given username.
     * Verifies that the controller responds with HTTP 200 (OK).
     *
     * @throws Exception if the request fails
     */
    @Test
    void testRemoveBookFromUserLibrary() throws Exception {
        doNothing().when(userBookService).removeBookFromUserLibrary("validuser", "0123456789");

        mockMvc.perform(delete("/api/users/validuser/books/0123456789"))
                .andExpect(status().isOk());

        verify(userBookService, times(1)).removeBookFromUserLibrary("validuser", "0123456789");
    }

    /**
     * Tests behavior when the specified user does not exist.
     * <p>
     * Expects the service to throw {@link UserNotFoundException}, and the controller
     * to respond with HTTP 400 (Bad Request).
     *
     * @throws Exception if the request fails
     */
    @Test
    void testRemoveBookFromUserLibraryUserNotFound() throws Exception {
        doThrow(new UserNotFoundException())
                .when(userBookService).removeBookFromUserLibrary("validuser", "0123456789");

        mockMvc.perform(delete("/api/users/validuser/books/0123456789"))
                .andExpect(status().isBadRequest());

        verify(userBookService, times(1)).removeBookFromUserLibrary("validuser", "0123456789");
    }

    /**
     * Tests behavior when the specified book is not found.
     * <p>
     * Expects the service to throw {@link BookNotFoundException}, and the controller
     * to respond with HTTP 400 (Bad Request).
     *
     * @throws Exception if the request fails
     */
    @Test
    void testRemoveBookFromUserLibraryBookNotFound() throws Exception {
        doThrow(new BookNotFoundException("not found"))
                .when(userBookService).removeBookFromUserLibrary("validuser", "0123456789");
        mockMvc.perform(delete("/api/users/validuser/books/0123456789"))
                .andExpect(status().isBadRequest());

        verify(userBookService, times(1)).removeBookFromUserLibrary("validuser", "0123456789");
    }

    /**
     * Tests behavior when a user tries to remove a book that doesn't exist in their library.
     * <p>
     * Expects the service to throw {@link UserBookNotFoundException}, and the controller
     * to respond with HTTP 400 (Bad Request).
     *
     * @throws Exception if the request fails
     */
    @Test
    void testRemoveBookFromUserLibraryUserBookNotFound() throws Exception {
        doThrow(new UserBookNotFoundException("not found"))
                .when(userBookService).removeBookFromUserLibrary("validuser", "0123456789");

        mockMvc.perform(delete("/api/users/validuser/books/0123456789"))
                .andExpect(status().isBadRequest());

        verify(userBookService, times(1)).removeBookFromUserLibrary("validuser", "0123456789");
    }

    /**
     * Tests successful retrieval of a user's book library.
     * <p>
     * Expects HTTP 200 OK and a list of {@link UserBookDto} in JSON format.
     * </p>
     *
     * @throws Exception if the request fails
     */
    @Test
    void testReadUserBooksLibrarySuccess() throws Exception {
        UserBookDto book1 = new UserBookDto();
        book1.setIsbn("0123456789");
        book1.setTitle("Test Book");
        book1.setAuthor("Test Author");
        book1.setRating(5);
        book1.setComment("Excellent!");

        when(userBookService.readUserBooks("validuser")).thenReturn(List.of(book1));

        mockMvc.perform(get("/api/users/validuser/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].isbn").value("0123456789"))
                .andExpect(jsonPath("$[0].title").value("Test Book"))
                .andExpect(jsonPath("$[0].author").value("Test Author"))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[0].comment").value("Excellent!"));
    }

    /**
     * Tests behavior when the specified user is not found.
     * <p>
     * Expects HTTP 400 Bad Request with an appropriate error message.
     * </p>
     *
     * @throws Exception if the request fails
     */
    @Test
    void testReadUserBooksLibraryUserNotFound() throws Exception {
        when(userBookService.readUserBooks("unknownuser")).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/api/users/unknownuser/books"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests validation failure due to invalid username format.
     * <p>
     * Expects HTTP 422 Unprocessable Entity with a validation error message.
     * </p>
     *
     * @throws Exception if the request fails
     */
    @Test
    void testReadUserBooksLibraryInvalidUsername() throws Exception {
        mockMvc.perform(get("/api/users/ab/books"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['readUserBooksLibrary.username']")
                        .value("The length must be between 5 and 20 characters"));
    }
}
