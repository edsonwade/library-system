package code.with.vanilson.libraryapplication.fine;

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminResponse;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianResponse;
import code.with.vanilson.libraryapplication.member.Member;
import code.with.vanilson.libraryapplication.member.MemberResponse;
import code.with.vanilson.libraryapplication.person.AddressDTO;

/**
 * FineMapper
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-09-10
 */
public class FineMapper {

    private FineMapper() {
        // empty constructor
    }

    public static FineResponse toResponse(Fine fine) {
        return FineResponse.builder()
                .id(fine.getId())
                .amount(fine.getAmount())
                .issueDate(fine.getIssueDate())
                .dueDate(fine.getDueDate())
                .isPaid(fine.getIsPaid())
                .memberId(mapToMemberResponse(fine.getMember()))
                .librarianId(mapToLibrarianResponse(fine.getLibrarian()))
                .adminId(mapToAdminResponse(fine.getAdmin()))
                .build();
    }

    private static AdminResponse mapToAdminResponse(Admin admin) {
        if (admin == null) {
            return null;
        }

        var adminResponse = new AdminResponse();
        adminResponse.setId(admin.getId());
        adminResponse.setName(admin.getName());
        adminResponse.setEmail(admin.getEmail());

        // Assuming AddressDTO has similar fields in Admin entity
        var addressDTO = new AddressDTO();
        addressDTO.setStreet(admin.getAddress().getStreet());
        addressDTO.setCity(admin.getAddress().getCity());
        addressDTO.setState(admin.getAddress().getState());
        addressDTO.setCountry(admin.getAddress().getCountry());
        addressDTO.setPostalCode(admin.getAddress().getPostalCode());

        // Set other address fields

        adminResponse.setAddress(addressDTO);
        adminResponse.setContact(admin.getContact());
        adminResponse.setAdminCode(admin.getAdminCode());
        adminResponse.setRole(admin.getRole());

        return adminResponse;
    }

    private static MemberResponse mapToMemberResponse(Member member) {
        if (member == null) {
            return null;
        }

        var memberResponse = new MemberResponse();
        memberResponse.setId(member.getId());
        memberResponse.setName(member.getName());
        memberResponse.setEmail(member.getEmail());

        // Assuming AddressDTO has similar fields in Member entity
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet(member.getAddress().getStreet());
        addressDTO.setCity(member.getAddress().getCity());
        addressDTO.setState(member.getAddress().getState());
        addressDTO.setCountry(member.getAddress().getCountry());
        addressDTO.setPostalCode(member.getAddress().getPostalCode());

        // Set other address fields

        memberResponse.setAddress(addressDTO);

        memberResponse.setContact(member.getContact());
        memberResponse.setMembershipStatus(member.getMembershipStatus());

        // Map associated entities if needed (e.g., BookResponse, LibrarianResponse, AdminResponse)

        return memberResponse;
    }

    private static LibrarianResponse mapToLibrarianResponse(Librarian librarian) {
        if (librarian == null) {
            return null;
        }

        var librarianResponse = new LibrarianResponse();
        librarianResponse.setId(librarian.getId());
        librarianResponse.setName(librarian.getName());
        librarianResponse.setEmail(librarian.getEmail());

        // Assuming AddressDTO has similar fields in Librarian entity
        var addressDTO = new AddressDTO();
        addressDTO.setStreet(librarian.getAddress().getStreet());
        addressDTO.setCity(librarian.getAddress().getCity());
        addressDTO.setState(librarian.getAddress().getState());
        addressDTO.setCountry(librarian.getAddress().getCountry());
        addressDTO.setPostalCode(librarian.getAddress().getPostalCode());

        // Set other address fields

        librarianResponse.setAddress(addressDTO);

        librarianResponse.setContact(librarian.getContact());
        librarianResponse.setEmployeeCode(librarian.getEmployeeCode());

        // Map the associated Admin entity to AdminResponse
        librarianResponse.setAdmin(mapToAdminResponse(librarian.getAdmin()));

        return librarianResponse;
    }
}