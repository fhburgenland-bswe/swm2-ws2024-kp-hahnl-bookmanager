package fh.bswe.bookmanager.repository;

import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.entity.UserBook;
import org.springframework.data.repository.CrudRepository;

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
}