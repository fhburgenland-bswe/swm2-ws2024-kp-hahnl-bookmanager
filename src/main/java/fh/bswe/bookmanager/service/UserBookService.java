package fh.bswe.bookmanager.service;

import fh.bswe.bookmanager.dto.BookDto;
import fh.bswe.bookmanager.dto.UserBookDto;
import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.entity.UserBook;
import fh.bswe.bookmanager.exception.BookNotFoundException;
import fh.bswe.bookmanager.exception.UserBookExistsException;
import fh.bswe.bookmanager.exception.UserBookNotFoundException;
import fh.bswe.bookmanager.exception.UserNotFoundException;
import fh.bswe.bookmanager.repository.BookRepository;
import fh.bswe.bookmanager.repository.UserAccountRepository;
import fh.bswe.bookmanager.repository.UserBookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final BookRepository bookRepository;
    private final OpenLibraryService openLibraryService;

    /**
     * Constructs a new {@code UserBookService} with the given repositories and OpenLibrary service.
     * <p>
     * This constructor is used by Spring for dependency injection and initializes
     * the components required for managing the association between users and books.
     *
     * @param userBookRepository       the repository for managing {@link UserBook} entities
     * @param openLibraryService       the service for retrieving book data via the OpenLibrary API
     * @param bookRepository           the repository for accessing {@link Book} entities
     * @param userAccountRepository    the repository for accessing {@link UserAccount} entities
     */
    public UserBookService(final UserBookRepository userBookRepository,
                           final OpenLibraryService openLibraryService,
                           final BookRepository bookRepository,
                           final UserAccountRepository userAccountRepository) {
        this.userBookRepository = userBookRepository;
        this.openLibraryService = openLibraryService;
        this.bookRepository = bookRepository;
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

    /**
     * Removes a book from a user's personal library based on the given username and ISBN.
     * <p>
     * This method first verifies that the user exists, the book exists in the database,
     * and that the book is currently associated with the user's library. If any of these
     * checks fail, the corresponding exception is thrown.
     * </p>
     *
     * @param username the username of the user
     * @param isbn     the ISBN of the book to remove
     * @throws UserNotFoundException     if the user does not exist in the database
     * @throws BookNotFoundException     if the book does not exist in the database
     * @throws UserBookNotFoundException if the book is not associated with the user's library
     */
    public void removeBookFromUserLibrary(final String username, final String isbn) throws UserNotFoundException {
        final Optional<UserAccount> userAccount = userAccountRepository.findByUsername(username);

        if (userAccount.isEmpty()) {
            throw new UserNotFoundException();
        }

        final Optional<Book> book = bookRepository.findByIsbn(isbn);

        if (book.isEmpty()) {
            throw new BookNotFoundException("The book %s is not in the database.".formatted(isbn));
        }

        if (!userBookRepository.existsByUserAccountAndBook(userAccount.get(), book.get())) {
            throw new UserBookNotFoundException("The book %s is not in the user %s".formatted(isbn, username));
        }

        userBookRepository.deleteByUserAccountAndBook(userAccount.get(), book.get());
    }

    /**
     * Retrieves a list of books from the library of a specific user.
     * <p>
     * This method looks up the user by their username. If the user exists,
     * it retrieves all {@link UserBook} associations
     * and maps them to {@link UserBookDto} representations.
     * </p>
     *
     * @param username the username of the user whose book library is to be retrieved
     * @return a list of {@link UserBookDto} objects representing the user's books
     * @throws UserNotFoundException if no {@link UserAccount} with the given username exists
     */
    public List<UserBookDto> readUserBooks(final String username) throws UserNotFoundException {
        final Optional<UserAccount> userAccount = userAccountRepository.findByUsername(username);

        if (userAccount.isEmpty()) {
            throw new UserNotFoundException();
        }

        final List<UserBook> userBooks = userBookRepository.findByUserAccount(userAccount.get());

        return userBooks.stream().map(this::mapToDto).collect(Collectors.toList());
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

    private UserBookDto mapToDto(final UserBook userBook) {
        final UserBookDto bookDto = new UserBookDto();
        bookDto.setIsbn(userBook.getBook().getIsbn());
        bookDto.setTitle(userBook.getBook().getTitle());
        bookDto.setAuthor(userBook.getBook().getAuthors());
        bookDto.setRating(userBook.getRating());
        bookDto.setComment(userBook.getComment());
        return bookDto;
    }
}
