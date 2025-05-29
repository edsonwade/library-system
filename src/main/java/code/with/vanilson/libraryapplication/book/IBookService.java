package code.with.vanilson.libraryapplication.book;

import java.util.List;

/**
 * IBookService - Interface defining operations for managing books in the library system
 *
 * @author vamuhong
 * @version 1.1
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
     * @return BookResponse containing book details
     */
    BookResponse getBookById(Long id);

    /**
     * Get a book by its title
     *
     * @param title title of the book to be retrieved
     * @return BookResponse containing book details
     */
    BookResponse getBookByTitle(String title);

    /**
     * Get a book by its author
     *
     * @param author author of the book to be retrieved
     * @return BookResponse containing book details
     */
    BookResponse getBookByAuthor(String author);

    /**
     * Get a book by its isbn
     *
     * @param isbn isbn of the book to be retrieved
     * @return BookResponse containing book details
     */
    BookResponse getBookByIsbn(String isbn);

    /**
     * Get a book by its genre
     *
     * @param genre genre of the book to be retrieved
     * @return BookResponse containing book details
     */
    BookResponse getBookByGenre(String genre);

    /**
     * Create a new book
     *
     * @param bookRequest Book object to be created
     * @return BookResponse with assigned ID
     */
    BookResponse createBook(BookRequest bookRequest);

    /**
     * Update an existing book
     *
     * @param bookRequest Book object to be updated with new information
     * @param bookId      ID of the book to be updated
     * @return Updated BookResponse
     */
    BookResponse updateBook(BookRequest bookRequest, Long bookId);

    /**
     * Delete a book
     *
     * @param bookId ID of the book to be deleted
     */
    void deleteBook(Long bookId);

    /**
     * Allows a librarian to borrow a book for a member.
     * Checks the availability of the book before proceeding with the borrowing process.
     *
     * @param bookId      The ID of the book to be borrowed.
     * @param memberId    The ID of the member who wants to borrow the book.
     * @param librarianId The ID of the librarian processing the borrow request.
     * @return Updated {@link BookResponse} representing the borrowed book.
     */
    BookResponse borrowBook(Long bookId, Long memberId, Long librarianId);

    /**
     * Checks if a book is available for borrowing.
     *
     * @param bookId The ID of the book to check.
     * @return {@code true} if the book is available, {@code false} otherwise.
     */
    boolean isBookAvailable(Long bookId);

    /**
     * Returns a borrowed book back to the library.
     *
     * @param bookId      The ID of the book being returned.
     * @param memberId    The ID of the member returning the book.
     * @param librarianId The ID of the librarian processing the return.
     * @return Updated {@link BookResponse} after the book is returned.
     */
    BookResponse returnBook(Long bookId, Long memberId, Long librarianId);
}
