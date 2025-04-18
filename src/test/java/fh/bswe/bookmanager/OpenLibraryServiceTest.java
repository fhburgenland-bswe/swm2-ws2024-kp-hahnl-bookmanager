package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.OpenLibraryAuthorDto;
import fh.bswe.bookmanager.dto.OpenLibraryBookDto;
import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.exception.BookNotFoundException;
import fh.bswe.bookmanager.exception.CoverNotFoundException;
import fh.bswe.bookmanager.helper.OpenLibraryFetcher;
import fh.bswe.bookmanager.repository.BookRepository;
import fh.bswe.bookmanager.service.BookService;
import fh.bswe.bookmanager.service.OpenLibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link OpenLibraryService} class.
 */
@SpringBootTest
public class OpenLibraryServiceTest {

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private OpenLibraryFetcher openLibraryFetcher;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    /**
     * Tests that an existing book is returned without fetching from Open Library.
     */
    @Test
    void shouldReturnExistingBookWhenFoundInDatabase() {
        Book mockBook = new Book();
        mockBook.setIsbn("1234567890");

        when(bookService.findBookByIsbn("1234567890")).thenReturn(mockBook);

        Book result = openLibraryService.findAndStoreBookByIsbn("1234567890");

        assertEquals(mockBook, result);
        verify(bookService).findBookByIsbn("1234567890");
        verifyNoInteractions(openLibraryFetcher);
    }

    /**
     * Tests that a book is fetched and saved when not found in the database.
     * Uses ISBN-10, null values for publishers and languages, and expects empty author name.
     */
    @Test
    void shouldFetchAndSaveBookTestPattern1() {
        OpenLibraryBookDto.Author author = new OpenLibraryBookDto.Author();
        author.setKey("/authors/OL1A");

        when(bookService.findBookByIsbn("1234567890")).thenThrow(new BookNotFoundException("Not found"));

        OpenLibraryBookDto bookDto = new OpenLibraryBookDto();
        bookDto.setTitle("Test Book");
        bookDto.setIsbn_10(List.of("1234567890"));
        bookDto.setAuthors(List.of(author));
        bookDto.setLanguages(null);
        bookDto.setPublish_date("2020");
        bookDto.setPublishers(null);
        bookDto.setCovers(null);

        OpenLibraryAuthorDto authorDto = new OpenLibraryAuthorDto();
        authorDto.setName("Test Author");

        when(openLibraryFetcher.fetchBook("1234567890")).thenReturn(bookDto);
        when(openLibraryFetcher.fetchAuthor("OL1A")).thenReturn(null);
        when(openLibraryFetcher.fetchCover("1234567890")).thenReturn(new byte[]{1, 2, 3});

        Book savedBook = openLibraryService.findAndStoreBookByIsbn("1234567890");

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("", savedBook.getAuthors());
        assertEquals("1234567890", savedBook.getIsbn());
        assertEquals("2020", savedBook.getPublishDate());
        assertEquals("", savedBook.getLanguage());
        assertEquals("", savedBook.getPublishers());
        assertEquals("", savedBook.getCoverKey());
        assertArrayEquals(new byte[]{1, 2, 3}, savedBook.getCoverImage());
    }

    /**
     * Tests that a book is fetched and saved when:
     * - ISBN-13 list is empty and fallback to ISBN-10
     * - authors are null
     * - languages, publishers, and covers are empty lists
     * - fetching the cover and author fails with exceptions
     */
    @Test
    void shouldFetchAndSaveBookTestPattern2() {
        when(bookService.findBookByIsbn("1234567890")).thenThrow(new BookNotFoundException("Not found"));

        OpenLibraryBookDto bookDto = new OpenLibraryBookDto();
        bookDto.setTitle("Test Book");
        bookDto.setIsbn_13(List.of());
        bookDto.setIsbn_10(List.of("1234567890"));
        bookDto.setAuthors(null);
        bookDto.setLanguages(List.of());
        bookDto.setPublish_date("2020");
        bookDto.setPublishers(List.of());
        bookDto.setCovers(List.of());

        OpenLibraryAuthorDto authorDto = new OpenLibraryAuthorDto();
        authorDto.setName("Test Author");

        when(openLibraryFetcher.fetchBook("1234567890")).thenReturn(bookDto);
        when(openLibraryFetcher.fetchCover("1234567890")).thenThrow(new CoverNotFoundException("Cover not found"));
        when(openLibraryFetcher.fetchAuthor("OL1A")).thenThrow(new CoverNotFoundException("Cover not found"));

        Book savedBook = openLibraryService.findAndStoreBookByIsbn("1234567890");

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("", savedBook.getAuthors());
        assertEquals("1234567890", savedBook.getIsbn());
        assertEquals("2020", savedBook.getPublishDate());
        assertEquals("", savedBook.getLanguage());
        assertEquals("", savedBook.getPublishers());
        assertEquals("", savedBook.getCoverKey());
        assertArrayEquals(new byte[0], savedBook.getCoverImage());
    }

