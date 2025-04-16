package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.dto.UserAccountUpdateDto;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.exception.UserExistsException;
import fh.bswe.bookmanager.exception.UserNotFoundException;
import fh.bswe.bookmanager.repository.UserAccountRepository;
import fh.bswe.bookmanager.service.UserAccountService;
import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
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

    /**
     * Tests successful retrieval of a user account by username.
     * <p>
     * This test verifies that the {@link fh.bswe.bookmanager.service.UserAccountService#findUserAccountByUsername(String)}
     * method returns a correctly mapped {@link fh.bswe.bookmanager.dto.UserAccountDto}
     * when a user with the given username exists in the repository.
     * </p>
     * <p>
     * It also confirms that the repository method {@code findByUsername} is called exactly once.
     * </p>
     *
     * @throws UserNotFoundException if the user is not found (not expected in this test)
     */
    @Test
    void testFindUserAccount() throws UserNotFoundException {
        UserAccount storedUserAccount = new UserAccount();
        storedUserAccount.setId(1);
        storedUserAccount.setUsername("newuser");
        storedUserAccount.setFirstname("John");
        storedUserAccount.setLastname("Doe");

        Optional<UserAccount> userAccount = Optional.of(storedUserAccount);
        when(userAccountRepository.findByUsername("newuser")).thenReturn(userAccount);

        UserAccountDto result = userAccountService.findUserAccountByUsername("newuser");

        assertEquals("newuser", result.getUsername());
        assertEquals("John", result.getFirstname());
        assertEquals("Doe", result.getLastname());
        assertEquals(1, result.getId());
        verify(userAccountRepository, times(1)).findByUsername("newuser");
    }

    /**
     * Tests the behavior when a user with the given username does not exist.
     * <p>
     * This test ensures that the {@link fh.bswe.bookmanager.service.UserAccountService#findUserAccountByUsername(String)}
     * method throws a {@link fh.bswe.bookmanager.exception.UserNotFoundException}
     * when the repository returns an empty {@link Optional}.
     * </p>
     * <p>
     * It also verifies that the repository method {@code findByUsername} is called exactly once.
     * </p>
     *
     * @throws UserNotFoundException expected to be thrown by the method under test
     */
    @Test
    void testFindUserAccountNotFound() throws UserNotFoundException {
        when(userAccountRepository.findByUsername("newuser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userAccountService.findUserAccountByUsername("newuser"));
        verify(userAccountRepository, times(1)).findByUsername("newuser");
    }

    /**
     * Tests updating an existing user account in the service layer.
     * <p>
     * Ensures that when a user with the given username exists, the
     * firstname and lastname are updated and persisted correctly.
     * </p>
     *
     * @throws UserNotFoundException if the user is not found (not expected here)
     */
    @Test
    void testUpdateUserAccount() throws UserNotFoundException {
        UserAccount storedUserAccount = new UserAccount();
        storedUserAccount.setId(1);
        storedUserAccount.setUsername("olduser");
        storedUserAccount.setFirstname("John");
        storedUserAccount.setLastname("Doe");

        when(userAccountRepository.findByUsername("olduser")).thenReturn(Optional.of(storedUserAccount));

        UserAccount savedUserAccount = new UserAccount();
        savedUserAccount.setId(1);
        savedUserAccount.setUsername("olduser");
        savedUserAccount.setFirstname("John");
        savedUserAccount.setLastname("Smith");

        UserAccountUpdateDto userAccountUpdateDto = new UserAccountUpdateDto();
        userAccountUpdateDto.setFirstname("John");
        userAccountUpdateDto.setLastname("Smith");

        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(savedUserAccount);

        UserAccountDto result = userAccountService.updateUserAccount("olduser", userAccountUpdateDto);

        assertEquals("olduser", result.getUsername());
        assertEquals("John", result.getFirstname());
        assertEquals("Smith", result.getLastname());
        assertEquals(1, result.getId());
        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    }

    /**
     * Tests the service behavior when trying to update a non-existent user account.
     * <p>
     * Expects that a {@link fh.bswe.bookmanager.exception.UserNotFoundException} is thrown
     * when the user is not found in the repository.
     * </p>
     *
     * @throws UserNotFoundException expected exception
     */
    @Test
    void testUpdateUserAccountNotFound() throws UserNotFoundException {
        when(userAccountRepository.findByUsername("olduser")).thenReturn(Optional.empty());

        UserAccountUpdateDto userAccountUpdateDto = new UserAccountUpdateDto();
        userAccountUpdateDto.setFirstname("John");
        userAccountUpdateDto.setLastname("Smith");

        assertThrows(UserNotFoundException.class, () -> userAccountService.updateUserAccount("olduser", userAccountUpdateDto));
        verify(userAccountRepository, times(1)).findByUsername("olduser");
    }
}
