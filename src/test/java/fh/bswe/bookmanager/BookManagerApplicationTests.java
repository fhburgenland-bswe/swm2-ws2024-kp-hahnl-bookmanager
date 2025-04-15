package fh.bswe.bookmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class BookManagerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testMain() {
        BookManagerApplication.main(new String[]{});
    }
}