    /**
     * Tests that a book is fetched and saved when:
     * - authors list is empty
     * - language is set with valid OpenLibrary path
     * - publishers and covers are null
     * - cover image fetch succeeds
     */
    @Test
    void shouldFetchAndSaveBookTestPattern3() {
        OpenLibraryBookDto.Language language = new OpenLibraryBookDto.Language();
        language.setKey("/languages/eng");

        when(bookService.findBookByIsbn("1234567890")).thenThrow(new BookNotFoundException("Not found"));

        OpenLibraryBookDto bookDto = new OpenLibraryBookDto();
        bookDto.setTitle("Test Book");
        bookDto.setIsbn_13(List.of("1234567890"));
        bookDto.setAuthors(List.of());
        bookDto.setLanguages(List.of(language));
        bookDto.setPublish_date("2020");
        bookDto.setPublishers(null);
        bookDto.setCovers(null);

        when(openLibraryFetcher.fetchBook("1234567890")).thenReturn(bookDto);
        when(openLibraryFetcher.fetchAuthor("OL1A")).thenReturn(null);
        when(openLibraryFetcher.fetchCover("1234567890")).thenReturn(new byte[]{1, 2, 3});

        Book savedBook = openLibraryService.findAndStoreBookByIsbn("1234567890");

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("", savedBook.getAuthors());
        assertEquals("1234567890", savedBook.getIsbn());
        assertEquals("2020", savedBook.getPublishDate());
        assertEquals("eng", savedBook.getLanguage());
        assertEquals("", savedBook.getPublishers());
        assertEquals("", savedBook.getCoverKey());
        assertArrayEquals(new byte[]{1, 2, 3}, savedBook.getCoverImage());
    }

    /**
     * Tests that a book is fetched and saved when:
     * - author and language keys do not contain '/'
     * - authors list is empty
     * - no valid language code can be extracted
     * - publishers and covers are null
     */
    @Test
    void shouldFetchAndSaveBookTestPattern4() {
        OpenLibraryBookDto.Author author = new OpenLibraryBookDto.Author();
        author.setKey("authorsOL1A");

        OpenLibraryBookDto.Language language = new OpenLibraryBookDto.Language();
        language.setKey("languageseng");

        when(bookService.findBookByIsbn("1234567890")).thenThrow(new BookNotFoundException("Not found"));

        OpenLibraryBookDto bookDto = new OpenLibraryBookDto();
        bookDto.setTitle("Test Book");
        bookDto.setIsbn_13(List.of("1234567890"));
        bookDto.setAuthors(List.of(author));
        bookDto.setLanguages(List.of(language));
        bookDto.setPublish_date("2020");
        bookDto.setPublishers(null);
        bookDto.setCovers(null);

        when(openLibraryFetcher.fetchBook("1234567890")).thenReturn(bookDto);
        when(openLibraryFetcher.fetchAuthor("OL1A")).thenReturn(null);
        when(openLibraryFetcher.fetchCover("1234567890")).thenReturn(new byte[]{1, 2, 3});

        Book savedBook = openLibraryService.findAndStoreBookByIsbn("1234567890");

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("", savedBook.getAuthors());
        assertEquals("1234567890", savedBook.getIsbn());
        assertEquals("2020", savedBook.getPublishDate());
        assertEquals("", savedBook.getLanguage());
        assertEquals("", savedBook.getPublishers());
        assertEquals("", savedBook.getCoverKey());
        assertArrayEquals(new byte[]{1, 2, 3}, savedBook.getCoverImage());
    }
}
