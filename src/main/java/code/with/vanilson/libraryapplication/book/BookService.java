package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.common.utils.MessageProvider;
import code.with.vanilson.libraryapplication.Member.Member;
import code.with.vanilson.libraryapplication.Member.MemberRepository;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static code.with.vanilson.libraryapplication.book.BookMapper.mapToBookEntity;
import static code.with.vanilson.libraryapplication.book.BookMapper.mapToBookResponse;

/**
 * BookService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@Repository
@Slf4j
@Service
public class BookService implements IBookService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LibrarianRepository librarianRepository;

    public BookService(BookRepository bookRepository, MemberRepository memberRepository,
                       LibrarianRepository librarianRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.librarianRepository = librarianRepository;
    }

    /**
     * Retrieves all books from the repository.
     *
     * @return List of {@link BookResponse} representing all books in the system.
     */
    @Override
    public List<BookResponse> getAllBooks() {
        log.info("Retrieving all books");
        return bookRepository.findAll().stream()
                .map(BookMapper::mapToBookResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param bookId The ID of the book to be retrieved.
     * @return {@link Optional<BookResponse>} containing the book details if found.
     */
    @Override
    public Optional<BookResponse> getBookById(Integer bookId) {
        log.info("Retrieving book with ID: {}", bookId);
        return bookRepository.findById(bookId)
                .map(BookMapper::mapToBookResponse)
                .or(() -> {
                    loggerError(bookId);
                    throw new ResourceNotFoundException(
                            MessageFormat.format(MessageProvider.getMessage("library.book.not_found"), bookId));
                });
    }

    /**
     * Creates a new book in the system.
     *
     * @param bookRequest The request containing details of the book to be created.
     * @return The created {@link BookResponse} representing the saved book.
     */
    @Override
    public BookResponse createBook(BookRequest bookRequest) {
        validateBookRequest(bookRequest);
        var librarian = findLibrarianById(bookRequest.getLibrarianId());
        var member = findMemberById(bookRequest.getMemberIds());

        var bookEntity = mapToBookEntity(bookRequest, librarian, member);
        var savedBook = bookRepository.save(bookEntity);

        return mapToBookResponse(savedBook);
    }

    /**
     * Updates an existing book.
     *
     * @param bookRequest The request containing updated book details.
     * @param bookId      The ID of the book to be updated.
     * @return The updated {@link BookResponse}.
     */
    @Override
    public BookResponse updateBook(BookRequest bookRequest, Integer bookId) {
        validateBookRequest(bookRequest);

        var existingBook = findBookById(bookId);
        var librarian = findLibrarianById(bookRequest.getLibrarianId());
        var member = findMemberById(bookRequest.getMemberIds());

        updateBookEntity(existingBook, bookRequest, librarian, member);
        var updatedBook = bookRepository.save(existingBook);

        return mapToBookResponse(updatedBook);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param bookId The ID of the book to be deleted.
     */
    @Override
    public void deleteBook(Integer bookId) {
        var existingBook = findBookById(bookId);
        bookRepository.delete(existingBook);
        log.info("Book with ID {} has been deleted successfully", bookId);
    }

    // Helper Methods

    /**
     * Validates the {@link BookRequest}.
     *
     * @param bookRequest The book request to be validated.
     */
    private void validateBookRequest(BookRequest bookRequest) {
        if (bookRequest == null) {
            log.error("Book request is null");
            throw new IllegalArgumentException(MessageProvider.getMessage("library.book.request_null"));
        }
    }

    /**
     * Finds a book by its ID or throws an exception if not found.
     *
     * @param bookId The ID of the book.
     * @return The found {@link Book}.
     */
    private Book findBookById(Integer bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    loggerError(bookId);
                    return new ResourceNotFoundException(
                            MessageFormat.format(MessageProvider.getMessage("library.book.not_found"), bookId));
                });
    }

    /**
     * Finds a librarian by ID or throws an exception if not found.
     *
     * @param librarianId The ID of the librarian.
     * @return The found {@link Librarian}.
     */
    private Librarian findLibrarianById(Long librarianId) {
        return librarianRepository.findById(librarianId)
                .orElseThrow(() -> {
                    log.error("Librarian with ID {} not found", librarianId);
                    return new ResourceNotFoundException(
                            MessageFormat.format(MessageProvider.getMessage("library.librarian.not_found"),
                                    librarianId));
                });
    }

    /**
     * Finds a member by ID or throws an exception if not found.
     *
     * @param memberId The ID of the member.
     * @return The found {@link Member}.
     */
    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("Member with ID {} not found", memberId);
                    return new ResourceNotFoundException(
                            MessageFormat.format(MessageProvider.getMessage("library.member.not_found"), memberId));
                });
    }

    /**
     * Updates the existing book entity with new values from the book request.
     *
     * @param existingBook The existing book entity to be updated.
     * @param bookRequest  The book request containing updated values.
     * @param librarian    The librarian to be assigned to the book.
     * @param member       The member to be assigned to the book.
     */
    private void updateBookEntity(Book existingBook, BookRequest bookRequest, Librarian librarian, Member member) {
        existingBook.setTitle(bookRequest.getTitle());
        existingBook.setAuthor(bookRequest.getAuthor());
        existingBook.setIsbn(bookRequest.getIsbn());
        existingBook.setGenre(bookRequest.getGenre());
        existingBook.setPublisherName(bookRequest.getPublisherName());
        existingBook.setPublisherYear(bookRequest.getPublisherYear());
        existingBook.setStatus(bookRequest.getStatus());
        existingBook.setLibrarian(librarian);
        existingBook.getMembers().add(member);
    }

    private static void loggerError(Integer bookId) {
        log.error("Book with ID {} not found", bookId);
    }
}
