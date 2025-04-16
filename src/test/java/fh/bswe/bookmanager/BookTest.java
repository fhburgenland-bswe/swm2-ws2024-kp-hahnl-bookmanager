package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.entity.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BookTest {
    /**
     * Verifies that getters and setters correctly assign and return values.
     */
    @Test
    void testGettersAndSetters() {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("1234567890123");
        book.setTitle("Test Title");
        book.setAuthors("Author A, Author B");
        book.setPublishYear(2020);
        book.setPublishers("Test Publisher");
        book.setCoverKey("cover_key");
        book.setCoverLink("http://cover.link");
        book.setLanguage("en");
        byte[] coverImage = new byte[]{1, 2, 3};
        book.setCoverImage(coverImage);

        assertEquals(1, book.getId());
        assertEquals("1234567890123", book.getIsbn());
        assertEquals("Test Title", book.getTitle());
        assertEquals("Author A, Author B", book.getAuthors());
        assertEquals(2020, book.getPublishYear());
        assertEquals("Test Publisher", book.getPublishers());
        assertEquals("cover_key", book.getCoverKey());
        assertEquals("http://cover.link", book.getCoverLink());
        assertEquals("en", book.getLanguage());
        assertArrayEquals(coverImage, book.getCoverImage());
    }

    /**
     * Ensures that getCoverImage() returns a defensive copy.
     */
    @Test
    void testCoverImageDefensiveCopy() {
        Book book = new Book();
        byte[] original = new byte[]{1, 2, 3};
        book.setCoverImage(original);
        byte[] retrieved = book.getCoverImage();

        assertNotSame(original, retrieved);
        assertArrayEquals(original, retrieved);

        retrieved[0] = 9;
        assertNotEquals(retrieved[0], book.getCoverImage()[0]);
    }

    /**
     * Ensures that null cover images are handled gracefully and do not cause errors.
     */
    @Test
    void testNullCoverImage() {
        Book book = new Book();
        book.setCoverImage(null);
        assertNull(book.getCoverImage());
    }

    /**
     * Tests that two equal {@link Book} instances produce the same hash code.
     */
    @Test
    public void testBookHashCode() {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("0123456789");

        Book book2 = new Book();
        book2.setId(1);
        book2.setIsbn("0123456789");

        assertEquals(book.hashCode(), book2.hashCode());
    }

    /**
     * Tests that two {@link Book} instances with identical data are equal.
     */
    @Test
    public void testBookEquals() {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("0123456789");

        Book book2 = new Book();
        book2.setId(1);
        book2.setIsbn("0123456789");

        Assertions.assertTrue(book.equals(book2));
    }

    /**
     * Tests that a {@link Book} is not equal to {@code null}.
     */
    @Test
    public void testBookEqualsNull() {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("0123456789");

        Assertions.assertFalse(book.equals(null));
    }

    /**
     * Tests that two {@link Book} instances with different isbn values are not equal.
     */
    @Test
    public void testBookEqualsFalse() {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("0123456789");

        Book book2 = new Book();
        book2.setId(1);
        book2.setIsbn("0123456780");

        Assertions.assertFalse(book.equals(book2));
    }

    /**
     * Tests that a {@link Book} is not equal to an object of a different class.
     */
    @Test
    public void testBookEqualsWrongClass() {
        Book book = new Book();
        book.setId(1);
        book.setIsbn("0123456789");

        UserAccountDto userAccountDto2 = new UserAccountDto();
        userAccountDto2.setFirstname("John");
        userAccountDto2.setLastname("Huber");

        Assertions.assertFalse(book.equals(userAccountDto2));
    }
}
