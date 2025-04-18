package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.BookDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the {@link BookDto} class.
 * <p>
 * This test class verifies the correct behavior of getters and setters,
 * especially with respect to defensive copying of the {@code coverImage} field.
 */
public class BookDtoTest {

    /**
     * Tests the basic functionality of all getters and setters in the {@link BookDto} class.
     * Ensures that values are correctly stored and retrieved.
     */
    @Test
    void testGettersAndSetters() {
        BookDto dto = new BookDto();

        dto.setId(1);
        dto.setIsbn("9781234567890");
        dto.setTitle("Test Title");
        dto.setAuthors("Author One, Author Two");
        dto.setPublishDate("2024");
        dto.setPublishers("Publisher A");
        dto.setCoverKey("OL12345M");
        dto.setCoverLink("http://example.com/cover.jpg");
        dto.setLanguage("en");

        byte[] image = {1, 2, 3, 4};
        dto.setCoverImage(image);

        assertEquals(1, dto.getId());
        assertEquals("9781234567890", dto.getIsbn());
        assertEquals("Test Title", dto.getTitle());
        assertEquals("Author One, Author Two", dto.getAuthors());
        assertEquals("2024", dto.getPublishDate());
        assertEquals("Publisher A", dto.getPublishers());
        assertEquals("OL12345M", dto.getCoverKey());
        assertEquals("http://example.com/cover.jpg", dto.getCoverLink());
        assertEquals("en", dto.getLanguage());
        assertArrayEquals(image, dto.getCoverImage());
    }

    /**
     * Tests that the {@code coverImage} field is defensively copied
     * to protect the internal state of the {@link BookDto} instance.
     */
    @Test
    void testCoverImageDefensiveCopy() {
        BookDto dto = new BookDto();
        byte[] original = {1, 2, 3};
        dto.setCoverImage(original);

        byte[] retrieved = dto.getCoverImage();
        assertArrayEquals(original, retrieved);

        original[0] = 99;
        assertNotEquals(original[0], dto.getCoverImage()[0]);

        retrieved[1] = 88;
        assertNotEquals(retrieved[1], dto.getCoverImage()[1]);
    }

    /**
     * Tests that setting a {@code null} value for {@code coverImage}
     * results in an empty array being stored and retrieved.
     */
    @Test
    void testNullCoverImage() {
        BookDto dto = new BookDto();
        dto.setCoverImage(null);

        assertNotNull(dto.getCoverImage());
        assertEquals(0, dto.getCoverImage().length);
    }
}
