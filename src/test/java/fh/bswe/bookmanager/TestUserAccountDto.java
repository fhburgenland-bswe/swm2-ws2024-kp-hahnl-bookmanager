package fh.bswe.bookmanager;

import fh.bswe.bookmanager.dto.UserAccountDto;
import fh.bswe.bookmanager.dto.UserAccountUpdateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestUserAccountDto {

    /**
     * Tests that two equal {@link UserAccountDto} instances produce the same hash code.
     */
    @Test
    public void testUserAccountDtoHashCode() {
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setId(1);
        userAccountDto.setFirstname("John");
        userAccountDto.setLastname("Smith");

        UserAccountDto userAccountDto2 = new UserAccountDto();
        userAccountDto2.setId(1);
        userAccountDto2.setFirstname("John");
        userAccountDto2.setLastname("Smith");

        Assertions.assertEquals(userAccountDto.hashCode(), userAccountDto2.hashCode());
    }

    /**
     * Tests that two {@link UserAccountDto} instances with identical data are equal.
     */
    @Test
    public void testUserAccountDtoEquals() {
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setId(1);
        userAccountDto.setFirstname("John");
        userAccountDto.setLastname("Smith");

        UserAccountDto userAccountDto2 = new UserAccountDto();
        userAccountDto2.setId(1);
        userAccountDto2.setFirstname("John");
        userAccountDto2.setLastname("Smith");

        Assertions.assertTrue(userAccountDto.equals(userAccountDto2));
    }

    /**
     * Tests that a {@link UserAccountDto} is not equal to {@code null}.
     */
    @Test
    public void testUserAccountDtoEqualsNull() {
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setId(1);
        userAccountDto.setFirstname("John");
        userAccountDto.setLastname("Smith");

        Assertions.assertFalse(userAccountDto.equals(null));
    }

    /**
     * Tests that two {@link UserAccountDto} instances with different lastname values are not equal.
     */
    @Test
    public void testUserAccountDtoEqualsFalse() {
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setId(1);
        userAccountDto.setFirstname("John");
        userAccountDto.setLastname("Smith");

        UserAccountDto userAccountDto2 = new UserAccountDto();
        userAccountDto2.setId(2);
        userAccountDto2.setFirstname("John");
        userAccountDto2.setLastname("Huber");

        Assertions.assertFalse(userAccountDto.equals(userAccountDto2));
    }

    /**
     * Tests that a {@link UserAccountDto} is not equal to an object of a different class.
     */
    @Test
    public void testUserAccountDtoEqualsWrongClass() {
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setId(1);
        userAccountDto.setFirstname("John");
        userAccountDto.setLastname("Smith");

        UserAccountUpdateDto userAccountUpdateDto = new UserAccountUpdateDto();
        userAccountUpdateDto.setFirstname("John");
        userAccountUpdateDto.setLastname("Huber");

        Assertions.assertFalse(userAccountDto.equals(userAccountUpdateDto));
    }
}
