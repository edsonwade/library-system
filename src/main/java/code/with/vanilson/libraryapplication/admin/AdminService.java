package code.with.vanilson.libraryapplication.admin;

import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceConflictException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.common.utils.MessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static code.with.vanilson.libraryapplication.admin.AdminMapper.*;
import static code.with.vanilson.libraryapplication.common.utils.MessageProvider.getMessage;

/**
 * BookService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@Repository
@Slf4j
@Service
@SuppressWarnings("all")
public class AdminService implements IAdminService {

    public static final String LIBRARY_ADMIN_NOT_FOUND = "library.admin.not_found";
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;

    }

    /**
     * Retrieves a list of all admins from the database.
     *
     * @return A list of {@link AdminResponse} objects representing all admins in the database.
     * If no admins are found, an empty list is returned.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdminResponse> getAllAdmins() {
        List<Admin> admins = fetchAllAdmins();
        return admins.stream()
                .map(AdminMapper::mapToAdminResponse)
                .toList();
    }

    /**
     * Retrieves an admin by their unique identifier.
     *
     * @param adminId The unique identifier of the admin to retrieve.
     * @return The admin with the given identifier, or a {@link ResourceNotFoundException} if not found.
     * @throws ResourceBadRequestException If the provided adminId is less than or equal to zero.
     */
    @Override
    @Transactional(readOnly = true)
    public AdminResponse getAdminById(Long adminId) {
        if (adminId <= 0) {
            var errorMessage = getMessage("library.admin.bad_request", adminId);
            log.error("The admin id provide is less than or equal to zero {} ", adminId);
            throw new ResourceBadRequestException(errorMessage);
        }
        Optional<Admin> admin = adminRepository.findById(adminId);
        log.info("Retrieving book with ID: {}", adminId);
        return admin
                .map(AdminMapper::mapToAdminResponse)
                .orElseThrow(() -> {
                    log.error("Admin {} not found", adminId);
                    var errorMessage = MessageFormat.format(
                            getMessage(LIBRARY_ADMIN_NOT_FOUND), adminId);
                    return new ResourceNotFoundException(errorMessage);
                });

    }

    @Override
    @Transactional(readOnly = true)
    public AdminResponse getAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email)
                .map(AdminMapper::mapToAdminResponse)
                .orElseThrow(() -> {
                    log.error("The admin email {} provide not found ", email);
                    var errorMessage = MessageFormat.format(
                            getMessage("library.admin_email_not_found"), email);
                    return new ResourceNotFoundException(errorMessage);
                });
    }

    /**
     * Creates a new admin in the database based on the provided adminRequest.
     *
     * @param adminRequest The request object containing the details of the admin to be created.
     * @return The response object containing the details of the newly created admin.
     * @throws ResourceBadRequestException If the provided adminRequest is null.
     */
    @Override
    @Transactional
    public AdminResponse createAdmin(AdminRequest adminRequest) {
        validateAdminRequest(adminRequest);

        if (adminRepository.existsAdminByEmail(adminRequest.getEmail())) {
            String message = formatMessage("library.admin.email_exists", adminRequest.getEmail());
            throw new ResourceConflictException(message);
        }

        var adminEntity = mapToAdminEntity(adminRequest);

        try {
            var savedAdmin = adminRepository.save(adminEntity);

            log.info(formatMessage("library.admin.creation_success", savedAdmin.getId()));
            return mapToAdminResponse(savedAdmin);

        } catch (DataIntegrityViolationException e) {
            // Handle specific unique constraint violations
            String errorMessage = formatMessage("database.exception.unique_constraint_violation");
            throw new ResourceConflictException(errorMessage);
        }
    }

    /**
     * Updates an admin in the database based on the provided adminRequest and adminId.
     *
     * @param adminRequest The request object containing the details of the admin to be updated.
     * @param adminId      The unique identifier of the admin to be updated.
     * @return The response object containing the details of the updated admin.
     * @throws ResourceBadRequestException If the provided adminRequest is null or adminId is invalid.
     * @throws ResourceNotFoundException   If the admin with the given adminId does not exist.
     */
    @Override
    @Transactional
    public AdminResponse updateAdmin(AdminRequest adminRequest, Long adminId) {
        validateAdminRequest(adminRequest);

        checkIfTheValueIsGreatOrLessThanZero(adminId);

        // Fetch the Admin entity directly from the repository, not the response.
        var existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(LIBRARY_ADMIN_NOT_FOUND));

        updateAdminDetails(adminRequest, existingAdmin);
        var updatedAdmin = adminRepository.save(existingAdmin);

        log.info(formatMessage("library.admin.update_success", updatedAdmin.getId()));
        return mapToAdminResponse(updatedAdmin);  // Return the mapped response object
    }

    private void checkIfTheValueIsGreatOrLessThanZero(Long adminId) {
        if (adminId <= 0) {
            log.error("Invalid admin ID: {}", adminId);
            throw new ResourceBadRequestException(formatMessage("library.admin.bad_request", adminId));
        }
    }

    /**
     * Deletes an admin from the database based on the provided adminId.
     *
     * @param adminId The unique identifier of the admin to be deleted.
     * @throws ResourceBadRequestException If the provided adminId is invalid.
     * @throws ResourceNotFoundException   If the admin with the given adminId does not exist.
     */
    @Override
    @Transactional
    public void deleteAdmin(Long adminId) {
        checkIfTheValueIsGreatOrLessThanZero(adminId);

        var admin = adminRepository.findById(adminId)
                .orElseThrow(() -> {
                    logAdminNotFound(adminId);
                    return new ResourceNotFoundException(formatMessage(LIBRARY_ADMIN_NOT_FOUND, adminId));
                });

        adminRepository.delete(admin);
        log.info(formatMessage("library.admin.deletion_success", adminId));
    }

    // Private helper methods for validation, logging, and mapping

    private void validateAdminRequest(AdminRequest adminRequest) {
        if (adminRequest == null) {
            var message= formatMessage("library.admin.cannot_be_null");
            throw new ResourceBadRequestException(message);
        }
    }

    private void updateAdminDetails(AdminRequest adminRequest, Admin existingAdmin) {
        existingAdmin.setName(adminRequest.getName());
        existingAdmin.setEmail(adminRequest.getEmail());
        existingAdmin.setAddress(mapToAddress(adminRequest.getAddress()));
        existingAdmin.setContact(adminRequest.getContact());
        existingAdmin.setAdminCode(adminRequest.getAdminCode());
        existingAdmin.setRole(adminRequest.getRole());
    }

    private void logAdminNotFound(Long adminId) {
        log.error(formatMessage(LIBRARY_ADMIN_NOT_FOUND, adminId));
    }

    public static String formatMessage(String key, Object... params) {
        String pattern = MessageProvider.getMessage(key);
        return MessageFormat.format(pattern, params);
    }

    /**
     * Fetches all admins from the repository.
     *
     * @return A list of {@link Admin} objects representing all admins in the repository.
     * If the repository returns null, a {@link ResourceBadRequestException} is thrown.
     * If the repository returns an empty list, an empty list is returned.
     * @throws ResourceBadRequestException if the admin list is null.
     */
    private List<Admin> fetchAllAdmins() {
        List<Admin> admins = adminRepository.findAll();

        if (Objects.isNull(admins)) {
            log.warn("No admins found in the system: {}", admins);
            String message = formatMessage("library.admin.cannot_be_null", admins);
            throw new ResourceBadRequestException(message);
        }
        if (admins.isEmpty()) {
            log.info("empty list of admins found in the system: {}", admins);
            return Collections.emptyList();
        }
        log.info("Retrieving all admins: {}", admins);
        return admins;
    }
}
