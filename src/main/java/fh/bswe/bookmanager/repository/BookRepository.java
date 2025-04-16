package fh.bswe.bookmanager.repository;

import fh.bswe.bookmanager.entity.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link Book} entities.
 * <p>
 * Extends {@link CrudRepository} to provide standard CRUD operations.
 * </p>
 */
public interface BookRepository extends CrudRepository<Book, Long> {
    /**
     * Finds a book by its ISBN.
     *
     * @param isbn the ISBN to search for
     * @return an {@link Optional} containing the matching {@link Book} if found, or empty if not
     */
    Optional<Book> findByIsbn(String isbn);
}
