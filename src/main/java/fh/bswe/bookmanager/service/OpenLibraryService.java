package fh.bswe.bookmanager.service;

import fh.bswe.bookmanager.dto.BookDto;
import fh.bswe.bookmanager.dto.OpenLibraryAuthorDto;
import fh.bswe.bookmanager.dto.OpenLibraryBookDto;
import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.exception.BookNotFoundException;
import fh.bswe.bookmanager.helper.Mapper;
import fh.bswe.bookmanager.helper.OpenLibraryFetcher;
import fh.bswe.bookmanager.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class that integrates with the Open Library API to fetch book data by ISBN
 * and store it in the database if not already present.
 */
@Service
public class OpenLibraryService {
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final OpenLibraryFetcher openLibraryFetcher;
    private final Logger logger = LoggerFactory.getLogger(OpenLibraryService.class);

    /**
     * Constructs a new {@code OpenLibraryService} with the given repository.
     *
     * @param bookService the book service for interaction with the book database
     * @param openLibraryFetcher the OpenLibrary helper for using its API
     */
    public OpenLibraryService(final BookService bookService, final BookRepository bookRepository, final OpenLibraryFetcher openLibraryFetcher) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.openLibraryFetcher = openLibraryFetcher;
    }

    /**
     * Finds a book by ISBN. If the book is not found in the database,
     * it attempts to fetch the book data and cover from the Open Library API and stores it.
     *
     * @param isbn the ISBN of the book to look up
     * @return the found or newly stored Book entity
     */
    public Book findAndStoreBookByIsbn(final String isbn) {
        try {
            return bookService.findBookByIsbn(isbn);
        } catch (BookNotFoundException e) {
            logger.error("BookService: {}", e.getMessage());
        }

        final OpenLibraryBookDto bookDto = openLibraryFetcher.fetchBook(isbn);
        byte[] image = new byte[0];

        try {
            image = openLibraryFetcher.fetchCover(isbn);
        } catch (Exception e) {
            logger.error("Fetch Cover: {}", e.getMessage());
        }

        final Book book = mapToEntity(bookDto, fetchAllAuthors(bookDto), image);

        return bookRepository.save(book);
    }

    /**
     * Finds a book by ISBN. If the book is not found in the database,
     * it attempts to fetch the book data and cover from the Open Library API and stores it.
     *
     * @param isbn the ISBN of the book to look up
     * @return the found or newly stored Book DTO
     */
    public BookDto findAndStoreBookByIsbnToDto(final String isbn) {
        return Mapper.mapToDto(findAndStoreBookByIsbn(isbn));
    }

    private Book mapToEntity(final OpenLibraryBookDto bookDto, final String authors, final byte[] image) {
        final Book book = new Book();

        if (bookDto.getIsbn_13() != null && !bookDto.getIsbn_13().isEmpty()) {
            book.setIsbn(bookDto.getIsbn_13().getFirst());
        } else {
            book.setIsbn(bookDto.getIsbn_10().getFirst());
        }

        book.setTitle(bookDto.getTitle());
        book.setAuthors(authors);
        book.setLanguage(extractLanguage(bookDto));
        book.setPublishDate(bookDto.getPublish_date());
        book.setPublishers(extractPublishers(bookDto));
        book.setCoverKey(extractCoverKey(bookDto));
        book.setCoverLink(generateCoverLink(book.getCoverKey()));
        book.setCoverImage(image);

        return book;
    }

    private String fetchAllAuthors(final OpenLibraryBookDto bookDto) {
        final List<String> authors = new ArrayList<>();

        if (bookDto.getAuthors() != null && !bookDto.getAuthors().isEmpty()) {
            for (final OpenLibraryBookDto.Author author : bookDto.getAuthors()) {
                if (author.getKey() != null && author.getKey().contains("/")) {
                    final String authorKey = author.getKey()
                            .substring(author
                                    .getKey()
                                    .lastIndexOf('/') + 1);

                    authors.add(fetchAuthor(authorKey));
                }
            }

            authors.removeIf(String::isEmpty);
            if (!authors.isEmpty()) {
                return String.join(", ", authors);
            }
        }

        return "";
    }

    private String fetchAuthor(final String authorKey) {
        try {
            final OpenLibraryAuthorDto authorDto = openLibraryFetcher.fetchAuthor(authorKey);
            return authorDto.getName();
        } catch (Exception e) {
            logger.error("Fetch Author: {}", e.getMessage());
        }

        return "";
    }

    private String extractLanguage(final OpenLibraryBookDto bookDto) {
        if (bookDto.getLanguages() != null && !bookDto.getLanguages().isEmpty()) {
            final String langKey = bookDto.getLanguages().getFirst().getKey();
            if (langKey.contains("/")) {
                return langKey.substring(langKey.lastIndexOf('/') + 1);
            }
        }

        return "";
    }

    private String extractPublishers(final OpenLibraryBookDto bookDto) {
        if (bookDto.getPublishers() != null && !bookDto.getPublishers().isEmpty()) {
            return String.join(", ", bookDto.getPublishers());
        }

        return "";
    }

    private String extractCoverKey(final OpenLibraryBookDto bookDto) {
        if (bookDto.getCovers() != null && !bookDto.getCovers().isEmpty()) {
            return String.valueOf(bookDto.getCovers().getFirst());
        }

        return "";
    }

    private String generateCoverLink(final String coverKey) {
        if (!coverKey.isEmpty()) {
            return "https://covers.openlibrary.org/b/id/" + coverKey + "-L.jpg";
        }

        return "";
    }
}
