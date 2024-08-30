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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static code.with.vanilson.libraryapplication.admin.AdminMapper.*;
import static code.with.vanilson.libraryapplication.admin.AdminMapper.mapToAdminResponse;
import static code.with.vanilson.libraryapplication.common.utils.MessageProvider.*;

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
public class AdminService implements IAdminService {

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
        log.info("Retrieving all books");
        return adminRepository.findAll()
                .stream()
                .map(AdminMapper::mapToAdminResponse)
                .collect(Collectors.toList());

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
                    loggerError(adminId);
                    var errorMessage = MessageFormat.format(
                            getMessage("library.admin.not_found"), adminId);
                    return new ResourceNotFoundException(errorMessage);
                });

    }

    @Override
    @Transactional(readOnly = true)
    public AdminResponse getAdminByEmail(String email) {
        return null;
    }

    /**
     * Creates a new admin in the database based on the provided adminRequest.
     *
     * @param adminRequest The request object containing the details of the admin to be created.
     *                     This object should not be null.
     * @return The response object containing the details of the newly created admin.
     * @throws ResourceBadRequestException If the provided adminRequest is null.
     */
    @Override
    @Transactional
    public AdminResponse createAdmin(AdminRequest adminRequest) {
        var admin = getAdmin(null == adminRequest, mapToAdminEntity(adminRequest));

        // Check if an Admin with the same unique fields already exists
        if (adminRepository.existsAdminByEmail(adminRequest.getEmail())) {
            var message = MessageFormat.format(MessageProvider.getMessage("library.admin.email_exists"),adminRequest.getEmail());
            throw new ResourceConflictException(message);
        }

        try {
            var adminResponseSaved = adminRepository.save(admin);
            var message = MessageFormat.format(MessageProvider.getMessage("library.admin.creation_success"),
                    adminResponseSaved.getId());
            log.info(message);
            return mapToAdminResponse(adminResponseSaved);
        } catch (DataIntegrityViolationException e) {
            // Handle specific unique constraint violation
            String errorMessage = getFormattedMessage("database.exception.unique_constraint_violation");
            throw new ResourceConflictException(errorMessage);
        }

    }

    /**
     * Updates an admin in the database based on the provided adminRequest and adminId.
     *
     * @param adminRequest The request object containing the details of the admin to be updated.
     *                     This object should not be null.
     * @param adminId      The unique identifier of the admin to be updated.
     *                     This value should be greater than zero.
     * @return The response object containing the details of the updated admin.
     * @throws ResourceBadRequestException If the provided adminRequest is null or the adminId is less than or equal to zero.
     * @throws ResourceNotFoundException   If the admin with the given adminId does not exist in the database.
     */

    @Override
    @Transactional
    public AdminResponse updateAdmin(AdminRequest adminRequest, Long adminId) {
        var existingAdmin = getAdmin(adminRequest == null, adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("library.admin.not_found")));

        methodAuxiliaryToCreateAdmin(adminRequest, existingAdmin);

        // Save the updated Admin entity
        var updatedAdmin = adminRepository.save(existingAdmin);

        // Log the update operation
        var message = MessageFormat.format(MessageProvider.getMessage("library.admin.update_success"),
                updatedAdmin.getId());
        log.info(message);

        // Return the updated AdminResponse
        return mapToAdminResponse(updatedAdmin);
    }

    /**
     * Deletes an admin from the database based on the provided adminId.
     *
     * @param adminId The unique identifier of the admin to be deleted.
     * @throws ResourceBadRequestException If the provided adminId is less than or equal to zero.
     * @throws ResourceNotFoundException   If the admin with the given adminId does not exist in the database.
     **/
    @Transactional
    @Override
    public void deleteAdmin(Long adminId) {
        if (adminId <= 0) {
            var errorMessage = getMessage("library.admin.bad_request", adminId);
            log.error("The admin id provided is less than or equal to zero {} ", adminId);
            throw new ResourceBadRequestException(errorMessage);
        }
        Optional<Admin> admin = adminRepository.findById(adminId);
        admin.ifPresent(adminRepository::delete);

        var message = MessageFormat.format(MessageProvider.getMessage("library.admin.deletion_success"), adminId);
        log.info(message);

        if (admin.isEmpty()) {
            loggerError(adminId);
            var errorMessage = MessageFormat.format(
                    getMessage("library.admin.not_found"), adminId);
            throw new ResourceNotFoundException(errorMessage);
        }

    }

    /**
     * Logs an error message for a non-existent admin.
     * <p>
     * This method is used to log an error message when an attempt is made to retrieve, update, or delete an admin
     * that does not exist in the database. The error message is formatted using the provided adminId and the
     * corresponding message from the MessageProvider.
     *
     * @param adminId The unique identifier of the admin that was not found.
     */
    private static void loggerError(Long adminId) {
        var message = MessageFormat.format(MessageProvider.getMessage("library.admin.not_found"), adminId);
        log.error(message);
    }

    private Admin getAdmin(boolean adminRequest, Admin adminRepository) {
        if (adminRequest) {
            throw new ResourceBadRequestException("library.admin.cannot_be_null");
        }

        // Retrieve the existing Admin from the database
        return adminRepository;
    }

    private static void methodAuxiliaryToCreateAdmin(AdminRequest adminRequest, Admin existingAdmin) {
        // Update the existing Admin with new values
        existingAdmin.setName(adminRequest.getName());
        existingAdmin.setEmail(adminRequest.getEmail());
        existingAdmin.setAddress(mapToAddress(adminRequest.getAddress()));
        existingAdmin.setContact(adminRequest.getContact());
        existingAdmin.setAdminCode(adminRequest.getAdminCode());
        existingAdmin.setRole(adminRequest.getRole());
    }

    /**
     * Retrieves and formats a message based on the provided key and parameters.
     *
     * @param key    The key for the message in the properties file.
     * @param params Parameters to be inserted into the message format.
     * @return The formatted message.
     */
    private String getFormattedMessage(String key, Object... params) {
        String pattern = MessageProvider.getMessage(key);
        return MessageFormat.format(pattern, params);

    }
}
