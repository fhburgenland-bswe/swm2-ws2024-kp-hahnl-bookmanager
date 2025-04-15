package fh.bswe.bookmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import fh.bswe.bookmanager.controller.UserAccountController;
import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.exception.UserExistsException;
import fh.bswe.bookmanager.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(jsonPath("$.username").value("Username must be 5-20 characters and contain only letters, numbers, and underscores"));
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
                .andExpect(jsonPath("$.firstname").value("Firstname must be 3-20 letters only"));
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
                .andExpect(jsonPath("$.lastname").value("Lastname must be 3-20 letters only"));
    }

}
