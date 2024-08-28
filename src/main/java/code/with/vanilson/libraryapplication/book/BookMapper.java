package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.Member.Member;
import code.with.vanilson.libraryapplication.librarian.Librarian;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * BookMapper
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
public class BookMapper {


    public static Book mapToBookEntity(BookRequest request, Librarian librarian, Member member) {
        if (null == request) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        return Book.builder()
                .id(request.getId())
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .genre(request.getGenre())
                .publisherName(request.getPublisherName())
                .publisherYear(request.getPublisherYear())
                .status(request.getStatus())
                .librarian(librarian)
                .members(Set.of(member))  // Assuming a single member for simplicity
                .build();
    }


    // Convert Book entity to BookResponse
    public static BookResponse mapToBookResponse(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .genre(book.getGenre())
                .publisherName(book.getPublisherName())
                .publisherYear(book.getPublisherYear())
                .status(book.getStatus())
                .librarian(book.getLibrarian().getId())
                .members(book.getMembers().iterator().next().getId())  // Assuming a single member
                .build();
    }


    // Convert Book entity to BookDTO
    public static BookDTO mapToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setGenre(book.getGenre());
        dto.setPublisherName(book.getPublisherName());
        dto.setPublisherYear(book.getPublisherYear());
        dto.setStatus(book.getStatus());

        // Set member IDs
        Set<Long> memberIds = book.getMembers().stream()
                .map(Member::getId)
                .collect(Collectors.toSet());
        dto.setMemberIds(memberIds);

        // Set librarian ID
        dto.setLibrarianId(book.getLibrarian() != null ? book.getLibrarian().getId() : null);

        return dto;
    }

}