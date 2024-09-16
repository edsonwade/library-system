package code.with.vanilson.libraryapplication.book;

import java.util.List;

/**
 * IBookService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@SuppressWarnings("unused")
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
     * Get a book by its title
     *
     * @param title title of the book to be retrieved
     */
    BookResponse getBookByTitle(String title);

    /**
     * Get a book by its author
     *
     * @param author author of the book to be retrieved
     */
    BookResponse getBookByAuthor(String author);

    /**
     * Get a book by its isbn
     *
     * @param isbn isbn of the book to be retrieved
     */
    BookResponse getBookByIsbn(String isbn);

    /**
     * Get a book by its genre
     *
     * @param genre isbn of the book to be retrieved
     */
    BookResponse getBookByGenre(String genre);



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
