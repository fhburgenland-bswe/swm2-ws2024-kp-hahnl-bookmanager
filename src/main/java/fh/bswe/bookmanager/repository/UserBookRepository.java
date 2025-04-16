package fh.bswe.bookmanager.repository;

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
}