package fh.bswe.bookmanager;

import fh.bswe.bookmanager.config.OpenLibraryConfig;
import fh.bswe.bookmanager.dto.OpenLibraryAuthorDto;
import fh.bswe.bookmanager.dto.OpenLibraryBookDto;
import fh.bswe.bookmanager.exception.AuthorNotFoundException;
import fh.bswe.bookmanager.exception.BookNotFoundException;
import fh.bswe.bookmanager.exception.ConnectionErrorException;
import fh.bswe.bookmanager.exception.CoverNotFoundException;
import fh.bswe.bookmanager.exception.WebRequestErrorException;
import fh.bswe.bookmanager.helper.OpenLibraryFetcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import okio.Buffer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration tests for {@link OpenLibraryFetcher} using {@link MockWebServer}.
 * <p>
 * These tests verify the behavior of the fetcher when interacting with an OpenLibrary-like API,
 * including handling of successful responses, various HTTP error codes, connection issues,
 * and invalid responses.
 */
public class OpenLibraryFetcherTest {
    public static MockWebServer mockWebServer;

    private static OpenLibraryFetcher fetcher;

    private static final byte[] VALID_RESPONSE_BYTES = new byte[] { 0x55 };
    private static final Buffer BUFFER = new Buffer().write(VALID_RESPONSE_BYTES);

