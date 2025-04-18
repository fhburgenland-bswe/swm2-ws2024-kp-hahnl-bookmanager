package fh.bswe.bookmanager.helper;

import fh.bswe.bookmanager.config.OpenLibraryConfig;
import fh.bswe.bookmanager.dto.OpenLibraryAuthorDto;
import fh.bswe.bookmanager.dto.OpenLibraryBookDto;
import fh.bswe.bookmanager.exception.AuthorNotFoundException;
import fh.bswe.bookmanager.exception.BookNotFoundException;
import fh.bswe.bookmanager.exception.ConnectionErrorException;
import fh.bswe.bookmanager.exception.CoverNotFoundException;
import fh.bswe.bookmanager.exception.WebRequestErrorException;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * Helper component for interacting with the OpenLibrary API.
 * <p>
 * Provides methods to fetch book metadata and cover images based on ISBN numbers.
 * Handles various HTTP and connection errors and maps them to custom exceptions.
 */
@Component
@SuppressWarnings({
        "PMD.CyclomaticComplexity",
        "PMD.PreserveStackTrace",
})
public class OpenLibraryFetcher {
    private final OpenLibraryConfig openLibraryConfig;
    private final Logger logger = LoggerFactory.getLogger(OpenLibraryFetcher.class);

    private final HttpClient httpClient = HttpClient.create()
            .followRedirect(true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofSeconds(5))
            .doOnConnected(conn -> conn
                    .addHandlerLast(new ReadTimeoutHandler(10))
                    .addHandlerLast(new WriteTimeoutHandler(10))
            );
    public WebClient client = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

    /**
     * Constructs a new {@code OpenLibraryFetcher} with the provided OpenLibrary configuration.
     *
     * @param openLibraryConfig the configuration containing OpenLibrary URLs and settings
     */
    public OpenLibraryFetcher(final OpenLibraryConfig openLibraryConfig) {
        this.openLibraryConfig = openLibraryConfig;
    }

