package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.admin.AdminMapper;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.utils.MessageProvider;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianMapper;
import code.with.vanilson.libraryapplication.member.Member;
import code.with.vanilson.libraryapplication.member.MemberResponse;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.AddressDTO;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * BookMapper
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@SuppressWarnings("unused")
public class BookMapper {

    private BookMapper() {
        // default
    }

    // Converts BookRequest to Book entity
    public static Book mapToBookEntity(BookRequest request, Librarian librarian, Set<Member> members) {
        validateNotNull(request, "library.book.cannot_be_null");
        validateNotNull(librarian, "library.librarian.cannot_be_null");
        validateNotNull(members, "library.member.cannot_be_null");

        Book book = getBook(request, librarian, members);

        // Synchronize the relationship by adding the book to each member's borrowedBooks set
        for (Member member : members) {
            member.getBorrowedBooks()
                    .add(book);
        }

        return book;
    }

    private static Book getBook(BookRequest request, Librarian librarian, Set<Member> members) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setGenre(request.getGenre());
        book.setPublisherName(request.getPublisherName());
        book.setPublisherYear(request.getPublisherYear());
        book.setLibrarian(librarian);
        book.setMembers(members);
        book.setStatus(request.getStatus());
        return book;
    }

    // Converts Book entity to BookResponse
    public static BookResponse mapToBookResponse(Book book) {
        validateNotNull(book, "library.book.cannot_be_null");

        return BookResponse.builder()
                .id(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .genre(book.getGenre())
                .publisherName(book.getPublisherName())
                .publisherYear(book.getPublisherYear())
                .status(book.getStatus())
                .librarian(LibrarianMapper.mapToLibrarianResponse(book.getLibrarian()))
                .members(mapToMemberResponse(book.getMembers()))
                .build();
    }

    // Converts Set<Member> to Set<MemberResponse>
    private static Set<MemberResponse> mapToMemberResponse(Set<Member> members) {
        if (members == null) {
            throw new ResourceBadRequestException("Members cannot be null");
        }

        Set<MemberResponse> memberResponses = new HashSet<>();
        for (Member member : members) {
            MemberResponse memberResponse = new MemberResponse();
            memberResponse.setId(member.getId());
            memberResponse.setName(member.getName());
            memberResponse.setEmail(member.getEmail());
            memberResponse.setAddress(mapToAddressDTO(member.getAddress()));
            memberResponse.setContact(member.getContact());
            memberResponse.setMembershipStatus(member.getMembershipStatus());
            // Assuming these methods exist to map responses
            memberResponse.setLibrarianResponse(LibrarianMapper.mapToLibrarianResponse(member.getLibrarian()));
            memberResponse.setAdminResponse(AdminMapper.mapToAdminResponse(member.getAdmin()));
            memberResponses.add(memberResponse);
        }
        return memberResponses;
    }

    private static AddressDTO mapToAddressDTO(Address address) {
        return AdminMapper.mapToAddressDTO(address); // Use appropriate address mapping
    }

    /**
     * Validates if an object is not null. If the object is null, it throws a {@link ResourceBadRequestException} with a formatted error message.
     *
     * @param object     The object to be validated.
     * @param messageKey The key of the error message to be retrieved from the {@link MessageProvider}.
     * @throws ResourceBadRequestException If the object is null.
     */
    // Utility method for null checks
    private static void validateNotNull(Object object, String messageKey) {
        if (object == null) {
            String errorMessage = MessageFormat.format(
                    MessageProvider.getMessage(messageKey), "null");
            throw new ResourceBadRequestException(errorMessage);
        }
    }
}
