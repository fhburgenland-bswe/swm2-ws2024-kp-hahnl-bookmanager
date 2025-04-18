package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.BookDto;
import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.exception.BookExistsException;
import fh.bswe.bookmanager.exception.BookNotFoundException;
import fh.bswe.bookmanager.repository.BookRepository;
import fh.bswe.bookmanager.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link BookService}.
 */
@SpringBootTest
public class BookServiceTest {

    @MockitoBean
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    /**
     * Tests that {@code findBookByIsbn} returns a book when found.
     */
    @Test
    void testFindBookByIsbn() {
        Book book = new Book();
        book.setIsbn("123");

        when(bookRepository.findByIsbn("123")).thenReturn(Optional.of(book));

        Book result = bookService.findBookByIsbn("123");

        assertNotNull(result);
        assertEquals("123", result.getIsbn());
        verify(bookRepository).findByIsbn("123");
    }

    /**
     * Tests that {@code findBookByIsbn} throws {@link BookNotFoundException} when the book is not found.
     */
    @Test
    void testFindBookByIsbnBookNotFound() {
        when(bookRepository.findByIsbn("999")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findBookByIsbn("999"));
    }

    /**
     * Tests that {@code save} stores a new book and returns its DTO.
     */
    @Test
    void testSaveNewBook() {
        Book book = new Book();
        book.setIsbn("123");
        book.setTitle("My Book");

        when(bookRepository.findByIsbn("123")).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);

        BookDto result = bookService.save(book);

        assertNotNull(result);
        assertEquals("123", result.getIsbn());
        assertEquals("My Book", result.getTitle());
        verify(bookRepository).save(book);
    }

    /**
     * Tests that {@code save} throws {@link BookExistsException} if a book with the same ISBN already exists.
     */
    @Test
    void testSaveExistingBookThrowsException() {
        Book book = new Book();
        book.setIsbn("123");

        when(bookRepository.findByIsbn("123")).thenReturn(Optional.of(new Book()));

        assertThrows(BookExistsException.class, () -> bookService.save(book));
    }
}
