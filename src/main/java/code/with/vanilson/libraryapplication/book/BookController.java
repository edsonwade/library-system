package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.common.exceptions.provider.LibraryExceptionHandlerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final LibraryExceptionHandlerProvider handlerProvider;

    public BookController(BookService bookService, LibraryExceptionHandlerProvider handlerProvider) {
        this.bookService = bookService;
        this.handlerProvider = handlerProvider;
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
     * @return A ResponseEntity containing an Optional of BookResponse object.
     * The HTTP status code is 200 (OK) if the operation is successful.
     * The body of the response contains the book if found, otherwise it's an empty Optional.
     * @throws IllegalArgumentException If the bookId is less than or equal to zero.
     */
    @GetMapping(value = "/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable long bookId) {
        try {
            log.info("Retrieving book by id: {}", bookId);
            BookResponse bookResponse = bookService.getBookById(bookId);
            return ResponseEntity.ok(bookResponse);
        } catch (ResourceNotFoundException ex) {
            log.error("Error book id not found {}", ex.getMessage());
            return handlerProvider.handleBookNotFound(ex);
        } catch (ResourceBadRequestException ex) {
            log.error("Error bad request book id {}", ex.getMessage());
            return handlerProvider.handleBookBadRequest(ex);
        }

    }

}