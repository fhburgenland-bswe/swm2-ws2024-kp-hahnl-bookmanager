package fh.bswe.bookmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Book Manager application.
 */
@SpringBootApplication
public class BookManagerApplication {

    /**
     * The main method that starts the Spring Boot application.
     * @param args  command-line arguments passed to the application
     */
    public static void main(final String[] args) {
        SpringApplication.run(BookManagerApplication.class, args);
    }

}
