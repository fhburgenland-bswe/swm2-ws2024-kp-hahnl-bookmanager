package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.BookDto;
import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.entity.UserBook;
import fh.bswe.bookmanager.exception.UserBookExistsException;
import fh.bswe.bookmanager.exception.UserNotFoundException;
import fh.bswe.bookmanager.repository.UserAccountRepository;
import fh.bswe.bookmanager.repository.UserBookRepository;
import fh.bswe.bookmanager.service.OpenLibraryService;
import fh.bswe.bookmanager.service.UserBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserBookServiceTest {

    @MockitoBean
    private UserBookRepository userBookRepository;

    @MockitoBean
    private UserAccountRepository userAccountRepository;

    @MockitoBean
    private OpenLibraryService openLibraryService;

    @InjectMocks
    @Autowired
    private UserBookService userBookService;

    private UserAccount user;
    private Book book;

    @BeforeEach
    void setUp() {
        user = new UserAccount();
        user.setUsername("testuser");

        book = new Book();
        book.setId(1);
        book.setIsbn("1234567890");
        book.setTitle("Test Book");
    }

    /**
     * Tests successful storage of a book to a user's library.
     */
    @Test
    void storeBookToUserLibraryUserAndBookAreValid() throws UserNotFoundException {
        when(userAccountRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(openLibraryService.findAndStoreBookByIsbn("1234567890")).thenReturn(book);
        when(userBookRepository.existsByUserAccountAndBook(user, book)).thenReturn(false);

        BookDto result = userBookService.storeBookToUserLibrary("testuser", "1234567890");

        assertNotNull(result);
        assertEquals("1234567890", result.getIsbn());
        verify(userBookRepository).save(any(UserBook.class));
    }

    /**
     * Tests exception is thrown when user is not found.
     */
    @Test
    void storeBookToUserLibraryUserDoesNotExist() {
        when(userAccountRepository.findByUsername("missinguser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userBookService.storeBookToUserLibrary("missinguser", "1234567890"));

        verify(userAccountRepository).findByUsername("missinguser");
        verifyNoInteractions(openLibraryService);
        verifyNoInteractions(userBookRepository);
    }

    /**
     * Tests exception is thrown if the book is already in the user's library.
     */
    @Test
    void storeBookToUserLibraryBookAlreadyAdded() {
        when(userAccountRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(openLibraryService.findAndStoreBookByIsbn("1234567890")).thenReturn(book);
        when(userBookRepository.existsByUserAccountAndBook(user, book)).thenReturn(true);

        assertThrows(UserBookExistsException.class,
                () -> userBookService.storeBookToUserLibrary("testuser", "1234567890"));

        verify(userBookRepository, never()).save(any());
    }
}
