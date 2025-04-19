package fh.bswe.bookmanager.repository;

import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.entity.UserBook;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link fh.bswe.bookmanager.entity.UserBook} entities.
 * <p>
 * This interface extends {@link org.springframework.data.repository.CrudRepository}
 * to provide basic CRUD operations for the {@code user_book} table.
 * </p>
 *
 * @author
 */
public interface UserBookRepository extends CrudRepository<UserBook, Long> {

    /**
     * Checks if a {@link UserBook} entry exists for the given user and book.
     *
     * @param userAccount the user account to check
     * @param book        the book to check
     * @return {@code true} if a mapping exists, otherwise {@code false}
     */
    boolean existsByUserAccountAndBook(UserAccount userAccount, Book book);

    /**
     * Deletes the {@link UserBook} entry that associates the given user account with the given book.
     *
     * @param userAccount the user account whose association should be removed
     * @param book        the book to disassociate from the user
     */
    void deleteByUserAccountAndBook(UserAccount userAccount, Book book);

    /**
     * Find all {@link UserBook} entries that associates the given user account.
     *
     * @param userAccount   the user account whose association should be found
     * @return              all books associates the given user account
     */
    List<UserBook> findByUserAccount(UserAccount userAccount);

    /**
     * Retrieves a {@link UserBook} entry that associates the specified user account with a specific book.
     * <p>
     * This method is typically used to fetch a user's specific interaction with a book,
     * such as for checking or updating metadata like ratings or comments.
     * </p>
     *
     * @param userAccount the user account to look up
     * @param book        the book to look up
     * @return an {@link Optional} containing the matching {@link UserBook} if found, or empty if not
     */
    Optional<UserBook> findByUserAccountAndBook(UserAccount userAccount, Book book);
}