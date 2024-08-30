package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.Member.Member;
import code.with.vanilson.libraryapplication.Member.MemberDTO;
import code.with.vanilson.libraryapplication.Person.Address;
import code.with.vanilson.libraryapplication.Person.AddressDTO;
import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminDTO;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.utils.MessageProvider;
import code.with.vanilson.libraryapplication.fine.Fine;
import code.with.vanilson.libraryapplication.fine.FineDTO;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianDTO;

import java.text.MessageFormat;
import java.util.HashSet;
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

    public static Book mapToBookEntity(BookRequest request, Librarian librarian, Set<Member> members) {
        // Validate inputs and throw exceptions if necessary
        validateNotNull(request, "library.book.cannot_be_null");
        validateNotNull(librarian, "library.librarian.cannot_be_null");
        validateNotNull(members, "library.members.cannot_be_null");

        return Book.builder()
                .bookId(request.getId())
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .genre(request.getGenre())
                .publisherName(request.getPublisherName())
                .publisherYear(request.getPublisherYear())
                .status(request.getStatus())
                .members(members)
                .librarian(librarian)
                .build();
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

    /**
     * Converts a Book entity to a BookResponse object.
     *
     * @param book The Book entity to be converted.
     * @return A BookResponse object containing the details of the Book entity.
     * @throws ResourceBadRequestException If the book parameter is null.
     */

    // Convert Book entity to BookResponse
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
                .librarian(mapToLibrarianDTO(book.getLibrarian())) // Map entire Librarian to LibrarianDTO
                .members(book.getMembers().stream()
                        .map(BookMapper::mapToMemberDTO) // Map each Member to MemberDTO
                        .collect(Collectors.toSet()))
                .build();
    }

    /**
     * Converts a Librarian entity to a LibrarianDTO object.
     *
     * @param librarian The Librarian entity to be converted.
     * @return A LibrarianDTO object containing the details of the Librarian entity.
     * @throws ResourceBadRequestException If the librarian parameter is null.
     */

    // Convert Book entity to BookDTO
    private static LibrarianDTO mapToLibrarianDTO(Librarian librarian) {
        validateNotNull(librarian, "library.librarian.cannot_be_null");
        return LibrarianDTO.builder()
                .id(librarian.getId())
                .name(librarian.getName())
                .email(librarian.getEmail())
                .address(mapToAddressDTO(librarian.getAddress()))
                .contact(librarian.getContact())
                .employeeCode(librarian.getEmployeeCode())
                .managedBooksIds(librarian.getManagedBooks() != null ?
                        librarian.getManagedBooks().stream().map(Book::getBookId).collect(Collectors.toSet()) :
                        new HashSet<>())
                .membersIds(librarian.getMembers() != null ?
                        librarian.getMembers().stream().map(Member::getId).collect(Collectors.toSet()) :
                        new HashSet<>())
                .admin(mapToAdminDTO(librarian.getAdmin()))
                .build();
    }

    private static AddressDTO mapToAddressDTO(Address address) {
        if (address == null) {
            throw new ResourceBadRequestException("Address cannot be null"); // Or handle this case as needed
        }
        return AddressDTO.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }

    /**
     * Maps a Book entity to a BookDTO object.
     *
     * @param book The Book entity to be converted.
     * @return A BookDTO object containing the details of the Book entity.
     * @throws ResourceBadRequestException If the book parameter is null.
     */
    private static BookDTO mapToBookDTO(Book book) {
        validateNotNull(book, "library.book.cannot_be_null");
        return BookDTO.builder()
                .id(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .genre(book.getGenre())
                .publisherName(book.getPublisherName())
                .publisherYear(book.getPublisherYear())
                .status(book.getStatus())
                .librarianId(book.getLibrarian() != null ? book.getLibrarian().getId() : 0L) // Include only ID
                .memberIds(book.getMembers() != null ?
                        book.getMembers().stream().map(Member::getId).collect(Collectors.toSet()) : new HashSet<>())
                .build();
    }

    /**
     * Maps a Fine entity to a FineDTO object.
     *
     * @param fine The Fine entity to be converted.
     * @return A FineDTO object containing the details of the Fine entity.
     * @throws ResourceBadRequestException If the fine parameter is null.
     */

    private static FineDTO mapToFineDTO(Fine fine) {
        validateNotNull(fine, "fine.cannot_be_null");
        return FineDTO.builder()
                .id(fine.getId())
                .amount(fine.getAmount())
                .issueDate(fine.getIssueDate())
                .dueDate(fine.getDueDate())
                .memberId(fine.getMember() != null ? fine.getMember().getId() : 0L)
                .librarianId(fine.getLibrarian() != null ? fine.getLibrarian().getId() : 0L)
                .adminId(fine.getAdmin() != null ? fine.getAdmin().getId() : 0L)
                .build();
    }

    /**
     * Maps an Admin entity to an AdminDTO object.
     *
     * @param admin The Admin entity to be converted.
     * @return A new AdminDTO object containing the details of the Admin entity.
     * Returns null if the admin parameter is null.
     * @throws ResourceBadRequestException If the admin's address, managed librarians, managed members, or managed fines are null.
     */

    private static AdminDTO mapToAdminDTO(Admin admin) {
        validateNotNull(admin, "library.admin.cannot_be_null");
        return AdminDTO.builder()
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .address(mapToAddressDTO(admin.getAddress()))
                .contact(admin.getContact())
                .adminCode(admin.getAdminCode())
                .role(admin.getRole())
                .managedLibrariansIds(admin.getManagedLibrarians() != null ?
                        admin.getManagedLibrarians().stream().map(Librarian::getId).collect(Collectors.toSet()) :
                        new HashSet<>())
                .managedMembersIds(admin.getManagedMembers() != null ?
                        admin.getManagedMembers().stream().map(Member::getId).collect(Collectors.toSet()) :
                        new HashSet<>())
                .managedFinesIds(admin.getManagedFines() != null ?
                        admin.getManagedFines().stream().map(Fine::getId).collect(Collectors.toSet()) : new HashSet<>())
                .build();
    }

    /**
     * Maps a Member entity to a MemberDTO object.
     *
     * @param member The Member entity to be converted.
     * @return A new MemberDTO object containing the details of the Member entity.
     * @throws ResourceBadRequestException If the member parameter is null.
     */

    private static MemberDTO mapToMemberDTO(Member member) {
        validateNotNull(member, "library.member.cannot_be_null");
        return MemberDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .address(mapToAddressDTO(member.getAddress()))
                .contact(member.getContact())
                .membershipStatus(member.getMembershipStatus())
                .borrowedBooksIds(member.getBorrowedBooks() != null ?
                        member.getBorrowedBooks().stream().map(Book::getBookId).collect(Collectors.toSet()) :
                        new HashSet<>())
                .librarianId(member.getLibrarian() != null ? member.getLibrarian().getId() : 0L)
                .adminId(member.getAdmin() != null ? member.getAdmin().getId() : 0L)
                .build();
    }

}