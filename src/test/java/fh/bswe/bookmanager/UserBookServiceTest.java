package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.BookDto;
import fh.bswe.bookmanager.dto.UserBookDto;
import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.entity.UserBook;
import fh.bswe.bookmanager.exception.BookNotFoundException;
import fh.bswe.bookmanager.exception.UserBookExistsException;
import fh.bswe.bookmanager.exception.UserBookNotFoundException;
import fh.bswe.bookmanager.exception.UserNotFoundException;
import fh.bswe.bookmanager.repository.BookRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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

    @MockitoBean
    private BookRepository bookRepository;

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

    /**
     * Tests behavior when the specified user does not exist.
     * <p>
     * Expects {@link UserNotFoundException} to be thrown.
     */
    @Test
    void testRemoveBookFromUserLibraryUserNotFound() {
        when(userAccountRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userBookService.removeBookFromUserLibrary("unknown", "1234567890"));
    }

    /**
     * Tests behavior when the specified book is not found in the database.
     * <p>
     * Expects {@link BookNotFoundException} to be thrown.
     */
    @Test
    void testRemoveBookFromUserLibraryBookNotFound() {
        UserAccount user = new UserAccount();
        when(userAccountRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(bookRepository.findByIsbn("unknown")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () ->
                userBookService.removeBookFromUserLibrary("testuser", "unknown"));
    }

    /**
     * Tests behavior when the book is not associated with the user's library.
     * <p>
     * Expects {@link UserBookExistsException} to be thrown.
     */
    @Test
    void testRemoveBookFromUserLibraryNotAssociated() {
        UserAccount user = new UserAccount();
        Book book = new Book();

        when(userAccountRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));
        when(userBookRepository.existsByUserAccountAndBook(user, book)).thenReturn(false);

        assertThrows(UserBookNotFoundException.class, () ->
                userBookService.removeBookFromUserLibrary("testuser", "1234567890"));
    }

    /**
     * Tests behavior when the book is associated with the user's library.
     * <p>
     * Expects to delete association
     */
    @Test
    void testRemoveBookFromUserLibraryAssociated() throws UserNotFoundException {
        UserAccount user = new UserAccount();
        Book book = new Book();

        when(userAccountRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));
        when(userBookRepository.existsByUserAccountAndBook(user, book)).thenReturn(true);
        doNothing().when(userBookRepository).deleteByUserAccountAndBook(user, book);

        userBookService.removeBookFromUserLibrary("testuser", "1234567890");

        verify(userBookRepository, times(1)).deleteByUserAccountAndBook(user, book);
    }

    /**
     * Tests that the method returns a list of {@link UserBookDto} for a valid username.
     */
    @Test
    void testReadUserBooks() throws UserNotFoundException {
        UserAccount user = new UserAccount();
        user.setUsername("testuser");

        Book book = new Book();
        book.setTitle("Test Book");
        book.setIsbn("1234567890");
        book.setAuthors("Author Name");

        UserBook userBook = new UserBook();
        userBook.setUser(user);
        userBook.setBook(book);
        userBook.setRating(5);
        userBook.setComment("Nice read");

        when(userAccountRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));
        when(userBookRepository.findByUserAccount(user))
                .thenReturn(List.of(userBook));

        List<UserBookDto> result = userBookService.readUserBooks("testuser", null);

        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
        assertEquals("1234567890", result.get(0).getIsbn());
        assertEquals("Author Name", result.get(0).getAuthor());
        assertEquals(5, result.get(0).getRating());
        assertEquals("Nice read", result.get(0).getComment());
    }

    /**
     * Tests that the method returns a list of rating filtered {@link UserBookDto} for a valid username.
     */
    @Test
    void testReadUserBooksRating() throws UserNotFoundException {
        UserAccount user = new UserAccount();
        user.setUsername("testuser");

        Book book1 = new Book();
        book1.setTitle("Test Book");
        book1.setIsbn("1234567890");
        book1.setAuthors("Author Name");

        UserBook userBook1 = new UserBook();
        userBook1.setUser(user);
        userBook1.setBook(book1);
        userBook1.setRating(5);
        userBook1.setComment("Nice read");

        Book book2 = new Book();
        book2.setTitle("Test Book 2");
        book2.setIsbn("1234564323");
        book2.setAuthors("Author Name 2");

        UserBook userBook2 = new UserBook();
        userBook2.setUser(user);
        userBook2.setBook(book2);
        userBook2.setRating(3);
        userBook2.setComment("Nice read");

        when(userAccountRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));
        when(userBookRepository.findByUserAccount(user))
                .thenReturn(List.of(userBook1, userBook2));

        List<UserBookDto> result = userBookService.readUserBooks("testuser", 5);

        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
        assertEquals("1234567890", result.get(0).getIsbn());
        assertEquals("Author Name", result.get(0).getAuthor());
        assertEquals(5, result.get(0).getRating());
        assertEquals("Nice read", result.get(0).getComment());
    }

    /**
     * Tests that a {@link UserNotFoundException} is thrown if the user does not exist.
     */
    @Test
    void testReadUserBooksUserNotFound() {
        when(userAccountRepository.findByUsername("nonexistent"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userBookService.readUserBooks("nonexistent", null));
    }

    /**
     * Tests that an empty list is returned if the user exists but has no books.
     */
    @Test
    void testReadUserBooksNoBooks() throws UserNotFoundException {
        UserAccount user = new UserAccount();
        user.setUsername("emptyuser");

        when(userAccountRepository.findByUsername("emptyuser"))
                .thenReturn(Optional.of(user));
        when(userBookRepository.findByUserAccount(user))
                .thenReturn(List.of());

        List<UserBookDto> result = userBookService.readUserBooks("emptyuser", null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests the successful update of a rating and comment for a user-book entry.
     */
    @Test
    void testAddRatingSuccess() throws UserNotFoundException {
        String username = "testuser";
        String isbn = "1234567890";

        UserAccount user = new UserAccount();
        user.setUsername(username);

        Book book = new Book();
        book.setIsbn(isbn);

        UserBook userBook = new UserBook();
        userBook.setBook(book);
        userBook.setUser(user);

        UserBookDto userBookDto = new UserBookDto();
        userBookDto.setRating(5);
        userBookDto.setComment("Excellent!");

        when(userAccountRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        when(userBookRepository.findByUserAccountAndBook(user, book)).thenReturn(Optional.of(userBook));
        when(userBookRepository.save(userBook)).thenReturn(userBook);

        UserBookDto result = userBookService.addRating(username, isbn, userBookDto);

        assertEquals(5, result.getRating());
        assertEquals("Excellent!", result.getComment());
    }

    /**
     * Tests that a {@link UserNotFoundException} is thrown when the user does not exist.
     */
    @Test
    void testAddRatingUserNotFound() {
        when(userAccountRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userBookService.addRating("missing", "1234567890", new UserBookDto()));
    }

    /**
     * Tests that a {@link BookNotFoundException} is thrown when the book is not found.
     */
    @Test
    void testAddRatingBookNotFound() {
        UserAccount user = new UserAccount();
        user.setUsername("test");

        when(userAccountRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(bookRepository.findByIsbn("9999999999")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () ->
                userBookService.addRating("test", "9999999999", new UserBookDto()));
    }

    /**
     * Tests that a {@link UserBookNotFoundException} is thrown when the book is not associated with the user.
     */
    @Test
    void testAddRatingUserBookNotFound() {
        UserAccount user = new UserAccount();
        user.setUsername("test");

        Book book = new Book();
        book.setIsbn("123");

        when(userAccountRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(bookRepository.findByIsbn("123")).thenReturn(Optional.of(book));
        when(userBookRepository.findByUserAccountAndBook(user, book)).thenReturn(Optional.empty());

        assertThrows(UserBookNotFoundException.class, () ->
                userBookService.addRating("test", "123", new UserBookDto()));
    }
}
