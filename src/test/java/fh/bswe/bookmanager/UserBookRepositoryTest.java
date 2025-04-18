package fh.bswe.bookmanager;

import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.entity.UserBook;
import fh.bswe.bookmanager.repository.BookRepository;
import fh.bswe.bookmanager.repository.UserAccountRepository;
import fh.bswe.bookmanager.repository.UserBookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
