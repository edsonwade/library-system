package code.with.vanilson.libraryapplication.book;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * IBookService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
public interface IBookService {

    /**
     * Get all books
     *
     * @return List of books
     */
    List<BookResponse> getAllBooks();

    /**
     * Get a book by its ID
     *
     * @param id ID of the book to be retrieved
     */
    BookResponse getBookById(Long id);

    /**
     * Create a new book
     *
     * @param bookRequest Book object to be created
     * @return Book object with assigned ID
     */
    BookResponse createBook(BookRequest bookRequest);

    /**
     * Update an existing book
     *
     * @param bookRequest Book object to be updated with new information about the book being updated
     * @param bookId      Update method to be called to update the book object with new information about the book being updated
     */

    BookResponse updateBook(BookRequest bookRequest, Long bookId);

    /**
     * Delete a book
     *
     * @param bookId ID of the book to be deleted
     */
    void deleteBook(Long bookId);

}
