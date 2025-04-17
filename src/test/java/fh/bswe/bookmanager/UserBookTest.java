package fh.bswe.bookmanager;

import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.entity.UserBook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


/**
 * Unit tests for the {@link UserBook} entity class.
 * <p>
 * This test class verifies the correctness of all getter and setter methods
 * in the {@link UserBook} class by checking whether values are properly stored and retrieved.
 * </p>
 */
class UserBookTest {

    /**
     * Tests setting and getting the ID of the {@link UserBook} entity.
     */
    @Test
    void testIdGetterSetter() {
        UserBook userBook = new UserBook();
        userBook.setId(42L);
        assertEquals(42L, userBook.getId());
    }

    /**
     * Tests setting and getting the {@link UserAccount} of the {@link UserBook} entity.
     */
    @Test
    void testUserAccountGetterSetter() {
        UserBook userBook = new UserBook();
        UserAccount userAccount = new UserAccount();
        userBook.setUser(userAccount);
        assertSame(userAccount, userBook.getUserAccount());
    }

    /**
     * Tests setting and getting the {@link Book} of the {@link UserBook} entity.
     */
    @Test
    void testBookGetterSetter() {
        UserBook userBook = new UserBook();
        Book book = new Book();
        userBook.setBook(book);
        assertSame(book, userBook.getBook());
    }

    /**
     * Tests setting and getting the rating of the {@link UserBook} entity.
     */
    @Test
    void testRatingGetterSetter() {
        UserBook userBook = new UserBook();
        userBook.setRating(4);
        assertEquals(4, userBook.getRating());
    }

    /**
     * Tests setting and getting the comment of the {@link UserBook} entity.
     */
    @Test
    void testCommentGetterSetter() {
        UserBook userBook = new UserBook();
        String comment = "Very insightful!";
        userBook.setComment(comment);
        assertEquals(comment, userBook.getComment());
    }
}
