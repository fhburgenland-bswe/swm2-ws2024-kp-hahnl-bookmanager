package fh.bswe.bookmanager.service;

import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.exception.UserExistsException;
import fh.bswe.bookmanager.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for handling user account-related operations.
 */
@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;

    /**
     * Constructs a new {@code UserAccountService} with the given repository.
     *
     * @param userAccountRepository the repository used to access user account data
     */
    public UserAccountService(final UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * Finds a user account by the given username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the user if found, or empty if not
     */
    public Optional<UserAccount> findUserByUsername(final String username) {
        return userAccountRepository.findByUsername(username);
    }

    /**
     * Saves a new user account after checking for uniqueness.
     * <p>
     * If a user with the given username already exists, a {@link UserExistsException} is thrown.
     * </p>
     *
     * @param userAccountDto the user account data to save
     * @return the saved user as a {@link UserAccountDto}
     * @throws UserExistsException if a user with the same username already exists
     */
    public UserAccountDto save(final UserAccountDto userAccountDto) throws UserExistsException {
        if (findUserByUsername(userAccountDto.getUsername()).isPresent()) {
            throw new UserExistsException();
        }

        return mapToDto(userAccountRepository.save(mapToEntity(userAccountDto)));
    }

    private UserAccount mapToEntity(final UserAccountDto userAccountDto) {
        final UserAccount userAccount = new UserAccount();
        userAccount.setUsername(userAccountDto.getUsername());
        userAccount.setFirstname(userAccountDto.getFirstname());
        userAccount.setLastname(userAccountDto.getLastname());
        return userAccount;
    }

    private UserAccountDto mapToDto(final UserAccount user) {
        final UserAccountDto dto = new UserAccountDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        return dto;
    }
}
