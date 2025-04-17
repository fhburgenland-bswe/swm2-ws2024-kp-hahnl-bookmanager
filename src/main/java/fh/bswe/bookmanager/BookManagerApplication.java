package fh.bswe.bookmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * The main entry point for the Book Manager application.
 */
@SpringBootApplication
@ConfigurationPropertiesScan("fh.bswe.bookmanager.config")
public class BookManagerApplication {

    /**
     * The main method that starts the Spring Boot application.
     * @param args  command-line arguments passed to the application
     */
    public static void main(final String[] args) {
        SpringApplication.run(BookManagerApplication.class, args);
    }

}
