package code.with.vanilson.libraryapplication.fine;

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminRepository;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianRepository;
import code.with.vanilson.libraryapplication.member.Member;
import code.with.vanilson.libraryapplication.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

import static code.with.vanilson.libraryapplication.common.utils.MessageProvider.getMessage;

/**
 * FineService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-09-10
 */
@Service
@Slf4j
public class FineService {

    public static final String LIBRARY_FINE_NOT_FOUND = "library.fine.not_found";

    private final FineRepository fineRepository;

    private final MemberRepository memberService;

    private final LibrarianRepository librarianService;

    private final AdminRepository adminService;

    private final FineManagementService fineManagementService;

    public FineService(FineRepository fineRepository, MemberRepository memberService,
                       LibrarianRepository librarianService,
                       AdminRepository adminService, FineManagementService fineManagementService) {
        this.fineRepository = fineRepository;
        this.memberService = memberService;
        this.librarianService = librarianService;
        this.adminService = adminService;
        this.fineManagementService = fineManagementService;
    }

    @Transactional(readOnly = true)
    public List<FineResponse> getAllFines() {
        log.info("Retrieving all fines");
        return fineRepository.findAll()
                .stream()
                .map(FineMapper::toResponse)
                .toList();
    }

    public FineResponse getFineById(Long id) {
        return fineRepository
                .findById(id)
                .map(FineMapper::toResponse)
                .orElseThrow(() -> {
                    var errorMessage =
                            MessageFormat.format(getMessage(LIBRARY_FINE_NOT_FOUND), id);
                    return new ResourceNotFoundException(errorMessage);
                });
    }

    /**
     * Applies a fine to a member for returning a book overdue.
     *
     * @param request The request containing necessary information for applying the fine.
     * @return A FineResponse object representing the applied fine.
     * @throws ResourceNotFoundException If the member, librarian, or admin with the given IDs are not found.
     */
    @Transactional
    public FineResponse applyFine(FineRequest request) {
        log.info("Applying fine with request: {}", request);

        // Retrieve entities through repositories to ensure they are managed
        var member = getMember(request);
        var librarian = getLibrarian(request);
        var admin = getAdmin(request);

        log.info("Retrieved Entities - Member: {}, Librarian: {}, Admin: {}", member, librarian, admin);

        // Calculate fine
        Fine fine = fineManagementService.calculateFine(request.getIssueDate(), request.getDueDate(),
                request.getAmount(), member,
                librarian, admin,
                request.getIsPaid());

        // Save fine using repository
        fine = fineRepository.save(fine);
        log.info("Saved Fine entity: {}", fine);

        return FineMapper.toResponse(fine);
    }

    /**
     * Updates an existing fine record in the library system.
     *
     * @param id      The unique identifier of the fine to be updated.
     * @param request The request object containing the updated details for the fine.
     * @return A FineResponse object representing the updated fine.
     * @throws ResourceNotFoundException If a fine with the given ID is not found.
     */

    @Transactional
    public FineResponse updateFine(Long id, FineRequest request) {
        log.info("Attempting to update fine with id: {}", id);

        // Retrieve entities through repositories to ensure they are managed
        var member = getMember(request);
        var librarian = getLibrarian(request);
        var admin = getAdmin(request);

        // Retrieve and validate the existing fine
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Fine with id {} not found", id);
                    return new ResourceNotFoundException(LIBRARY_FINE_NOT_FOUND);
                });

        // Update fine details
        fine.setAmount(request.getAmount());
        fine.setIssueDate(request.getIssueDate());
        fine.setDueDate(request.getDueDate());
        fine.setIsPaid(request.getIsPaid());
        fine.setMember(member);
        fine.setLibrarian(librarian);
        fine.setAdmin(admin);

        // Save updated fine
        fine = fineRepository.save(fine);
        log.info("Fine with id {} has been updated successfully", id);

        return FineMapper.toResponse(fine);
    }

    @Transactional
    public void deleteFine(Long id) {
        log.info("Attempting to delete fine with id: {}", id);
        Fine fine = fineRepository.findById(id)
                .orElseThrow(() -> {
                    loggerInfo(id);
                    var errorMessage =
                            MessageFormat.format(getMessage(LIBRARY_FINE_NOT_FOUND), id);
                    return new ResourceNotFoundException(errorMessage);
                });

        fineRepository.delete(fine);
        log.info("Fine with id {} has been successfully deleted", id);
    }

    private Member getMember(FineRequest request) {
        return memberService.findById(request.getMemberId())
                .orElseThrow(() -> {
                    var errorMessage =
                            MessageFormat.format(getMessage("library.members.not_found"), request.getMemberId());
                    return new ResourceNotFoundException(errorMessage);
                });
    }

    private Admin getAdmin(FineRequest request) {
        return adminService.findById(request.getAdminId())
                .orElseThrow(() -> {
                    var errorMessage =
                            MessageFormat.format(getMessage("library.admin.not_found"), request.getMemberId());
                    return new ResourceNotFoundException(errorMessage);
                });
    }

    private Librarian getLibrarian(FineRequest request) {
        return librarianService.findById(request.getLibrarianId()).
                orElseThrow(() -> {
                    var errorMessage =
                            MessageFormat.format(getMessage("library.librarian.not_found"), request.getMemberId());
                    return new ResourceNotFoundException(errorMessage);
                });
    }

    private static void loggerInfo(Long id) {
        log.warn("Fine with id {} not found", id);
    }

}