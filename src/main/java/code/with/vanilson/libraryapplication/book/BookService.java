package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.common.utils.MessageProvider;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianRepository;
import code.with.vanilson.libraryapplication.member.Member;
import code.with.vanilson.libraryapplication.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

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

    private static final String LIBRARY_BOOK_NOT_FOUND = "library.book.not_found";
    private static final String LIBRARY_LIBRARIAN_NOT_FOUND = "library.librarian.not_found";
    private static final String LIBRARY_MEMBERS_NOT_FOUND = "library.members.not_found";

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
     * Retrieves all books.
     *
     * @return List of BookResponse containing all books.
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
     * @param bookId The ID of the book.
     * @return BookResponse if the book exists.
     * @throws ResourceNotFoundException if book is not found.
     */
    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long bookId) {
        validateBookId(bookId);
        return bookRepository.findById(bookId)
                .map(BookMapper::mapToBookResponse)
                .orElseThrow(() -> resourceNotFoundException(LIBRARY_BOOK_NOT_FOUND, bookId));
    }

    @Override
    public BookResponse getBookByTitle(String title) {
        return null;
    }

    @Override
    public BookResponse getBookByAuthor(String author) {
        return null;
    }

    @Override
    public BookResponse getBookByIsbn(String isbn) {
        return null;
    }

    @Override
    public BookResponse getBookByGenre(String genre) {
        return null;
    }

    /**
     * Creates a new book.
     *
     * @param bookRequest BookRequest containing book details.
     * @return BookResponse of the created book.
     * @throws ResourceNotFoundException if librarian or members are not found.
     */
    @Override
    @Transactional
    public BookResponse createBook(BookRequest bookRequest) {
        validateBookRequest(bookRequest);
        Librarian librarian = findLibrarianById(bookRequest.getLibrarianId());
        Set<Member> members = findMembersByIds(bookRequest.getMemberIds());

        Book newBook = BookMapper.mapToBookEntity(bookRequest, librarian, members);
        Book savedBook = bookRepository.save(newBook);
        return BookMapper.mapToBookResponse(savedBook);
    }

    /**
     * Updates an existing book.
     *
     * @param bookRequest BookRequest with updated details.
     * @param bookId      The ID of the book to update.
     * @return BookResponse of the updated book.
     * @throws ResourceNotFoundException if book, librarian, or members are not found.
     */
    @Override
    @Transactional
    public BookResponse updateBook(BookRequest bookRequest, Long bookId) {
        validateBookRequest(bookRequest);
        validateBookId(bookId);

        Book existingBook = findBookById(bookId);
        Librarian librarian = findLibrarianById(bookRequest.getLibrarianId());
        Set<Member> members = findMembersByIds(bookRequest.getMemberIds());

        updateBookFields(existingBook, bookRequest, librarian, members);
        Book updatedBook = bookRepository.save(existingBook);
        return BookMapper.mapToBookResponse(updatedBook);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param bookId The ID of the book to delete.
     * @throws ResourceNotFoundException if the book is not found.
     */
    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        Book book = findBookById(bookId);
        bookRepository.delete(book);
    }

    /**
     * Allows a librarian to borrow a book for a member.
     * Checks the availability of the book before proceeding with the borrowing process.
     *
     * @param bookId      The ID of the book to be borrowed.
     * @param memberId    The ID of the member who wants to borrow the book.
     * @param librarianId The ID of the librarian processing the borrow request.
     * @return Updated {@link BookResponse} representing the borrowed book.
     * @throws ResourceNotFoundException   if the book, member, or librarian is not found.
     * @throws ResourceBadRequestException if the book is not available for borrowing.
     */
    @Transactional
    public BookResponse borrowBook(Long bookId, Long memberId, Long librarianId) {
        Book book = findBookById(bookId);

        if (!book.isAvailable()) {
            throw new ResourceBadRequestException("Book is not available for borrowing.");
        }

        // Retrieve member by ID and handle Optional
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + memberId));

        Librarian librarian = findLibrarianById(librarianId);

        // Add member to the book and update status
        book.addBookMember(member);
        book.setStatus(BookStatus.BORROWED);
        book.setLibrarian(librarian);

        // Save the updated book
        bookRepository.save(book);

        // Return the mapped BookResponse
        return BookMapper.mapToBookResponse(book);
    }

    /**
     * Checks if a book is available for borrowing.
     *
     * @param bookId The ID of the book to check.
     * @return {@code true} if the book is available, {@code false} otherwise.
     */
    @Transactional(readOnly = true)
    public boolean isBookAvailable(Long bookId) {
        Book book = findBookById(bookId);
        return book.isAvailable();
    }

    /**
     * Returns a borrowed book back to the library.
     *
     * @param bookId      The ID of the book being returned.
     * @param memberId    The ID of the member returning the book.
     * @param librarianId The ID of the librarian processing the return.
     * @return Updated {@link BookResponse} after the book is returned.
     * @throws ResourceNotFoundException   if the book, member, or librarian is not found.
     * @throws ResourceBadRequestException if the book is not currently borrowed by the member.
     */
    @Transactional
    public BookResponse returnBook(Long bookId, Long memberId, Long librarianId) {
        Book book = findBookById(bookId);
        Member member = memberRepository.getReferenceById(memberId);
        Librarian librarian = findLibrarianById(librarianId);

        if (!book.isBorrowed() || !book.isReservedBy(member)) {
            throw new ResourceBadRequestException("Book is not borrowed by this member.");
        }

        book.getMembers().remove(member);
        member.getBorrowedBooks().remove(book);

        book.setStatus(BookStatus.AVAILABLE);
        book.setLibrarian(librarian);

        bookRepository.save(book);
        return BookMapper.mapToBookResponse(book);
    }

    // Helper Methods

    /**
     * Validates the book request.
     *
     * @param bookRequest The book request to validate.
     * @throws ResourceBadRequestException if the request is null.
     */
    private void validateBookRequest(BookRequest bookRequest) {
        if (bookRequest == null) {
            log.error("Book request is null");
            throw new ResourceBadRequestException("Book request cannot be null");
        }
    }

    /**
     * Validates the book ID.
     *
     * @param bookId The ID to validate.
     * @throws ResourceBadRequestException if the ID is invalid.
     */
    private void validateBookId(Long bookId) {
        if (bookId == null || bookId <= 0) {
            throw new ResourceBadRequestException("Invalid book ID");
        }
    }

    /**
     * Finds a book by ID or throws a ResourceNotFoundException.
     *
     * @param bookId The ID of the book.
     * @return Book entity if found.
     */
    private Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> resourceNotFoundException(LIBRARY_BOOK_NOT_FOUND, bookId));
    }

    /**
     * Finds a librarian by ID.
     *
     * @param librarianId The ID of the librarian.
     * @return Librarian entity.
     * @throws ResourceNotFoundException if librarian is not found.
     */
    private Librarian findLibrarianById(Long librarianId) {
        return librarianRepository.findById(librarianId)
                .orElseThrow(() -> resourceNotFoundException(LIBRARY_LIBRARIAN_NOT_FOUND, librarianId));
    }

    /**
     * Finds members by their IDs.
     *
     * @param memberIds List of member IDs.
     * @return Set of Member entities.
     * @throws ResourceNotFoundException if any member is not found.
     */
    private Set<Member> findMembersByIds(Set<Long> memberIds) {
        return memberRepository.findMemberByIdIn(memberIds)
                .orElseThrow(() -> resourceNotFoundException(LIBRARY_MEMBERS_NOT_FOUND, memberIds));
    }

    /**
     * Updates the book entity with new details.
     *
     * @param book        The book entity to update.
     * @param bookRequest The new book details.
     * @param librarian   The updated librarian.
     * @param members     The updated members list.
     */
    private void updateBookFields(Book book, BookRequest bookRequest, Librarian librarian, Set<Member> members) {
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setIsbn(bookRequest.getIsbn());
        book.setGenre(bookRequest.getGenre());
        book.setPublisherName(bookRequest.getPublisherName());
        book.setPublisherYear(bookRequest.getPublisherYear());
        book.setStatus(bookRequest.getStatus());
        book.setLibrarian(librarian);
        book.setMembers(members);
    }

    /**
     * Logs and returns a ResourceNotFoundException with the provided message.
     *
     * @param messageKey The message key to retrieve.
     * @param args       Arguments for the message format.
     * @return ResourceNotFoundException
     */
    private ResourceNotFoundException resourceNotFoundException(String messageKey, Object... args) {
        String message = MessageFormat.format(MessageProvider.getMessage(messageKey), args);
        log.error(message);
        return new ResourceNotFoundException(message);
    }
}