    /**
     * Initializes the {@link MockWebServer} and configures the {@link OpenLibraryFetcher}
     * before running any tests.
     *
     * @throws IOException if the server fails to start
     */
    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        OpenLibraryConfig mockConfig = new OpenLibraryConfig();
        mockConfig.setBookUrl(mockWebServer.url("/book/").toString());
        mockConfig.setCoverUrl(mockWebServer.url("/cover/").toString());
        mockConfig.setAuthorUrl(mockWebServer.url("/author/").toString());
        fetcher = new OpenLibraryFetcher(mockConfig);
    }

    /**
     * Shuts down the {@link MockWebServer} after all tests have run.
     *
     * @throws IOException if the server fails to shut down
     */
    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    /**
     * Tests successful fetching of a book with a valid JSON response.
     */
    @Test
    void testFetchBookSuccess() throws Exception {
        String validReponse = "{\"works\": [{\"key\": \"/works/OL16804289W\"}], " +
                "\"title\": \"Inferno\", \"publishers\": [\"Anchor Books\"], " +
                "\"publish_date\": \"2016\", \"key\": \"/books/OL49829482M\", " +
                "\"type\": {\"key\": \"/type/edition\"}, \"identifiers\": {}, " +
                "\"covers\": [14540877], \"ocaid\": \"inferno0000danb\", " +
                "\"isbn_13\": [\"9781101974117\"], \"classifications\": {}, " +
                "\"languages\": [{\"key\": \"/languages/eng\"}], \"latest_revision\": 3, " +
                "\"revision\": 3, \"created\": {\"type\": \"/type/datetime\", " +
                "\"value\": \"2023-11-05T15:01:36.790484\"}, \"last_modified\": {\"type\": " +
                "\"/type/datetime\", \"value\": \"2023-11-05T15:02:41.501834\"}}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setBody(validReponse));

        OpenLibraryBookDto book = fetcher.fetchBook("9781101974117");

        assertEquals("9781101974117", book.getIsbn_13().getFirst());
    }

    /**
     * Tests that a 404 response results in a {@link BookNotFoundException}.
     */
    @Test
    void testFetchBookNotFound() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404));

        assertThrows(BookNotFoundException.class, () -> fetcher.fetchBook("9781101974117"));
    }

    /**
     * Tests that a 403 response results in a {@link WebRequestErrorException}.
     */
    @Test
    void testFetchBookForbidden() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(403));

        assertThrows(WebRequestErrorException.class, () -> fetcher.fetchBook("9781101974117"));
    }

    /**
     * Tests that a 500 response results in a {@link WebRequestErrorException}.
     */
    @Test
    void testFetchBookInternalServerError() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        assertThrows(WebRequestErrorException.class, () -> fetcher.fetchBook("9781101974117"));
    }

    /**
     * Tests that a dropped connection results in a {@link ConnectionErrorException}.
     */
    @Test
    void testFetchBookConnectionError() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        assertThrows(ConnectionErrorException.class, () -> fetcher.fetchBook("9781101974117"));

    }

    /**
     * Tests that a malformed JSON response results in a {@link WebRequestErrorException}.
     */
    @Test
    void testFetchBookParsingError() throws Exception {
        String invalidReponse = "\"works\": [{\"key\": \"/works/OL16804289W\"}], " +
                "\"title\": \"Inferno\", \"publishers\": [\"Anchor Books\"], " +
                "\"publish_date\": \"2016\", \"key\": \"/books/OL49829482M\", " +
                "\"type\": {\"key\": \"/type/edition\"}, \"identifiers\": {}, " +
                "\"covers\": [14540877], \"ocaid\": \"inferno0000danb\", " +
                "\"isbn_13\": [\"9781101974117\"], \"classifications\": {}, " +
                "\"languages\": [{\"key\": \"/languages/eng\"}], \"latest_revision\": 3, " +
                "\"revision\": 3, \"created\": {\"type\": \"/type/datetime\", \"value\": " +
                "\"2023-11-05T15:01:36.790484\"}, \"last_modified\": {\"type\": " +
                "\"/type/datetime\", \"value\": \"2023-11-05T15:02:41.501834\"}}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setBody(invalidReponse));

        assertThrows(WebRequestErrorException.class, () -> fetcher.fetchBook("9781101974117"));
    }

    /**
     * Tests successful fetching of a cover image.
     */
    @Test
    void testFetchCoverSuccess() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .setBody(BUFFER));

        byte[] cover = fetcher.fetchCover("9781101974117");

        assertEquals(0x55, cover[0]);
    }

    /**
     * Tests that a 404 response when fetching a cover results in a {@link CoverNotFoundException}.
     */
    @Test
    void testFetchCoverNotFound() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404));

        assertThrows(CoverNotFoundException.class, () -> fetcher.fetchCover("9781101974117"));
    }

    /**
     * Tests that a 403 response when fetching a cover results in a {@link WebRequestErrorException}.
     */
    @Test
    void testFetchCoverForbidden() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(403));

        assertThrows(WebRequestErrorException.class, () -> fetcher.fetchCover("9781101974117"));
    }

    /**
     * Tests that a 500 response results in a {@link WebRequestErrorException}.
     */
    @Test
    void testFetchCoverInternalServerError() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        assertThrows(WebRequestErrorException.class, () -> fetcher.fetchCover("9781101974117"));
    }

    /**
     * Tests that a connection timeout or error when fetching a cover
     * results in a {@link ConnectionErrorException}.
     */
    @Test
    void testFetchCoverConnectionError() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        assertThrows(ConnectionErrorException.class, () -> fetcher.fetchCover("9781101974117"));

    }

    /**
     * Tests successful fetching of an author with a valid JSON response.
     */
    @Test
    void testFetchAuthorSuccess() throws Exception {
        String validReponse = "{\"type\": {\"key\": \"/type/author\"" +
                "},\"name\": \"Jack Thorne\",\"key\": \"/authors/OL10259603A\"," +
                "\"source_records\": [\"bwb:9781780019956\"],\"latest_revision\": 1," +
                "\"revision\": 1,\"created\": {\"type\": \"/type/datetime\"," +
                "\"value\": \"2022-02-27T20:39:03.235199\"},\"last_modified\": {\n" +
                "\"type\": \"/type/datetime\",\"value\": \"2022-02-27T20:39:03.235199\"" +
                "}}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setBody(validReponse));

        OpenLibraryAuthorDto authorDto = fetcher.fetchAuthor("OL10259603A");

        assertEquals("Jack Thorne", authorDto.getName());
    }

    /**
     * Tests successful fetching of an author with an invalid JSON response.
     */
    @Test
    void testFetchAuthorInvalidJson() throws Exception {
        String validReponse = "{ not valid";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setBody(validReponse));

        assertThrows(WebRequestErrorException.class, () -> fetcher.fetchAuthor("9781101974117"));
    }

    /**
     * Tests successful fetching of an author with a valid JSON response.
     */
    @Test
    void testFetchAuthorSuccessMissingAuthor() throws Exception {
        String validReponse = "{\"type\": {\"key\": \"/type/author\"" +
                "},\"name\": \"Jack Thorne\",\"key\": \"/authors/OL10259603A\"," +
                "\"source_records\": [\"bwb:9781780019956\"],\"latest_revision\": 1," +
                "\"revision\": 1,\"created\": {\"type\": \"/type/datetime\"," +
                "\"value\": \"2022-02-27T20:39:03.235199\"},\"last_modified\": {\n" +
                "\"type\": \"/type/datetime\",\"value\": \"2022-02-27T20:39:03.235199\"" +
                "}}";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setBody(validReponse));

        OpenLibraryAuthorDto authorDto = fetcher.fetchAuthor("OL10259603A");

        assertEquals("Jack Thorne", authorDto.getName());
    }

    /**
     * Tests that a 404 response results in a {@link AuthorNotFoundException}.
     */
    @Test
    void testFetchAuthorNotFound() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404));

        assertThrows(AuthorNotFoundException.class, () -> fetcher.fetchAuthor("OL10259603A"));
    }

    /**
     * Tests that a 403 response results in a {@link WebRequestErrorException}.
     */
    @Test
    void testFetchAuthorForbidden() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(403));

        assertThrows(WebRequestErrorException.class, () -> fetcher.fetchAuthor("OL10259603A"));
    }

    /**
     * Tests that a 500 response results in a {@link WebRequestErrorException}.
     */
    @Test
    void testFetchAuthorInternalServerError() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));

        assertThrows(WebRequestErrorException.class, () -> fetcher.fetchAuthor("9781101974117"));
    }

    /**
     * Tests that a dropped connection results in a {@link ConnectionErrorException}.
     */
    @Test
    void testFetchAuthorConnectionError() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE));

        assertThrows(ConnectionErrorException.class, () -> fetcher.fetchAuthor("OL10259603A"));

    }
}
