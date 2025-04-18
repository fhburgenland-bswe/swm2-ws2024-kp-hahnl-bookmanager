package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.entity.UserAccount;
import fh.bswe.bookmanager.entity.UserBook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Unit tests for the {@link UserAccount} entity.
 * These tests verify the correctness of getters, setters, and equality logic.
 */
public class UserAccountTest {

    /**
     * Verifies that the ID getter and setter work as expected.
     */
    @Test
    void testSetAndGetId() {
        UserAccount user = new UserAccount();
        user.setId(42);
        assertEquals(42, user.getId());
    }

    /**
     * Verifies that the username getter and setter work as expected.
     */
    @Test
    void testSetAndGetUsername() {
        UserAccount user = new UserAccount();
        user.setUsername("johndoe");
        assertEquals("johndoe", user.getUsername());
    }

    /**
     * Verifies that the firstname getter and setter work as expected.
     */
    @Test
    void testSetAndGetFirstname() {
        UserAccount user = new UserAccount();
        user.setFirstname("John");
        assertEquals("John", user.getFirstname());
    }

    /**
     * Verifies that the lastname getter and setter work as expected.
     */
    @Test
    void testSetAndGetLastname() {
        UserAccount user = new UserAccount();
        user.setLastname("Doe");
        assertEquals("Doe", user.getLastname());
    }

    /**
     * Verifies that the list of userBooks can be set and retrieved properly.
     */
    @Test
    void testSetAndGetUserBooks() {
        UserAccount user = new UserAccount();
        UserBook userBook = new UserBook();
        user.setUserBooks(List.of(userBook));
        assertEquals(1, user.getUserBooks().size());
        assertSame(userBook, user.getUserBooks().get(0));
    }

    /**
     * Tests that two equal {@link UserAccount} instances produce the same hash code.
     */
    @Test
    public void testUserAccountHashCode() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(1);
        userAccount.setFirstname("John");
        userAccount.setLastname("Smith");

        UserAccount userAccount2 = new UserAccount();
        userAccount2.setId(1);
        userAccount2.setFirstname("John");
        userAccount2.setLastname("Smith");

        assertEquals(userAccount.hashCode(), userAccount2.hashCode());
    }

    /**
     * Tests that two {@link UserAccount} instances with identical data are equal.
     */
    @Test
    public void testUserAccountEquals() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(1);
        userAccount.setFirstname("John");
        userAccount.setLastname("Smith");

        UserAccount userAccount2 = new UserAccount();
        userAccount2.setId(1);
        userAccount2.setFirstname("John");
        userAccount2.setLastname("Smith");

        Assertions.assertTrue(userAccount.equals(userAccount2));
    }

    /**
     * Tests that a {@link UserAccount} is not equal to {@code null}.
     */
    @Test
    public void testUserAccountEqualsNull() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(1);
        userAccount.setFirstname("John");
        userAccount.setLastname("Smith");

        Assertions.assertFalse(userAccount.equals(null));
    }

    /**
     * Tests that two {@link UserAccount} instances with different lastname values are not equal.
     */
    @Test
    public void testUserAccountEqualsFalse() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(1);
        userAccount.setFirstname("John");
        userAccount.setLastname("Smith");

        UserAccount userAccount2 = new UserAccount();
        userAccount2.setId(2);
        userAccount2.setFirstname("John");
        userAccount2.setLastname("Huber");

        Assertions.assertFalse(userAccount.equals(userAccount2));
    }

    /**
     * Tests that a {@link UserAccount} is not equal to an object of a different class.
     */
    @Test
    public void testUserAccountEqualsWrongClass() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(1);
        userAccount.setFirstname("John");
        userAccount.setLastname("Smith");

        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setId(2);
        userAccountDto.setFirstname("John");
        userAccountDto.setLastname("Huber");

        Assertions.assertFalse(userAccount.equals(userAccountDto));
    }
}
