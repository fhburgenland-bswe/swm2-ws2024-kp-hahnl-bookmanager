package fh.bswe.bookmanager;

import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.entity.UserBook;
import fh.bswe.bookmanager.repository.BookRepository;
import fh.bswe.bookmanager.repository.UserAccountRepository;
import fh.bswe.bookmanager.repository.UserBookRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserBookRepositoryTest {

    @Autowired
    private UserBookRepository userBookRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Tests that {@code existsByUserAccountAndBook} returns {@code true}
     * when a {@link UserBook} entry exists.
     */
    @Test
    void testExistsByUserAccountAndBookExists() {
        UserAccount user = new UserAccount();
        user.setUsername("testuser");
        user.setFirstname("firstname");
        user.setLastname("lastname");
        user = userAccountRepository.save(user);

        Book book = new Book();
        book.setIsbn("1234567890");
        book.setTitle("Test Book");
        book = bookRepository.save(book);

        UserBook userBook = new UserBook();
        userBook.setUser(user);
        userBook.setBook(book);
        userBookRepository.save(userBook);

        boolean exists = userBookRepository.existsByUserAccountAndBook(user, book);
        assertTrue(exists);
    }

    /**
     * Tests that {@code existsByUserAccountAndBook} returns {@code false}
     * when no {@link UserBook} entry exists for the given user and book.
     */
    @Test
    void testExistsByUserAccountAndBookDoesNotExist() {
        UserAccount user = new UserAccount();
        user.setUsername("newuser");
        user.setFirstname("firstname");
        user.setLastname("lastname");
        user = userAccountRepository.save(user);

        Book book = new Book();
        book.setIsbn("9876543210");
        book.setTitle("New Book");
        book = bookRepository.save(book);

        boolean exists = userBookRepository.existsByUserAccountAndBook(user, book);
        assertFalse(exists);
    }

    /**
     * Tests that {@link UserBookRepository#deleteByUserAccountAndBook(UserAccount, Book)}
     * successfully deletes the mapping between user and book.
     */
    @Transactional
    @Test
    void testDeleteByUserAccountAndBook() {
        UserAccount user = new UserAccount();
        user.setUsername("deletetest");
        user.setFirstname("firstname");
        user.setLastname("lastname");
        userAccountRepository.save(user);

        Book book = new Book();
        book.setIsbn("1112223334");
        bookRepository.save(book);

        UserBook userBook = new UserBook();
        userBook.setUser(user);
        userBook.setBook(book);
        userBookRepository.save(userBook);

        assertTrue(userBookRepository.existsByUserAccountAndBook(user, book));

        userBookRepository.deleteByUserAccountAndBook(user, book);

        assertFalse(userBookRepository.existsByUserAccountAndBook(user, book));
    }

    /**
     * Tests that {@link UserBookRepository#findByUserAccount(UserAccount)} returns the correct
     * list of {@link UserBook} entries associated with the given {@link UserAccount}.
     * <p>
     * This test verifies the query behavior with one user and two associated books.
     */
    @Test
    void testFindByUserAccountCorrectEntries() {
        UserAccount user = new UserAccount();
        user.setUsername("reader1");
        user.setFirstname("Alice");
        user.setLastname("Smith");
        user = userAccountRepository.save(user);

        Book book1 = new Book();
        book1.setIsbn("1111111111");
        book1.setTitle("Book One");
        book1 = bookRepository.save(book1);

        Book book2 = new Book();
        book2.setIsbn("2222222222");
        book2.setTitle("Book Two");
        book2 = bookRepository.save(book2);

        UserBook userBook1 = new UserBook();
        userBook1.setUser(user);
        userBook1.setBook(book1);
        userBookRepository.save(userBook1);

        UserBook userBook2 = new UserBook();
        userBook2.setUser(user);
        userBook2.setBook(book2);
        userBookRepository.save(userBook2);

        List<UserBook> result = userBookRepository.findByUserAccount(user);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(ub -> ub.getBook().getIsbn().equals("1111111111")));
        assertTrue(result.stream().anyMatch(ub -> ub.getBook().getIsbn().equals("2222222222")));
    }

    /**
     * Tests that {@link UserBookRepository#findByUserAccount(UserAccount)} returns
     * an empty list when the user has no associated books.
     */
    @Test
    void testFindByUserAccountEmptyList() {
        UserAccount user = new UserAccount();
        user.setUsername("xnewuser");
        user.setFirstname("Bob");
        user.setLastname("Brown");
        user = userAccountRepository.save(user);

        List<UserBook> result = userBookRepository.findByUserAccount(user);

        assertTrue(result.isEmpty());
    }
}
