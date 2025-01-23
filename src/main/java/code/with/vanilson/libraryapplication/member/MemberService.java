package code.with.vanilson.libraryapplication.member;

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminRepository;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceConflictException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

import static code.with.vanilson.libraryapplication.admin.AdminMapper.mapToAddress;
import static code.with.vanilson.libraryapplication.common.utils.MessageProvider.getMessage;
import static code.with.vanilson.libraryapplication.member.MemberMapper.mapToMemberResponse;

/**
 * MemberService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-09-06
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class MemberService implements IMember {

    public static final String LIBRARY_MEMBERS_NOT_FOUND = "library.members.not_found";
    private final MemberRepository memberRepository;
    private final LibrarianRepository librarianRepository;
    private final AdminRepository adminRepository;

    public MemberService(MemberRepository memberRepository, LibrarianRepository librarianRepository,
                         AdminRepository adminRepository) {
        this.memberRepository = memberRepository;
        this.librarianRepository = librarianRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponse> getAllMembers() {
        log.info("Get all member");
        return memberRepository
                .findAll()
                .stream()
                .map(MemberMapper::mapToMemberResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMemberById(Long id) {
        validateLibrarianIdIsPositive(id);
        return fetchMemberDetailsById(id);
    }

    @Override
    public MemberResponse getMemberByEmail(String email) {
        // TODO: implement the logic here
        return memberRepository.findMemberByEmail(email)
                .map(MemberMapper::mapToMemberResponse)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("library.members.email_not_found", email)));

    }

    @Transactional
    @Override
    public MemberResponse createMember(MemberRequest memberRequest) {
        if (null == memberRequest) {
            throw new ResourceBadRequestException("library.member.cannot_be_null");
        }
        var admin = fetchAssociatedAdmin(memberRequest);
        var librarian = fetchAssociatedLibrarian(memberRequest);

        try {
            validateAndCheckMemberUniqueFieldsForUpdate(memberRequest);
            var member = MemberMapper.mapToMemberEntity(memberRequest, admin, librarian);
            var memberSaved = memberRepository.save(member);
            return mapToMemberResponse(memberSaved);

        } catch (DataIntegrityViolationException e) {
            var errorMessage = MessageFormat.format(getMessage("library.member.email_and_contact_already_exists"),
                    memberRequest.getEmail(), memberRequest.getContact());
            throw new ResourceConflictException(errorMessage);
        }

    }

    @Override
    @Transactional
    public MemberResponse updateMember(MemberRequest memberRequest, Long memberId) {
        if (null == memberRequest) {
            throw new ResourceBadRequestException("library.member.cannot_be_null");
        }
        validateLibrarianIdIsPositive(memberId);
        var existingMember = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageFormat.format(getMessage(LIBRARY_MEMBERS_NOT_FOUND), memberId)));

        var admin = fetchAssociatedAdmin(memberRequest);
        var librarian = fetchAssociatedLibrarian(memberRequest);
        validateAndCheckMemberUniqueFieldsForUpdate(memberRequest);

        // Update fields on the existing entity
        existingMember.setName(memberRequest.getName());
        existingMember.setEmail(memberRequest.getEmail());
        existingMember.setAddress(mapToAddress(memberRequest.getAddress()));
        existingMember.setContact(memberRequest.getContact());
        existingMember.setMembershipStatus(memberRequest.getMembershipStatus());
        existingMember.setAdmin(admin);
        existingMember.setLibrarian(librarian);

        // Save updated entity
        var updatedMember = memberRepository.save(existingMember);

        return MemberMapper.mapToMemberResponse(updatedMember);

    }

    @Override
    @Transactional
    public void deleteMemberById(Long memberId) {
        if (memberId <= 0) {
            var errorMessage = getMessage("library.member.bad_request", memberId);
            log.error("The member id provided is less than or equal to zero {} ", memberId);
            throw new ResourceBadRequestException(errorMessage);
        }

        // then validate if the member exists
        if (!memberRepository.existsById(memberId)) {
            var errorMessage = MessageFormat.format(getMessage(LIBRARY_MEMBERS_NOT_FOUND), memberId);
            loggerMessage(memberId);
            throw new ResourceNotFoundException(errorMessage);
        }

        // finally, delete the member
        memberRepository.deleteById(memberId);
        log.info("member deleted successfully with ID {}", memberId);

    }

    static void validateLibrarianIdIsPositive(Long memberId) {
        if (memberId <= 0) {
            var errorMessage = getMessage("library.member.bad_request", memberId);
            log.error("The librarian id provide is less than or equal to zero {} ", memberId);
            throw new ResourceBadRequestException(errorMessage);
        }
    }

    private MemberResponse fetchMemberDetailsById(Long librarianId) {
        return memberRepository.findById(librarianId)
                .map(MemberMapper::mapToMemberResponse)
                .orElseThrow(() -> {
                    var errorMessage = MessageFormat.format(getMessage(LIBRARY_MEMBERS_NOT_FOUND), librarianId);
                    loggerMessage(librarianId);
                    return new ResourceNotFoundException(errorMessage);
                });
    }

    private static void loggerMessage(Long memberId) {
        log.error("No member found with ID {}", memberId);
    }

    private Admin fetchAssociatedAdmin(MemberRequest memberRequest) {
        return adminRepository.findById(memberRequest.getAdminId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                MessageFormat.format(getMessage("library.admin.not_found"),
                                        memberRequest.getAdminId())));
    }

    private Librarian fetchAssociatedLibrarian(MemberRequest memberRequest) {
        return librarianRepository.findById(memberRequest.getLibrarianId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageFormat.format(getMessage("library.librarian.not_found"),
                                memberRequest.getLibrarianId())));
    }

    public void validateAndCheckMemberUniqueFieldsForUpdate(MemberRequest memberRequest) {
        // Exclude the current librarian's own fields from the uniqueness checks
        if (memberRepository.existsMemberByEmailAndIdNot(memberRequest.getEmail(), 0L)) {
            throw new ResourceConflictException("library.member.email_exists");
        }
        if (memberRepository.existsMemberByContactAndIdNot(memberRequest.getContact(), 0L)) {

            throw new ResourceConflictException("library.member.contact_exists");
        }
    }
}