package code.with.vanilson.libraryapplication.fine;

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminResponse;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianResponse;
import code.with.vanilson.libraryapplication.member.Member;
import code.with.vanilson.libraryapplication.member.MemberResponse;
import code.with.vanilson.libraryapplication.person.AddressDTO;

import static code.with.vanilson.libraryapplication.admin.AdminMapper.mapToAddressDTO;

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
            throw new ResourceBadRequestException("library.admin.cannot_be_null");
        }
        var adminResponse = new AdminResponse();
        adminResponse.setId(admin.getId());
        adminResponse.setName(admin.getName());
        adminResponse.setEmail(admin.getEmail());
        adminResponse.setAddress(mapToAddressDTO(admin.getAddress()));
        adminResponse.setContact(admin.getContact());
        adminResponse.setAdminCode(admin.getAdminCode());
        adminResponse.setRole(admin.getRole());

        return adminResponse;
    }

    private static MemberResponse mapToMemberResponse(Member member) {
        if (member == null) {
            throw new ResourceBadRequestException("library.member.cannot_be_null");
        }
        var memberResponse = new MemberResponse();
        memberResponse.setId(member.getId());
        memberResponse.setName(member.getName());
        memberResponse.setEmail(member.getEmail());
        memberResponse.setAddress(getAddressDTO(member));
        memberResponse.setContact(member.getContact());
        memberResponse.setMembershipStatus(member.getMembershipStatus());
        return memberResponse;
    }

    private static LibrarianResponse mapToLibrarianResponse(Librarian librarian) {
        if (librarian == null) {
            throw new ResourceBadRequestException("library.librarian.cannot_be_null");
        }
        var librarianResponse = new LibrarianResponse();
        librarianResponse.setId(librarian.getId());
        librarianResponse.setName(librarian.getName());
        librarianResponse.setEmail(librarian.getEmail());
        librarianResponse.setAddress(mapToAddressDTO(librarian.getAddress()));
        librarianResponse.setContact(librarian.getContact());
        librarianResponse.setEmployeeCode(librarian.getEmployeeCode());
        librarianResponse.setAdmin(mapToAdminResponse(librarian.getAdmin()));
        return librarianResponse;
    }

    private static AddressDTO getAddressDTO(Member member) {
        // Assuming AddressDTO has similar fields in Member entity
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet(member.getAddress().getStreet());
        addressDTO.setCity(member.getAddress().getCity());
        addressDTO.setState(member.getAddress().getState());
        addressDTO.setCountry(member.getAddress().getCountry());
        addressDTO.setPostalCode(member.getAddress().getPostalCode());
        return addressDTO;
    }

}