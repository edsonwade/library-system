package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.member.Member;
import code.with.vanilson.libraryapplication.member.MemberRepository;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceInvalidException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.common.utils.MessageProvider;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static code.with.vanilson.libraryapplication.book.BookMapper.mapToBookResponse;

/**
 * BookService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@SuppressWarnings("unused")
@Repository
@Slf4j
@Service
public class BookService implements IBookService {

    public static final String LIBRARY_BOOK_NOT_FOUND = "library.book.not_found";
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
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        log.info("Retrieving all books");
        return bookRepository.findAll().stream()
                .map(BookMapper::mapToBookResponse)
                .toList();
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param bookId The ID of the book to be retrieved.
     * @return {@link BookResponse} containing the book details if found.
     */
    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long bookId) {
        if (bookId <= 0) {
            var errorMessage = MessageProvider.getMessage("library.book.bad_request", bookId);
            throw new ResourceBadRequestException(errorMessage);
        }
        Optional<Book> book = bookRepository.findById(bookId);
        log.info("Retrieving book with ID: {}", bookId);
        return book
                .map(BookMapper::mapToBookResponse)
                .orElseThrow(() -> {
                    loggerError(bookId);
                    String errorMessage = MessageFormat.format(
                            MessageProvider.getMessage(LIBRARY_BOOK_NOT_FOUND), bookId);
                    return new ResourceNotFoundException(errorMessage);
                });
    }

    /**
     * Creates a new book in the system.
     *
     * @param bookRequest The request containing details of the book to be created.
     * @return The created {@link BookResponse} representing the saved book.
     */
    @Override
    @Transactional
    public BookResponse createBook(BookRequest bookRequest) {
        if (null == bookRequest) {
            throw new ResourceBadRequestException("Book request is null");
        }
        Set<Member> members = new HashSet<>();

        for (Member member : bookRequest.getMemberIds()) {
            var member1 = memberRepository.findById(member.getId())
                    .orElseThrow(() -> {
                        loggerError(member.getId());
                        String errorMessage = MessageFormat.format(
                                MessageProvider.getMessage("library.members.not_found"), member.getId());
                        return new ResourceNotFoundException(errorMessage);
                    });
            members.add(member1);

        }
        var librarian = librarianRepository.findById(bookRequest.getLibrarianId())
                .orElseThrow(() -> {
                    loggerError(bookRequest.getLibrarianId());
                    String errorMessage = MessageFormat.format(
                            MessageProvider.getMessage("library.librarian.not_found"), bookRequest.getLibrarianId());
                    return new ResourceNotFoundException(errorMessage);
                });

        var newBook = BookMapper.mapToBookEntity(bookRequest, librarian, members);

        var savedBook = bookRepository.save(newBook);

        return mapToBookResponse(savedBook);

    }

    @Override
    public BookResponse updateBook(BookRequest bookRequest, Long bookId) {
        return null;
    }

    /**
     * Deletes a book by its ID.
     *
     * @param bookId The ID of the book to be deleted.
     */
    @Override
    @Transactional
    public void deleteBook(Long bookId) {
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
        if (null == bookRequest) {
            log.error("Book request is null");
            throw new ResourceInvalidException(MessageProvider.getMessage("library.book.cannot_be_null"));
        }
    }

    /**
     * Finds a book by its ID or throws an exception if not found.
     *
     * @param bookId The ID of the book.
     * @return The found {@link Book}.
     */
    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    loggerError(bookId);
                    return new ResourceNotFoundException(
                            MessageFormat.format(MessageProvider.getMessage(LIBRARY_BOOK_NOT_FOUND), bookId));
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
     * Finds member by their IDs or throws an exception if any member ID is not found.
     *
     * @param memberIds The IDs of the member to find.
     * @return A set of found {@link Member}s.
     * @throws ResourceNotFoundException if any member ID is not found.
     */
    private Set<Member> findMembersByIds(Set<Long> memberIds) {
        // Fetch all member by their IDs
        List<Member> memberList = memberRepository.findAllById(memberIds);

        // Create a set from the list to ensure unique member
        Set<Member> members = new HashSet<>(memberList);

        // Check if the number of found member matches the number of requested IDs
        if (members.size() != memberIds.size()) {
            // Determine which member IDs were not found
            Set<Long> foundMemberIds = members.stream()
                    .map(Member::getId)
                    .collect(Collectors.toSet());

            Set<Long> notFoundMemberIds = memberIds.stream()
                    .filter(id -> !foundMemberIds.contains(id))
                    .collect(Collectors.toSet());

            log.error("Members with IDs {} not found", notFoundMemberIds);
            throw new ResourceNotFoundException(
                    MessageFormat.format(MessageProvider.getMessage("library.members.not_found"), notFoundMemberIds));
        }

        return members;
    }

    /**
     * Updates the existing book entity with new values from the book request.
     *
     * @param existingBook The existing book entity to be updated.
     * @param bookRequest  The book request containing updated values.
     * @param librarian    The librarian to be assigned to the book.
     * @param members      The member to be assigned to the book.
     */
    private void updateBookEntity(Book existingBook, BookRequest bookRequest, Librarian librarian,
                                  Set<Member> members) {
        existingBook.setTitle(bookRequest.getTitle());
        existingBook.setAuthor(bookRequest.getAuthor());
        existingBook.setIsbn(bookRequest.getIsbn());
        existingBook.setGenre(bookRequest.getGenre());
        existingBook.setPublisherName(bookRequest.getPublisherName());
        existingBook.setPublisherYear(bookRequest.getPublisherYear());
        existingBook.setStatus(bookRequest.getStatus());
        existingBook.setLibrarian(librarian);
        existingBook.setMembers(members); // Set the member
    }

    /**
     * Logs an error message for a book not found scenario.
     * <p>
     * This method constructs an error message using the provided book ID and logs it using the SLF4J logger.
     * The error message is formatted using the {@link MessageFormat} class and retrieved from the
     * {@link MessageProvider} using the key "library.book.not_found".
     *
     * @param bookId The ID of the book that was not found.
     */
    private static void loggerError(Long bookId) {
        var message = MessageFormat.format(MessageProvider.getMessage(LIBRARY_BOOK_NOT_FOUND), bookId);
        log.error(message);
    }
}
