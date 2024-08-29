package code.with.vanilson.libraryapplication.book;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
        return ResponseEntity.ok()
                .body(bookService.getAllBooks());
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
    public ResponseEntity<BookResponse> getBookById(@PathVariable long bookId) {
        BookResponse bookResponse = bookService.getBookById(bookId);
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

        // Build a created response with URI and ETag
        return ResponseEntity
                .created(new URI("/api/books/" + newBook.getId()))
                .eTag(newBook.getIsbn())
                .body(newBook);
    }

}