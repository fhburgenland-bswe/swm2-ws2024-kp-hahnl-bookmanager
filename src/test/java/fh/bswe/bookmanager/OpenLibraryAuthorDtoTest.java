package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.OpenLibraryAuthorDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenLibraryAuthorDtoTest {

    /**
     * Tests setting and getting the author name.
     */
    @Test
    void testSetAndGetName() {
        OpenLibraryAuthorDto dto = new OpenLibraryAuthorDto();
        dto.setName("Jack Thorne");
        assertEquals("Jack Thorne", dto.getName());
    }

    /**
     * Tests setting and getting the author key.
     */
    @Test
    void testSetAndGetKey() {
        OpenLibraryAuthorDto dto = new OpenLibraryAuthorDto();
        dto.setKey("/authors/OL10259603A");
        assertEquals("/authors/OL10259603A", dto.getKey());
    }

    /**
     * Tests setting and getting the type key.
     */
    @Test
    void testSetAndGetType() {
        OpenLibraryAuthorDto.Type type = new OpenLibraryAuthorDto.Type();
        type.setKey("/type/author");

        OpenLibraryAuthorDto dto = new OpenLibraryAuthorDto();
        dto.setType(type);

        assertEquals("/type/author", dto.getType().getKey());
    }

    /**
     * Tests setting and getting source records.
     */
    @Test
    void testSetAndGetSourceRecords() {
        OpenLibraryAuthorDto dto = new OpenLibraryAuthorDto();
        dto.setSource_records(List.of("bwb:9781780019956"));
        assertEquals("bwb:9781780019956", dto.getSource_records().get(0));
    }

    /**
     * Tests setting and getting latest revision.
     */
    @Test
    void testSetAndGetLatestRevision() {
        OpenLibraryAuthorDto dto = new OpenLibraryAuthorDto();
        dto.setLatest_revision(1);
        assertEquals(1, dto.getLatest_revision());
    }

    /**
     * Tests setting and getting revision.
     */
    @Test
    void testSetAndGetRevision() {
        OpenLibraryAuthorDto dto = new OpenLibraryAuthorDto();
        dto.setRevision(1);
        assertEquals(1, dto.getRevision());
    }

    /**
     * Tests setting and getting created metadata.
     */
    @Test
    void testSetAndGetCreated() {
        OpenLibraryAuthorDto.Created created = new OpenLibraryAuthorDto.Created();
        created.setType("/type/datetime");
        created.setValue("2022-02-27T20:39:03.235199");

        OpenLibraryAuthorDto dto = new OpenLibraryAuthorDto();
        dto.setCreated(created);

        assertEquals("/type/datetime", dto.getCreated().getType());
        assertEquals("2022-02-27T20:39:03.235199", dto.getCreated().getValue());
    }

    /**
     * Tests setting and getting last modified metadata.
     */
    @Test
    void testSetAndGetLastModified() {
        OpenLibraryAuthorDto.LastModified lastModified = new OpenLibraryAuthorDto.LastModified();
        lastModified.setType("/type/datetime");
        lastModified.setValue("2022-02-27T20:39:03.235199");

        OpenLibraryAuthorDto dto = new OpenLibraryAuthorDto();
        dto.setLast_modified(lastModified);

        assertEquals("/type/datetime", dto.getLast_modified().getType());
        assertEquals("2022-02-27T20:39:03.235199", dto.getLast_modified().getValue());
    }
}
