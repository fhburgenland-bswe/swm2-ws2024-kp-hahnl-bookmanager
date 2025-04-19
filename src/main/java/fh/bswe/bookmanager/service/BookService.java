package fh.bswe.bookmanager.service;

import fh.bswe.bookmanager.dto.BookDto;
import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.exception.BookExistsException;
import fh.bswe.bookmanager.exception.BookNotFoundException;
import fh.bswe.bookmanager.helper.Mapper;
import fh.bswe.bookmanager.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Service class for managing books in the system.
 * <p>
 * Provides operations for retrieving and saving books, and includes logic
 * to convert between entity and DTO representations.
 */
@Service
public class BookService {
    private final BookRepository bookRepository;

    /**
     * Constructs a new {@code BookService} with the given repository.
     *
     * @param bookRepository the book repository
     */
    public BookService(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Finds a {@link Book} entity by its ISBN.
     * ISBN 10 or ISBN 13 can be used with only numbers.
     *
     * @param isbn the ISBN of the book to find
     * @return the found {@link Book} entity
     * @throws BookNotFoundException if no book is found with the given ISBN
     */
    public Book findBookByIsbn(final String isbn) {
        final Optional<Book> book = bookRepository.findByIsbn(isbn);

        if (book.isEmpty()) {
            throw new BookNotFoundException("Book (isbn: " + isbn + ") not found");
        }

        return book.get();
    }

    /**
     * Saves a new book to the repository.
     * <p>
     * If a book with the same ISBN already exists, a {@link BookExistsException} is thrown.
     *
     * @param book the {@link Book} entity to save
     * @return a {@link BookDto} representing the saved book
     * @throws BookExistsException if a book with the same ISBN already exists
     */
    public BookDto save(final Book book) {
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new BookExistsException();
        }

        return Mapper.mapToDto(bookRepository.save(book));
    }
}
