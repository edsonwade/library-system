package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.common.https.HeaderConstants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * BookController
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@RestController
@RequestMapping("/api/books")
@CrossOrigin(
        origins = "http://localhost:8081", // Replace it with your frontend URL(s) if needed.
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        allowedHeaders = {"*"},
        exposedHeaders = {"*"},
        allowCredentials = "true",
        maxAge = 3600
)
@Slf4j
public class BookController {

    public static final String BOOKS = "books";
    // Implement the CRUD operations for books here...
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves a list of all books from the database.
     *
     * @return A ResponseEntity containing a list of BookResponse objects.
     * The HTTP status code is 200 (OK) if the operation is successful.
     * The body of the response contains the list of books.
     */
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        log.info("Retrieving all books");
        var books = bookService.getAllBooks();

        HttpHeaders headers = prepareResponseHeaders(null, false);

        books.forEach(bookResponse -> {
            bookResponse.add(linkTo(methodOn(BookController.class)
                    .getBookById(headers, bookResponse.getId()))
                    .withSelfRel());
            bookResponse.add(linkTo(methodOn(BookController.class)
                    .getAllBooks())
                    .withRel(BOOKS));

        });
        return ResponseEntity.ok()
                .headers(headers)
                .body(books);
    }

    /**
     * Retrieves a book by its unique identifier from the database.
     *
     * @param bookId The unique identifier of the book to retrieve.
     * @return A ResponseEntity containing a BookResponse object.
     * The HTTP status code is 200 (OK) if the operation is successful.
     * The body of the response contains the book if found.
     * @throws IllegalArgumentException If the bookId is less than or equal to zero.
     */
    @GetMapping(value = "/{bookId}")
    public ResponseEntity<BookResponse> getBookById(@RequestHeader HttpHeaders headers, @PathVariable long bookId) {
        var bookResponse = bookService.getBookById(bookId);
        bookResponse.add(linkTo(methodOn(BookController.class)
                .getBookById(headers, bookResponse.getId()))
                .withSelfRel());
        bookResponse.add(linkTo(methodOn(BookController.class)
                .getAllBooks())
                .withRel(BOOKS));
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bookResponse);
    }

    /**
     * Retrieves a book by its title.
     *
     * @param title the title of the book
     * @return a ResponseEntity containing the BookResponse
     */
    @GetMapping(value = "/title")
    public ResponseEntity<BookResponse> getBookByTitle(@RequestParam(name = "title") String title,
                                                       @RequestHeader HttpHeaders headers) {
        var bookResponse = bookService.getBookByTitle(title);

        bookResponse.add(
                linkTo(methodOn(BookController.class).getBookById(headers, bookResponse.getId())).withSelfRel());
        bookResponse.add(linkTo(methodOn(BookController.class).getAllBooks()).withRel(BOOKS));
        return ResponseEntity.ok(bookResponse);
    }

    /**
     * Retrieves a book by its author.
     *
     * @param author the author of the book
     * @return a ResponseEntity containing the BookResponse
     */

    @GetMapping(value = "/author")
    public ResponseEntity<BookResponse> getBookByAuthor(@RequestParam(name = "author") String author,
                                                        @RequestHeader HttpHeaders headers) {
        var bookResponse = bookService.getBookByAuthor(author);
        bookResponse.add(
                linkTo(methodOn(BookController.class).getBookById(headers, bookResponse.getId())).withSelfRel());
        bookResponse.add(linkTo(methodOn(BookController.class).getAllBooks()).withRel(BOOKS));
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bookResponse);
    }

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn the ISBN of the book
     * @return a ResponseEntity containing the BookResponse
     */
    @GetMapping(value = "/isbn")
    public ResponseEntity<BookResponse> getBookByIsbn(@RequestParam(name = "isbn") String isbn,
                                                      @RequestHeader HttpHeaders headers) {
        var bookResponse = bookService.getBookByIsbn(isbn);
        bookResponse.add(linkTo(methodOn(BookController.class)
                .getBookById(headers, bookResponse.getId()))
                .withSelfRel());
        bookResponse.add(linkTo(methodOn(BookController.class)
                .getAllBooks())
                .withRel(BOOKS));

        return ResponseEntity.ok()
                .headers(headers)
                .body(bookResponse);
    }

    /**
     * Retrieves a book by its genre.
     *
     * @param genre the genre of the book
     * @return a ResponseEntity containing the BookResponse
     */
    @GetMapping(value = "/genre")
    public ResponseEntity<BookResponse> getBookByGenre(@RequestParam(name = "genre") String genre,
                                                       @RequestHeader HttpHeaders headers) {
        var bookResponse = bookService.getBookByIsbn(genre);
        bookResponse.add(linkTo(methodOn(BookController.class)
                .getBookById(headers, bookResponse.getId()))
                .withSelfRel());
        bookResponse.add(linkTo(methodOn(BookController.class)
                .getAllBooks())
                .withRel(BOOKS));
        return ResponseEntity.ok(bookResponse);
    }

    /**
     * Creates a new book in the database.
     *
     * @param bookRequest The request object containing the details of the book to be created.
     * @return A ResponseEntity containing the newly created BookResponse object.
     * The HTTP status code is 201 (Created) if the operation is successful.
     * The body of the response contains the newly created book.
     * The Location header contains the URI of the newly created book.
     * The ETag header contains the ISBN of the newly created book.
     * @throws URISyntaxException If the URI creation fails.
     */
    // Create a new Book
    @PostMapping(value = "/create-book")
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest bookRequest)
            throws URISyntaxException {
        var newBook = bookService.createBook(bookRequest);
        // Prepare response headers
        HttpHeaders headers = prepareResponseHeaders(newBook, true);

        newBook.add(linkTo(methodOn(BookController.class).getBookById(headers, newBook.getId())).withSelfRel());
        newBook.add(linkTo(methodOn(BookController.class).getAllBooks()).withRel(BOOKS));

        // Build a created response with URI and ETag
        return ResponseEntity
                .created(new URI("/api/books/" + newBook.getId()))
                .eTag(newBook.getIsbn())
                .headers(headers)
                .body(newBook);
    }

    /**
     * Endpoint for borrowing a book.
     *
     * @param bookId      The ID of the book to be borrowed.
     * @param memberId    The ID of the member borrowing the book.
     * @param librarianId The ID of the librarian processing the request.
     * @return Updated {@link BookResponse} representing the borrowed book.
     */
    @PostMapping("/{bookId}/borrow")
    public ResponseEntity<BookResponse> borrowBook(
            @PathVariable Long bookId,
            @RequestParam Long memberId,
            @RequestParam Long librarianId) {
        var response = bookService.borrowBook(bookId, memberId, librarianId);

        return ResponseEntity
                .ok(response);
    }

    /**
     * Endpoint for checking if a book is available.
     *
     * @param bookId The ID of the book to check.
     * @return {@code true} if the book is available, {@code false} otherwise.
     */
    @GetMapping("/{bookId}/available")
    public ResponseEntity<Boolean> isBookAvailable(@PathVariable Long bookId) {
        var available = bookService.isBookAvailable(bookId);
        log.info("Book available: {}", available);
        return ResponseEntity.ok(available);
    }

    /**
     * Endpoint for returning a borrowed book.
     *
     * @param bookId      The ID of the book being returned.
     * @param memberId    The ID of the member returning the book.
     * @param librarianId The ID of the librarian processing the return.
     * @return Updated {@link BookResponse} after the book is returned.
     */
    @PostMapping("/{bookId}/return")
    public ResponseEntity<BookResponse> returnBorrowedBook(
            @PathVariable Long bookId,
            @RequestParam Long memberId,
            @RequestParam Long librarianId) {
        var response = bookService.returnBook(bookId, memberId, librarianId);
        log.info("Book returned: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing book.
     *
     * @param bookRequest the request body containing the book details
     * @param id          the ID of the book to be updated
     * @return a ResponseEntity containing the updated BookResponse
     */
    @PutMapping(value = "/update-book/{id}")
    public ResponseEntity<BookResponse> updateExistentBook(@Valid
                                                           @RequestBody BookRequest bookRequest,
                                                           @PathVariable(name = "id") long id) {
        log.info("Updating fine with ID: {}", id);
        var bookResponse = bookService.updateBook(bookRequest, id);

        // Prepare response headers
        HttpHeaders headers = prepareResponseHeaders(bookResponse, true);
        // Add HATEOAS links
        bookResponse.add(linkTo(methodOn(BookController.class)
                .getBookById(headers, bookResponse.getId()))
                .withSelfRel());
        bookResponse.add(linkTo(methodOn(BookController.class)
                .getAllBooks())
                .withRel(BOOKS));

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bookResponse);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to be deleted
     * @return a ResponseEntity with no content
     */
    @DeleteMapping("/delete-book/{id}")
    public ResponseEntity<Void> deleteFine(@PathVariable Long id) {
        log.warn("Deleting book with ID: {}", id);
        bookService.deleteBook(id);
        // Add HATEOAS link to the list of all fines
        // Create a base URL for listing fines
        String allFinesUrl = linkTo(methodOn(BookController.class).getAllBooks()).toUri().toString();
        // You can include a location header or return the link in the response body if necessary
        return ResponseEntity
                .noContent()
                .header(HttpHeaders.LINK, allFinesUrl)
                .build();
    }

    /**
     * Prepares HTTP headers for the response based on the provided admin data and cookie inclusion flag.
     * This method sets standard security headers, CORS headers, and optionally includes custom headers
     * specific to an {@link BookResponse} object and a {@code Set-Cookie} header.
     *
     * @param bookResponse  The {@link BookResponse} object used to set custom headers specific to the admin details.
     *                      If {@code null}, custom admin-specific headers will not be set.
     * @param includeCookie A {@code boolean} indicating whether to include a {@code Set-Cookie} header in the response.
     *                      If {@code true}, a session cookie is added; if {@code false}, no session cookie is added.
     * @return {@link HttpHeaders} containing the configured headers for the HTTP response.
     *
     * <p>
     * This method sets the following headers:
     * <ul>
     *     <li>{@code Content-Type: application/json} - Specifies that the response body contains JSON data.</li>
     *     <li>{@code Strict-Transport-Security} - Enables HTTP Strict Transport Security (HSTS) to prevent MITM attacks.</li>
     *     <li>{@code X-Content-Type-Options} - Prevents browsers from interpreting files as a different MIME type.</li>
     *     <li>{@code X-Frame-Options} - Prevents blackjacking by disallowing the page to be framed.</li>
     *     <li>{@code X-XSS-Protection} - Enables cross-site scripting (XSS) protection in supported browsers.</li>
     *     <li>{@code Access-Control-Allow-Origin} - Specifies which origins are permitted to access the resource.</li>
     *     <li>{@code Set-Cookie} (conditionally) - Sets a session cookie if {@code includeCookie} is {@code true}.</li>
     *     <li>Custom headers related to the admin data (if {@code adminResponse} is not {@code null})</li>
     * </ul>
     * </p>
     */

    private HttpHeaders prepareResponseHeaders(BookResponse bookResponse, boolean includeCookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Set Content-Type correctly

        // Security headers (always include)
        headers.set(HeaderConstants.STRICT_TRANSPORT_SECURITY, HeaderConstants.STRICT_TRANSPORT_SECURITY_VALUE); // HSTS
        headers.set(HeaderConstants.X_CONTENT_TYPE_OPTIONS,
                HeaderConstants.X_CONTENT_TYPE_OPTIONS_VALUE); // Prevent MIME type sniffing
        headers.set(HeaderConstants.X_FRAME_OPTIONS, HeaderConstants.X_FRAME_OPTIONS_VALUE); // Prevent clickjacking
        headers.set(HeaderConstants.X_XSS_PROTECTION, HeaderConstants.X_XSS_PROTECTION_VALUE); // Enable XSS protection

        // CORS header (adjust based on access needs)
        headers.set(HeaderConstants.ACCESS_CONTROL_ALLOW_ORIGIN,
                HeaderConstants.ACCESS_CONTROL_ALLOW_ORIGIN_VALUE); // Allow cross-origin access from any domain

        if (bookResponse != null) {
            // Custom headers (if the response contains resource-specific data)
            headers.set(HeaderConstants.X_BOOK_ID, String.valueOf(bookResponse.getId()));
            headers.set(HeaderConstants.X_BOOK_TITLE, bookResponse.getTitle());
            headers.set(HeaderConstants.X_BOOK_AUTHOR, bookResponse.getAuthor());
            headers.set(HeaderConstants.X_BOOK_ISBN, bookResponse.getIsbn());
        }

        // Optionally include Set-Cookie header based on context
        if (includeCookie) {
            ResponseCookie sessionCookie = ResponseCookie.from("sessionId", "abc123")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .sameSite("Strict")
                    .build();
            headers.add(HeaderConstants.SET_COOKIE, sessionCookie.toString()); // Add the cookie to headers
        }

        return headers;
    }

}