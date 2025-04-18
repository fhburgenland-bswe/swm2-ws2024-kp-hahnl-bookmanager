package fh.bswe.bookmanager.repository;

import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.entity.UserBook;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

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
}