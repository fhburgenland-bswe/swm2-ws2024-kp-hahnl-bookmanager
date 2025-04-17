package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.OpenLibraryBookDto;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link OpenLibraryBookDto} class.
 * <p>
 * These tests verify the correct behavior of all getter and setter methods,
 * including nested DTO classes such as {@code Work}, {@code Type},
 * {@code Language}, {@code Created}, and {@code LastModified}.
 */
public class OpenLibraryBookDtoTest {
    /**
     * Tests setting and retrieving the book title.
     */
    @Test
    void testSetAndGetTitle() {
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setTitle("Inferno");
        assertEquals("Inferno", dto.getTitle());
    }

    /**
     * Tests setting and retrieving the list of works.
     */
    @Test
    void testSetAndGetWorks() {
        OpenLibraryBookDto.Work work = new OpenLibraryBookDto.Work();
        work.setKey("/works/OL123");
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setWorks(List.of(work));
        assertEquals("/works/OL123", dto.getWorks().getFirst().getKey());
    }

    /**
     * Tests setting and retrieving the list of publishers.
     */
    @Test
    void testSetAndGetPublishers() {
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setPublishers(List.of("Anchor Books"));
        assertEquals("Anchor Books", dto.getPublishers().getFirst());
    }

    /**
     * Tests setting and retrieving the publication date.
     */
    @Test
    void testSetAndGetPublishDate() {
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setPublish_date("2016");
        assertEquals("2016", dto.getPublish_date());
    }

    /**
     * Tests setting and retrieving the unique book key.
     */
    @Test
    void testSetAndGetKey() {
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setKey("/books/OL123");
        assertEquals("/books/OL123", dto.getKey());
    }

    /**
     * Tests setting and retrieving the book type.
     */
    @Test
    void testSetAndGetType() {
        OpenLibraryBookDto.Type type = new OpenLibraryBookDto.Type();
        type.setKey("/type/edition");
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setType(type);
        assertEquals("/type/edition", dto.getType().getKey());
    }

    /**
     * Tests setting and retrieving the identifier map.
     */
    @Test
    void testSetAndGetIdentifiers() {
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setIdentifiers(Map.of("isbn_10", List.of("1234567890")));
        assertTrue(dto.getIdentifiers().containsKey("isbn_10"));
    }

    /**
     * Tests setting and retrieving the cover image list.
     */
    @Test
    void testSetAndGetCovers() {
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setCovers(List.of(12345));
        assertEquals(12345, dto.getCovers().getFirst());
    }

    /**
     * Tests setting and retrieving the OCA ID.
     */
    @Test
    void testSetAndGetOcaid() {
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setOcaid("ocaid-test");
        assertEquals("ocaid-test", dto.getOcaid());
    }

    /**
     * Tests setting and retrieving the list of ISBN-13 numbers.
     */
    @Test
    void testSetAndGetIsbn13() {
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setIsbn_13(List.of("9781234567897"));
        assertEquals("9781234567897", dto.getIsbn_13().getFirst());
    }

    /**
     * Tests setting and retrieving the list of ISBN-10 numbers.
     */
    @Test
    void testSetAndGetIsbn10() {
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setIsbn_10(List.of("1234567890"));
        assertEquals("1234567890", dto.getIsbn_10().getFirst());
    }

    /**
     * Tests setting and retrieving the classification map.
     */
    @Test
    void testSetAndGetClassifications() {
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setClassifications(Map.of("dewey_decimal", "813/.54"));
        assertEquals("813/.54", dto.getClassifications().get("dewey_decimal"));
    }

    /**
     * Tests setting and retrieving the language list.
     */
    @Test
    void testSetAndGetLanguages() {
        OpenLibraryBookDto.Language language = new OpenLibraryBookDto.Language();
        language.setKey("/languages/eng");
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setLanguages(List.of(language));
        assertEquals("/languages/eng", dto.getLanguages().getFirst().getKey());
    }

    /**
     * Tests setting and retrieving the latest revision number.
     */
    @Test
    void testSetAndGetLatestRevision() {
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setLatest_revision(5);
        assertEquals(5, dto.getLatest_revision());
    }

    /**
     * Tests setting and retrieving the current revision number.
     */
    @Test
    void testSetAndGetRevision() {
        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setRevision(3);
        assertEquals(3, dto.getRevision());
    }

    /**
     * Tests setting and retrieving the creation metadata.
     */
    @Test
    void testSetAndGetCreated() {
        OpenLibraryBookDto.Created created = new OpenLibraryBookDto.Created();
        created.setType("datetime");
        created.setValue("2023-01-01T12:00:00Z");

        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setCreated(created);

        assertEquals("datetime", dto.getCreated().getType());
        assertEquals("2023-01-01T12:00:00Z", dto.getCreated().getValue());
    }

    /**
     * Tests setting and retrieving the last modified metadata.
     */
    @Test
    void testSetAndGetLastModified() {
        OpenLibraryBookDto.LastModified lastModified = new OpenLibraryBookDto.LastModified();
        lastModified.setType("datetime");
        lastModified.setValue("2024-01-01T12:00:00Z");

        OpenLibraryBookDto dto = new OpenLibraryBookDto();
        dto.setLast_modified(lastModified);

        assertEquals("datetime", dto.getLast_modified().getType());
        assertEquals("2024-01-01T12:00:00Z", dto.getLast_modified().getValue());
    }
}
