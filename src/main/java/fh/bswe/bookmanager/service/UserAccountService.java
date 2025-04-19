package fh.bswe.bookmanager.service;

import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.dto.UserAccountUpdateDto;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.exception.UserExistsException;
import fh.bswe.bookmanager.exception.UserNotFoundException;
import fh.bswe.bookmanager.helper.Mapper;
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
     * Retrieves a {@link UserAccountDto} by the given username.
     * <p>
     * This method attempts to find a user account with the specified username from the repository.
     * If no user with the given username exists, a {@link UserNotFoundException} is thrown.
     * </p>
     *
     * @param username the username of the user account to retrieve
     * @return the corresponding {@link UserAccountDto} if found
     * @throws UserNotFoundException if no user account with the given username exists
     */
    public UserAccountDto findUserAccountByUsername(final String username) throws UserNotFoundException {
        final Optional<UserAccount> userAccount = userAccountRepository.findByUsername(username);

        if (userAccount.isEmpty()) {
            throw new UserNotFoundException();
        }

        return Mapper.mapToDto(userAccount.get());
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

        return Mapper.mapToDto(userAccountRepository.save(Mapper.mapToEntity(userAccountDto)));
    }

    /**
     * Updates the firstname and lastname of an existing user account.
     * <p>
     * Looks up the user by username. If found, the firstname and lastname
     * are updated using the values from the given {@link UserAccountUpdateDto}.
     * </p>
     * <p>
     * Returns the updated user data as a {@link UserAccountDto}.
     * If the user does not exist, a {@link UserNotFoundException} is thrown.
     * </p>
     *
     * @param username the username of the user to update
     * @param userAccountUpdateDto the new values for firstname and lastname
     * @return the updated {@link UserAccountDto}
     * @throws UserNotFoundException if no user with the given username exists
     */
    public UserAccountDto updateUserAccount(final String username, final UserAccountUpdateDto userAccountUpdateDto) throws UserNotFoundException {
        final Optional<UserAccount> userAccount = userAccountRepository.findByUsername(username);

        if (userAccount.isEmpty()) {
            throw new UserNotFoundException();
        }

        userAccount.get().setFirstname(userAccountUpdateDto.getFirstname());
        userAccount.get().setLastname(userAccountUpdateDto.getLastname());

        return Mapper.mapToDto(userAccountRepository.save(userAccount.get()));
    }

    /**
     * Deletes a user account by its username.
     * <p>
     * Searches for a {@link fh.bswe.bookmanager.entity.UserAccount} using the provided username.
     * If the user exists, it is deleted from the repository. Otherwise, a
     * {@link fh.bswe.bookmanager.exception.UserNotFoundException} is thrown.
     * </p>
     *
     * @param username the username of the user account to be deleted
     * @throws UserNotFoundException if no user with the given username exists
     */
    public void deleteUserAccountByUsername(final String username) throws UserNotFoundException {
        final Optional<UserAccount> userAccount = userAccountRepository.findByUsername(username);

        if (userAccount.isEmpty()) {
            throw new UserNotFoundException();
        }

        userAccountRepository.delete(userAccount.get());
    }

    private Optional<UserAccount> findUserByUsername(final String username) {
        return userAccountRepository.findByUsername(username);
    }
}
