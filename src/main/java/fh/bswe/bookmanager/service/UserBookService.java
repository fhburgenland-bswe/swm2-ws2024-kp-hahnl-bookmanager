package fh.bswe.bookmanager.service;

import fh.bswe.bookmanager.dto.BookDto;
import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.entity.UserBook;
import fh.bswe.bookmanager.exception.UserBookExistsException;
import fh.bswe.bookmanager.exception.UserNotFoundException;
import fh.bswe.bookmanager.repository.UserAccountRepository;
import fh.bswe.bookmanager.repository.UserBookRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing the association between users and books.
 * <p>
 * Handles operations for storing a book in a user's library by checking user existence,
 * avoiding duplicates, and fetching book data via the OpenLibrary API.
 */
@Service
public class UserBookService {
    private final UserBookRepository userBookRepository;
    private final UserAccountRepository userAccountRepository;
    private final OpenLibraryService openLibraryService;

    /**
     * Constructs a new {@code UserBookService} with the given repositories and OpenLibrary service.
     * <p>
     * This constructor is used by Spring for dependency injection and initializes
     * the components required for managing the association between users and books.
     *
     * @param userBookRepository       the repository for managing {@link UserBook} entities
     * @param openLibraryService       the service for retrieving book data via the OpenLibrary API
     * @param userAccountRepository    the repository for accessing {@link UserAccount} entities
     */
    public UserBookService(final UserBookRepository userBookRepository,
                           final OpenLibraryService openLibraryService,
                           final UserAccountRepository userAccountRepository) {
        this.userBookRepository = userBookRepository;
        this.openLibraryService = openLibraryService;
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * Associates a book with a user's library based on username and ISBN.
     * <p>
     * If the user does not exist, a {@link UserNotFoundException} is thrown.
     * If the user already has this book, a {@link UserBookExistsException} is thrown.
     *
     * @param username the username of the user
     * @param isbn     the ISBN of the book to be added
     * @return the stored book as {@link BookDto}
     * @throws UserNotFoundException   if the user could not be found
     * @throws UserBookExistsException if the book is already stored for the user
     */
    public BookDto storeBookToUserLibrary(final String username, final String isbn) throws UserNotFoundException {
        final Optional<UserAccount> userAccount = userAccountRepository.findByUsername(username);

        if (userAccount.isEmpty()) {
            throw new UserNotFoundException();
        }

        final Book book = openLibraryService.findAndStoreBookByIsbn(isbn);

        if (userBookRepository.existsByUserAccountAndBook(userAccount.get(), book)) {
            throw new UserBookExistsException("The book %s was already added to user %s".formatted(isbn, username));
        }

        final UserBook userBook = new UserBook();
        userBook.setBook(book);
        userBook.setUser(userAccount.get());

        userBookRepository.save(userBook);

        return mapToDto(book);
    }

    private BookDto mapToDto(final Book book) {
        final BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthors(book.getAuthors());
        bookDto.setLanguage(book.getLanguage());
        bookDto.setPublishDate(book.getPublishDate());
        bookDto.setPublishers(book.getPublishers());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCoverKey(book.getCoverKey());
        bookDto.setCoverLink(book.getCoverLink());
        return bookDto;
    }
}
