package fh.bswe.bookmanager.helper;

import fh.bswe.bookmanager.dto.BookDto;
import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.dto.UserBookDto;
import fh.bswe.bookmanager.entity.Book;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.entity.UserBook;

/**
 * Utility class for mapping between entity and DTO objects within the book management application.
 * <p>
 * Provides static methods to convert between {@link Book},
 * {@link UserBook}, {@link UserAccount}
 * and their corresponding DTO representations.
 * </p>
 */
public class Mapper {

    /**
     * Maps a {@link Book} entity to a {@link BookDto}.
     *
     * @param book the Book entity to map
     * @return the mapped BookDto
     */
    public static BookDto mapToDto(final Book book) {
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

    /**
     * Maps a {@link UserBook} entity to a {@link UserBookDto}.
     * <p>
     * This includes extracting information from the associated {@link Book}.
     * </p>
     *
     * @param userBook the UserBook entity to map
     * @return the mapped UserBookDto
     */
    public static UserBookDto mapToDto(final UserBook userBook) {
        final UserBookDto bookDto = new UserBookDto();
        bookDto.setIsbn(userBook.getBook().getIsbn());
        bookDto.setTitle(userBook.getBook().getTitle());
        bookDto.setAuthor(userBook.getBook().getAuthors());
        bookDto.setRating(userBook.getRating());
        bookDto.setComment(userBook.getComment());
        return bookDto;
    }

    /**
     * Maps a {@link UserAccountDto} to a {@link UserAccount} entity.
     * <p>
     * Used for saving new or updated user information to the database.
     * </p>
     *
     * @param userAccountDto the DTO containing user input data
     * @return the mapped UserAccount entity
     */
    public static UserAccount mapToEntity(final UserAccountDto userAccountDto) {
        final UserAccount userAccount = new UserAccount();
        userAccount.setUsername(userAccountDto.getUsername());
        userAccount.setFirstname(userAccountDto.getFirstname());
        userAccount.setLastname(userAccountDto.getLastname());
        return userAccount;
    }

    /**
     * Maps a {@link UserAccount} entity to a {@link UserAccountDto}.
     * <p>
     * Used for returning user data in API responses.
     * </p>
     *
     * @param user the UserAccount entity to map
     * @return the mapped UserAccountDto
     */
    public static UserAccountDto mapToDto(final UserAccount user) {
        final UserAccountDto dto = new UserAccountDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        return dto;
    }
}
