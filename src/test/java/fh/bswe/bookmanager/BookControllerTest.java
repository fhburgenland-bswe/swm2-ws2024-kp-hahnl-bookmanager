package fh.bswe.bookmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import fh.bswe.bookmanager.controller.BookController;
import fh.bswe.bookmanager.dto.BookDto;
import fh.bswe.bookmanager.exception.BookNotFoundException;
import fh.bswe.bookmanager.exception.ConnectionErrorException;
import fh.bswe.bookmanager.service.BookService;
import fh.bswe.bookmanager.service.OpenLibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the {@link BookController}, focusing on HTTP endpoint behavior.
 * <p>
 * The tests are performed using Spring's {@link WebMvcTest} setup and {@link MockMvc}
 * to simulate HTTP requests and responses.
 * </p>
 */
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OpenLibraryService openLibraryService;

    /**
     * Tests successful read book details with valid input.
     * Expects HTTP 200 OK and returns the book data in JSON format.
     */
    @Test
    void testReadBookDetails() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1);
        bookDto.setPublishers("Publishers");
        bookDto.setPublishDate("2020");
        bookDto.setIsbn("0123456789");
        bookDto.setTitle("Test Book");
        bookDto.setAuthors("Authors");
        bookDto.setCoverKey("Cover Key");
        bookDto.setCoverLink("Cover Link");
        bookDto.setCoverImage(new byte[0]);
        bookDto.setLanguage("eng");

        when(openLibraryService.findAndStoreBookByIsbnToDto("0123456789")).thenReturn(bookDto);

        mockMvc.perform(get("/api/books/0123456789"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn").value("0123456789"))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.authors").value("Authors"))
                .andExpect(jsonPath("$.coverKey").value("Cover Key"))
                .andExpect(jsonPath("$.coverLink").value("Cover Link"))
                .andExpect(jsonPath("$.language").value("eng"))
                .andExpect(jsonPath("$.publishDate").value("2020"))
                .andExpect(jsonPath("$.publishers").value("Publishers"));

        verify(openLibraryService, times(1)).findAndStoreBookByIsbnToDto("0123456789");
    }

    /**
     * Tests behavior when the specified user is not found.
     * <p>
     * Expects HTTP 400 Bad Request with an appropriate error message.
     * </p>
     *
     * @throws Exception if the request fails
     */
    @Test
    void testReadUserBooksLibraryUserNotFound() throws Exception {
        when(openLibraryService.findAndStoreBookByIsbnToDto("0123456789")).thenThrow(new BookNotFoundException("Book not found"));

        mockMvc.perform(get("/api/books/0123456789"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests validation failure due to invalid isbn format.
     * <p>
     * Expects HTTP 422 Unprocessable Entity with a validation error message.
     * </p>
     *
     * @throws Exception if the request fails
     */
    @Test
    void testReadUserBooksLibraryInvalidIsbn() throws Exception {
        mockMvc.perform(get("/api/books/0123456789-"))
                .andExpect(status().isUnprocessableEntity());
    }

    /**
     * Tests validation failure due to invalid isbn format.
     * <p>
     * Expects HTTP 422 Unprocessable Entity with a validation error message.
     * </p>
     *
     * @throws Exception if the request fails
     */
    @Test
    void testReadUserBooksLibraryConnectionError() throws Exception {
        when(openLibraryService.findAndStoreBookByIsbnToDto("0123456789")).thenThrow(new ConnectionErrorException("error"));

        mockMvc.perform(get("/api/books/0123456789"))
                .andExpect(status().isInternalServerError());
    }
}
