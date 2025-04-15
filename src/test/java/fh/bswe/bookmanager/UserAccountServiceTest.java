package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.exception.UserExistsException;
import fh.bswe.bookmanager.repository.UserAccountRepository;
import fh.bswe.bookmanager.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit and integration-like tests for the {@link UserAccountService} class.
 * <p>
 * These tests verify the behavior of the service layer, especially when interacting with the {@link UserAccountRepository}.
 * The repository is mocked to isolate the service logic, while the service itself is injected via Spring Boot's context.
 * </p>
 * <p>
 * The test class uses {@link SpringBootTest} for Spring context initialization and {@code @MockitoBean}
 * to override the repository dependency with a Mockito mock.
 * </p>
 */
@SpringBootTest
public class UserAccountServiceTest {

    /**
     * Mocked user account repository to simulate database interactions.
     * Replaces the real bean in the Spring context.
     */
    @MockitoBean
    private UserAccountRepository userAccountRepository;

    /**
     * The service under test, injected from the application context.
     */
    @Autowired
    private UserAccountService userAccountService;

    /**
     * Tests successful user creation when the username is not already taken.
     * <p>
     * Ensures that:
     * <ul>
     *     <li>The repository's {@code findByUsername} returns empty.</li>
     *     <li>The {@code save} method is called once.</li>
     *     <li>The returned DTO contains correct user data.</li>
     * </ul>
     * </p>
     *
     * @throws UserExistsException if the username check fails unexpectedly
     */
    @Test
    void testCreateUserAccount() throws UserExistsException {
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setUsername("newuser");
        userAccountDto.setFirstname("John");
        userAccountDto.setLastname("Doe");

        when(userAccountRepository.findByUsername("newuser")).thenReturn(Optional.empty());

        UserAccount savedUserAccount = new UserAccount();
        savedUserAccount.setId(1);
        savedUserAccount.setUsername("newuser");
        savedUserAccount.setFirstname("John");
        savedUserAccount.setLastname("Doe");

        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(savedUserAccount);

        UserAccountDto result = userAccountService.save(userAccountDto);

        assertEquals("newuser", result.getUsername());
        assertEquals("John", result.getFirstname());
        assertEquals("Doe", result.getLastname());
        assertEquals(1, result.getId());
        verify(userAccountRepository).save(any(UserAccount.class));
    }

    /**
     * Tests that the service throws a {@link UserExistsException} when the username already exists.
     * <p>
     * Ensures that:
     * <ul>
     *     <li>The repository detects the existing username.</li>
     *     <li>No call to {@code save} is made.</li>
     * </ul>
     * </p>
     */
    @Test
    void testCreateUserAccountUsernameExists() {
        UserAccountDto dto = new UserAccountDto();
        dto.setUsername("existinguser");

        when(userAccountRepository.findByUsername("existinguser"))
                .thenReturn(Optional.of(new UserAccount()));

        assertThrows(UserExistsException.class, () -> userAccountService.save(dto));
        verify(userAccountRepository, never()).save(any());
    }
}
