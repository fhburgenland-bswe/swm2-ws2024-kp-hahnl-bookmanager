package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.dto.UserAccountUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserAccountUpdateDtoTest {

    /**
     * Tests that two equal {@link UserAccountUpdateDto} instances produce the same hash code.
     */
    @Test
    public void testUserAccountUpdateDtoHashCode() {
        UserAccountUpdateDto userAccountUpdateDto = new UserAccountUpdateDto();
        userAccountUpdateDto.setFirstname("John");
        userAccountUpdateDto.setLastname("Smith");

        UserAccountUpdateDto userAccountUpdateDto2 = new UserAccountUpdateDto();
        userAccountUpdateDto2.setFirstname("John");
        userAccountUpdateDto2.setLastname("Smith");

        Assertions.assertEquals(userAccountUpdateDto.hashCode(), userAccountUpdateDto2.hashCode());
    }

    /**
     * Tests that two {@link UserAccountUpdateDto} instances with identical data are equal.
     */
    @Test
    public void testUserAccountUpdateDtoEquals() {
        UserAccountUpdateDto userAccountUpdateDto = new UserAccountUpdateDto();
        userAccountUpdateDto.setFirstname("John");
        userAccountUpdateDto.setLastname("Smith");

        UserAccountUpdateDto userAccountUpdateDto2 = new UserAccountUpdateDto();
        userAccountUpdateDto2.setFirstname("John");
        userAccountUpdateDto2.setLastname("Smith");

        Assertions.assertTrue(userAccountUpdateDto.equals(userAccountUpdateDto2));
    }

    /**
     * Tests that a {@link UserAccountUpdateDto} is not equal to {@code null}.
     */
    @Test
    public void testUserAccountUpdateDtoEqualsNull() {
        UserAccountUpdateDto userAccountUpdateDto = new UserAccountUpdateDto();
        userAccountUpdateDto.setFirstname("John");
        userAccountUpdateDto.setLastname("Smith");

        Assertions.assertFalse(userAccountUpdateDto.equals(null));
    }

    /**
     * Tests that two {@link UserAccountUpdateDto} instances with different lastname values are not equal.
     */
    @Test
    public void testUserAccountUpdateDtoEqualsFalseLastname() {
        UserAccountUpdateDto userAccountUpdateDto = new UserAccountUpdateDto();
        userAccountUpdateDto.setFirstname("John");
        userAccountUpdateDto.setLastname("Smith");

        UserAccountUpdateDto userAccountUpdateDto2 = new UserAccountUpdateDto();
        userAccountUpdateDto2.setFirstname("John");
        userAccountUpdateDto2.setLastname("Huber");

        Assertions.assertFalse(userAccountUpdateDto.equals(userAccountUpdateDto2));
    }

    /**
     * Tests that two {@link UserAccountUpdateDto} instances with different firstname values are not equal.
     */
    @Test
    public void testUserAccountUpdateDtoEqualsFalseFirstname() {
        UserAccountUpdateDto userAccountUpdateDto = new UserAccountUpdateDto();
        userAccountUpdateDto.setFirstname("Sepp");
        userAccountUpdateDto.setLastname("Smith");

        UserAccountUpdateDto userAccountUpdateDto2 = new UserAccountUpdateDto();
        userAccountUpdateDto2.setFirstname("John");
        userAccountUpdateDto2.setLastname("Smith");

        Assertions.assertFalse(userAccountUpdateDto.equals(userAccountUpdateDto2));
    }

    /**
     * Tests that a {@link UserAccountUpdateDto} is not equal to an object of a different class.
     */
    @Test
    public void testUserAccountUpdateDtoEqualsWrongClass() {
        UserAccountUpdateDto userAccountUpdateDto = new UserAccountUpdateDto();
        userAccountUpdateDto.setFirstname("John");
        userAccountUpdateDto.setLastname("Smith");

        UserAccountDto userAccountDto2 = new UserAccountDto();
        userAccountDto2.setFirstname("John");
        userAccountDto2.setLastname("Huber");

        Assertions.assertFalse(userAccountUpdateDto.equals(userAccountDto2));
    }
}