    /**
     * Fetches book metadata from the OpenLibrary API using the given ISBN.
     * <p>
     * Returns a {@link OpenLibraryBookDto} object if the request is successful.
     * Throws specific exceptions for connection issues, HTTP errors, or missing books.
     *
     * @param isbn the ISBN of the book to be fetched
     * @return an {@link OpenLibraryBookDto} instance containing book metadata
     * @throws ConnectionErrorException    if the OpenLibrary service is unreachable
     * @throws BookNotFoundException       if the book with the given ISBN is not found (HTTP 404)
     * @throws WebRequestErrorException    if an unexpected HTTP or runtime error occurs
     */
    public OpenLibraryBookDto fetchBook(final String isbn) {
        final ResponseEntity<OpenLibraryBookDto> response;

        try {
            response = client
                    .get()
                    .uri(openLibraryConfig.getBookUrl() + isbn + ".json")
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> {
                                logger.warn("Client error: {}", clientResponse.statusCode());
                                return clientResponse.createException();
                            }
                    )
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            clientResponse -> {
                                logger.error("Server error: {}", clientResponse.statusCode());
                                return clientResponse.createException();
                            }
                    )
                    .toEntity(OpenLibraryBookDto.class)
                    .block();

        } catch (WebClientRequestException e) {
            logger.error("URL not reachable: {}", e.getMessage());
            throw new ConnectionErrorException("URL not reachable: " + e.getMessage());
        } catch (WebClientResponseException e) {
            logger.error("HTTP-Response error ({}): {}", e.getStatusCode(), e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new BookNotFoundException("Book not found: " + isbn);
            }
            throw new WebRequestErrorException("HTTP-Response error (" + e.getStatusCode() + "): " + e.getMessage());
        } catch (Exception e) {
            logger.error("General error: {}", e.getMessage());
            throw new WebRequestErrorException("General error: " + e.getMessage());
        }

        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            logger.error("Fetching book failed!");
            throw new WebRequestErrorException("Fetching book failed");
        }

        return response.getBody();
    }

    /**
     * Fetches the cover image of a book from the OpenLibrary API using the given ISBN.
     * <p>
     * Returns the image as a byte array. Throws specific exceptions for connection issues,
     * HTTP errors, or missing covers.
     *
     * @param isbn the ISBN of the book whose cover image is to be fetched
     * @return a byte array containing the cover image in JPEG format
     * @throws ConnectionErrorException    if the OpenLibrary service is unreachable
     * @throws CoverNotFoundException      if the cover image is not found (HTTP 404)
     * @throws WebRequestErrorException    if an unexpected HTTP or runtime error occurs
     */
    public byte[] fetchCover(final String isbn) {
        try {
            return client
                    .get()
                    .uri(openLibraryConfig.getCoverUrl() + isbn + ".jpg")
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> {
                                logger.warn("Client error: {}", clientResponse.statusCode());
                                return clientResponse.createException();
                            }
                    )
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            clientResponse -> {
                                logger.error("Server error: {}", clientResponse.statusCode());
                                return clientResponse.createException();
                            }
                    )
                    .bodyToMono(byte[].class)
                    .block();

        } catch (WebClientRequestException e) {
            logger.error("URL not reachable: {}", e.getMessage());
            throw new ConnectionErrorException("URL not reachable: " + e.getMessage());
        } catch (WebClientResponseException e) {
            logger.error("HTTP-Response error ({}): {}", e.getStatusCode(), e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new CoverNotFoundException("Book not found: " + isbn);
            }
            throw new WebRequestErrorException("HTTP-Response error (" + e.getStatusCode() + "): " + e.getMessage());
        } catch (Exception e) {
            logger.error("General error: {}", e.getMessage());
            throw new WebRequestErrorException("General error: " + e.getMessage());
        }
    }

    /**
     * Fetches author metadata from the OpenLibrary API using the given author key.
     * <p>
     * Returns a {@link OpenLibraryAuthorDto} object if the request is successful.
     * Throws specific exceptions for connection issues, HTTP errors, or missing authors.
     *
     * @param authorKey the OpenLibrary author key (e.g., {@code /authors/OL1234A})
     * @return an {@link OpenLibraryAuthorDto} instance containing author metadata
     * @throws ConnectionErrorException    if the OpenLibrary service is unreachable
     * @throws AuthorNotFoundException     if the author with the given key is not found (HTTP 404)
     * @throws WebRequestErrorException    if an unexpected HTTP or runtime error occurs
     */
    public OpenLibraryAuthorDto fetchAuthor(final String authorKey) {
        final ResponseEntity<OpenLibraryAuthorDto> response;

        try {
            response = client
                    .get()
                    .uri(openLibraryConfig.getAuthorUrl() + authorKey + ".json")
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> {
                                logger.warn("Client error: {}", clientResponse.statusCode());
                                return clientResponse.createException();
                            }
                    )
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            clientResponse -> {
                                logger.error("Server error: {}", clientResponse.statusCode());
                                return clientResponse.createException();
                            }
                    )
                    .toEntity(OpenLibraryAuthorDto.class)
                    .block();

        } catch (WebClientRequestException e) {
            logger.error("URL not reachable: {}", e.getMessage());
            throw new ConnectionErrorException("URL not reachable: " + e.getMessage());
        } catch (WebClientResponseException e) {
            logger.error("HTTP-Response error ({}): {}", e.getStatusCode(), e.getMessage());
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new AuthorNotFoundException("Author not found: " + authorKey);
            }
            throw new WebRequestErrorException("HTTP-Response error (" + e.getStatusCode() + "): " + e.getMessage());
        } catch (Exception e) {
            logger.error("General error: {}", e.getMessage());
            throw new WebRequestErrorException("General error: " + e.getMessage());
        }

        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            logger.error("Fetching author failed");
            throw new WebRequestErrorException("Fetching author failed");
        }

        return response.getBody();
    }
}
