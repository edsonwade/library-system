package code.with.vanilson.libraryapplication.unit.book;

import code.with.vanilson.libraryapplication.TestDataHelper;
import code.with.vanilson.libraryapplication.book.*;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianRepository;
import code.with.vanilson.libraryapplication.member.Member;
import code.with.vanilson.libraryapplication.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Book Service Test")
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    private static final long BOOK_ID = 1L;
    private static final long MEMBER_ID = 1L;
    private static final long LIBRARIAN_ID = 1L;
    private static final long INVALID_ID = 0L;

    private BookRepository bookRepository;
    private MemberRepository memberRepository;
    private LibrarianRepository librarianRepository;
    private BookService bookService;
    private TestDataHelper testDataHelper;
    private Book book;
    private BookRequest bookRequest;
    private BookResponse bookResponse;
    private Member member;
    private Librarian librarian;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        memberRepository = mock(MemberRepository.class);
        librarianRepository = mock(LibrarianRepository.class);
        bookService = new BookService(bookRepository, memberRepository, librarianRepository);
        testDataHelper = new TestDataHelper();

        // Create test data
        setupTestData();
    }

    private void setupTestData() {
        // Create a book
        book = new Book();
        book.setBookId(BOOK_ID);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setGenre("Test Genre");
        book.setPublisherName("Test Publisher");
        book.setPublisherYear(2023);
        book.setStatus(BookStatus.AVAILABLE);

        // Create a member
        member = new Member();
        member.setId(MEMBER_ID);
        member.setName("Test Member");

        // Create a librarian
        librarian = new Librarian();
        librarian.setId(LIBRARIAN_ID);
        librarian.setName("Test Librarian");

        // Create book request
        bookRequest = new BookRequest();
        bookRequest.setTitle("Test Book");
        bookRequest.setAuthor("Test Author");
        bookRequest.setIsbn("1234567890");
        bookRequest.setGenre("Test Genre");
        bookRequest.setPublisherName("Test Publisher");
        bookRequest.setPublisherYear(2023);
        bookRequest.setStatus(BookStatus.AVAILABLE);
        bookRequest.setLibrarianId(LIBRARIAN_ID);
        bookRequest.setMemberIds(Set.of(MEMBER_ID));

        // Create book response using builder pattern
        bookResponse = BookResponse.builder()
                .id(BOOK_ID)
                .title("Test Book")
                .author("Test Author")
                .isbn("1234567890")
                .genre("Test Genre")
                .publisherName("Test Publisher")
                .publisherYear(2023)
                .status(BookStatus.AVAILABLE)
                .build();
    }

    @Test
    @DisplayName("Should return all books successfully")
    void shouldReturnAllBooks_WhenRepositoryFindsAll() {
        // Given
        when(bookRepository.findAll()).thenReturn(List.of(book));

        try (MockedStatic<BookMapper> mockedBookMapper = mockStatic(BookMapper.class)) {
            mockedBookMapper.when(() -> BookMapper.mapToBookResponse(any(Book.class))).thenReturn(bookResponse);

            // When
            List<BookResponse> result = bookService.getAllBooks();

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(BOOK_ID, result.get(0).getId());
            assertEquals("Test Book", result.get(0).getTitle());
            verify(bookRepository, times(1)).findAll();
            mockedBookMapper.verify(() -> BookMapper.mapToBookResponse(any(Book.class)), times(1));
        }
    }

    @Test
    @DisplayName("Should return empty list when no books exist")
    void shouldReturnEmptyList_WhenNoBookExists() {
        // Given
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<BookResponse> result = bookService.getAllBooks();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return book by ID successfully")
    void shouldReturnBookById_WhenIdIsValid() {
        // Given
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

        try (MockedStatic<BookMapper> mockedBookMapper = mockStatic(BookMapper.class)) {
            mockedBookMapper.when(() -> BookMapper.mapToBookResponse(any(Book.class))).thenReturn(bookResponse);

            // When
            BookResponse result = bookService.getBookById(BOOK_ID);

            // Then
            assertNotNull(result);
            assertEquals(BOOK_ID, result.getId());
            assertEquals("Test Book", result.getTitle());
            verify(bookRepository, times(1)).findById(BOOK_ID);
            mockedBookMapper.verify(() -> BookMapper.mapToBookResponse(any(Book.class)), times(1));
        }
    }

    @Test
    @DisplayName("Should throw exception when book ID is invalid")
    void shouldThrowException_WhenBookIdIsInvalid() {
        // When & Then
        assertThrows(ResourceBadRequestException.class, () -> bookService.getBookById(INVALID_ID));
        verify(bookRepository, never()).findById(INVALID_ID);
    }

    @Test
    @DisplayName("Should throw exception when book is not found")
    void shouldThrowException_WhenBookIsNotFound() {
        // Given
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(BOOK_ID));
        verify(bookRepository, times(1)).findById(BOOK_ID);
    }

    @Test
    @DisplayName("Should create book successfully")
    @Disabled
    void shouldCreateBook_WhenRequestIsValid() {
        // Given
        when(librarianRepository.findById(LIBRARIAN_ID)).thenReturn(Optional.of(librarian));
        when(memberRepository.findMemberByIdIn(Set.of(MEMBER_ID))).thenReturn(Optional.of(Set.of(member)));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        BookResponse result = bookService.createBook(bookRequest);

        // Then
        assertNotNull(result);
        assertEquals(BOOK_ID, result.getId());
        assertEquals("Test Book", result.getTitle());
        verify(librarianRepository, times(1)).findById(LIBRARIAN_ID);
        verify(memberRepository, times(1)).findMemberByIdIn(Set.of(MEMBER_ID));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should throw exception when book request is null")
    void shouldThrowException_WhenBookRequestIsNull() {
        // When & Then
        assertThrows(ResourceBadRequestException.class, () -> bookService.createBook(null));
        verify(librarianRepository, never()).findById(anyLong());
        verify(memberRepository, never()).findMemberByIdIn(anySet());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Should update book successfully")
    @Disabled
    void shouldUpdateBook_WhenRequestIsValid() {
        // Given
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(librarianRepository.findById(LIBRARIAN_ID)).thenReturn(Optional.of(librarian));
        when(memberRepository.findMemberByIdIn(Set.of(MEMBER_ID))).thenReturn(Optional.of(Set.of(member)));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        BookResponse result = bookService.updateBook(bookRequest, BOOK_ID);

        // Then
        assertNotNull(result);
        assertEquals(BOOK_ID, result.getId());
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(librarianRepository, times(1)).findById(LIBRARIAN_ID);
        verify(memberRepository, times(1)).findMemberByIdIn(Set.of(MEMBER_ID));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should delete book successfully")
    void shouldDeleteBook_WhenIdIsValid() {
        // Given
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

        // When
        bookService.deleteBook(BOOK_ID);

        // Then
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    @DisplayName("Should check if book is available successfully")
    void shouldCheckIfBookIsAvailable_WhenBookExists() {
        // Given
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

        // When
        boolean result = bookService.isBookAvailable(BOOK_ID);

        // Then
        assertTrue(result);
        verify(bookRepository, times(1)).findById(BOOK_ID);
    }

    @Test
    @DisplayName("Should return false when book is not available")
    void shouldReturnFalse_WhenBookIsNotAvailable() {
        // Given
        book.setStatus(BookStatus.BORROWED);
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

        // When
        boolean result = bookService.isBookAvailable(BOOK_ID);

        // Then
        assertFalse(result);
        verify(bookRepository, times(1)).findById(BOOK_ID);
    }

    @Test
    @Disabled
    @DisplayName("Should borrow book successfully")
    void shouldBorrowBook_WhenBookIsAvailable() {
        // Given
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member));
        when(librarianRepository.findById(LIBRARIAN_ID)).thenReturn(Optional.of(librarian));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        BookResponse result = bookService.borrowBook(BOOK_ID, MEMBER_ID, LIBRARIAN_ID);

        // Then
        assertNotNull(result);
        assertEquals(BOOK_ID, result.getId());
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(memberRepository, times(1)).findById(MEMBER_ID);
        verify(librarianRepository, times(1)).findById(LIBRARIAN_ID);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should throw exception when borrowing unavailable book")
    void shouldThrowException_WhenBorrowingUnavailableBook() {
        // Given
        book.setStatus(BookStatus.BORROWED);
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

        // When & Then
        assertThrows(ResourceBadRequestException.class, () -> bookService.borrowBook(BOOK_ID, MEMBER_ID, LIBRARIAN_ID));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(memberRepository, never()).findById(anyLong());
        verify(librarianRepository, never()).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Should return book successfully")
    @Disabled
    void shouldReturnBook_WhenBookIsBorrowed() {
        // Given
        book.setStatus(BookStatus.BORROWED);
        Set<Member> members = new HashSet<>();
        members.add(member);
        book.setMembers(members);

        Set<Book> books = new HashSet<>();
        books.add(book);
        member.setBorrowedBooks(books);

        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(memberRepository.getReferenceById(MEMBER_ID)).thenReturn(member);
        when(librarianRepository.findById(LIBRARIAN_ID)).thenReturn(Optional.of(librarian));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        BookResponse result = bookService.returnBook(BOOK_ID, MEMBER_ID, LIBRARIAN_ID);

        // Then
        assertNotNull(result);
        assertEquals(BOOK_ID, result.getId());
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(memberRepository, times(1)).getReferenceById(MEMBER_ID);
        verify(librarianRepository, times(1)).findById(LIBRARIAN_ID);
        verify(bookRepository, times(1)).save(any(Book.class));
    }
}
