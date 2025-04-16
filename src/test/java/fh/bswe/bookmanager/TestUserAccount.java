package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.entity.UserAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestUserAccount {

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

        Assertions.assertEquals(userAccount.hashCode(), userAccount2.hashCode());
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
