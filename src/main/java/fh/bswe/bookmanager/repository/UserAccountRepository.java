package fh.bswe.bookmanager.repository;

import fh.bswe.bookmanager.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link UserAccount} entities.
 * <p>
 * This interface extends {@link CrudRepository}, which provides basic methods for
 * saving, finding, deleting, and updating entities. Spring Data automatically
 * generates the implementation at runtime.
 * </p>
 */
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    /**
     * Finds a user account by its username.
     *
     * @param username the username to search for (must not be {@code null})
     * @return an {@link Optional} containing the matching {@link UserAccount},
     *         or an empty {@code Optional} if no match is found
     */
    Optional<UserAccount> findByUsername(String username);
}
