package code.with.vanilson.libraryapplication.member;

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

import static code.with.vanilson.libraryapplication.admin.AdminMapper.*;
import static code.with.vanilson.libraryapplication.librarian.LibrarianMapper.mapToLibrarian;
import static code.with.vanilson.libraryapplication.librarian.LibrarianMapper.mapToLibrarianResponse;

/**
 * MemberMapper
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-09-06
 */
@SuppressWarnings("unused")
@Slf4j
public class MemberMapper {

    public static final String LIBRARY_MEMBER_CANNOT_BE_NULL = "library.member.cannot_be_null";

    private MemberMapper() {
        // Private constructor to prevent instantiation
    }

    // mapping methods for member response
    public static MemberResponse mapToMemberResponse(Member member) {
        if (null == member) {
            log.error("member is null %s".formatted((Object) null));
            throw new ResourceBadRequestException(LIBRARY_MEMBER_CANNOT_BE_NULL);
        }
        if (null == member.getLibrarian()) {
            log.error(MessageFormat.format("librarian is null {0}", (Object) null));
            throw new ResourceBadRequestException("library.member.association_must_exists");
        }

        if (null == member.getAdmin()) {
            log.error(MessageFormat.format("admin is null {0}", (Object) null));
            throw new ResourceBadRequestException("library.admin.association_must_exists");
        }
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .address(mapToAddressDTO(member.getAddress()))
                .contact(member.getContact())
                .membershipStatus(member.getMembershipStatus())
                .librarianResponse(mapToLibrarianResponse(member.getLibrarian()))
                .adminResponse(mapToAdminResponse(member.getAdmin()))
                .build();
    }

    // Maps LibrarianRequest DTO to Librarian entity
    public static Member mapToMemberEntity(MemberRequest memberRequest, Admin admin, Librarian librarian) {
        if (null == memberRequest) {
            log.error("Librarian is null %s".formatted((Object) null));
            throw new ResourceBadRequestException(LIBRARY_MEMBER_CANNOT_BE_NULL);
        }
        if (null == memberRequest.getLibrarianId()) {
            log.error(MessageFormat.format("librarian is null {0}", (Object) null));
            throw new ResourceBadRequestException("library.member.association_must_exists");
        }

        if (null == memberRequest.getAdminId()) {
            log.error(MessageFormat.format("admin is null {0}", (Object) null));
            throw new ResourceBadRequestException("library.admin.association_must_exists");
        }

        // Map the request DTO to the Librarian entity
        return new Member(
                memberRequest.getName(),
                memberRequest.getEmail(),
                mapToAddress(memberRequest.getAddress()), // Ensure this method maps the AddressDTO to Address entity
                memberRequest.getContact(),
                memberRequest.getMembershipStatus(),
                librarian,
                admin // Set the admin from the service layer
        );
    }

    public static Member mapToMember(MemberResponse memberResponse) {
        if (null == memberResponse) {
            log.error("Member is null %s".formatted((Object) null));
            throw new ResourceBadRequestException(LIBRARY_MEMBER_CANNOT_BE_NULL);
        }

        Member member = new Member();
        member.setId(memberResponse.getId());
        member.setMembershipStatus(memberResponse.getMembershipStatus());
        member.setLibrarian(mapToLibrarian(memberResponse.getLibrarianResponse()));
        member.setAdmin(mapToAdmin(memberResponse.getAdminResponse()));

        return member;
    }


}