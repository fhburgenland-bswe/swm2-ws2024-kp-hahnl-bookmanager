package fh.bswe.bookmanager.controller;

import fh.bswe.bookmanager.dto.BookDto;
import fh.bswe.bookmanager.exception.BookNotFoundException;
import fh.bswe.bookmanager.service.OpenLibraryService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing books.
 * <p>
 * This controller handles incoming HTTP requests related to books.
 * </p>
 */
@SuppressWarnings({
        "PMD.AvoidDuplicateLiterals"
})
@Validated
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final OpenLibraryService openLibraryService;

    /**
     * Constructs a new {@code BookController} with the given service.
     *
     * @param openLibraryService   the service used for open library operations
     */
    public BookController(final OpenLibraryService openLibraryService) {
        this.openLibraryService = openLibraryService;
    }

    /**
     * Retrieves detailed information about a book by its ISBN.
     * <p>
     * This endpoint attempts to fetch book metadata from the OpenLibrary API and store it
     * in the application's database (if not already present). The result is returned as a
     * {@link BookDto} object in JSON format.
     * </p>
     *
     * @param isbn the ISBN of the book to be retrieved. Must be 10 to 13 digits long
     *             and contain only digits.
     * @return {@link ResponseEntity} containing the {@link BookDto} and HTTP status:
     *         <ul>
     *             <li>{@code 200 OK} if the book was found successfully</li>
     *             <li>{@code 400 BAD_REQUEST} if the book was not found or ISBN is invalid</li>
     *             <li>{@code 500 INTERNAL_SERVER_ERROR} for unexpected errors</li>
     *         </ul>
     */
    @GetMapping("/{isbn}")
    public ResponseEntity<?> readBookDetails(
            @NotBlank
            @PathVariable("isbn")
            @Size(min = 10, max = 13, message = "The length must be between 10 and 13 digits")
            @Pattern(regexp = "^[0-9]{10,13}$", message = "ISBN must be 10 or 13 digits and contain only digits")
            final String isbn) {
        try {
            final BookDto bookDto = openLibraryService.findAndStoreBookByIsbnToDto(isbn);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
