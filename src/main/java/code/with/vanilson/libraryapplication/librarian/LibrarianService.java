package code.with.vanilson.libraryapplication.librarian;

import code.with.vanilson.libraryapplication.Person.AddressDTO;
import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminRepository;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceConflictException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static code.with.vanilson.libraryapplication.admin.AdminMapper.mapToAddress;
import static code.with.vanilson.libraryapplication.common.utils.MessageProvider.getMessage;
import static code.with.vanilson.libraryapplication.librarian.LibrarianMapper.mapToLibrarianResponse;

/**
 * LibrarianService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-09-02
 */
@Service
@Slf4j
public class LibrarianService implements ILibrarian {

    public static final String ADMIN = "admin";
    private final LibrarianRepository librarianRepository;
    private final AdminRepository adminRepository;

    public LibrarianService(LibrarianRepository librarianRepository, AdminRepository adminRepository) {
        this.librarianRepository = librarianRepository;
        this.adminRepository = adminRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<LibrarianResponse> getAllLibrarians() {
        log.info("Get all librarians");
        return librarianRepository
                .findAll()
                .stream()
                .map(LibrarianMapper::mapToLibrarianResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public LibrarianResponse getLibrarianById(Long librarianId) {
        validateLibrarianIdIsPositive(librarianId);
        return fetchLibrarianDetailsById(librarianId);
    }

    @Transactional(readOnly = true)
    @Override
    public LibrarianResponse getLibrarianByEmail(String email) {
        var librarian = librarianRepository.findLibrariansByEmail(email);
        log.info("Get librarian by email {}", email);
        return librarian.map(LibrarianMapper::mapToLibrarianResponse).
                orElseThrow(() -> new ResourceNotFoundException("library.librarian.email_not_found"));
    }

    /**
     * Creates a new librarian in the system.
     *
     * @param librarianRequest The details of the librarian to be created.
     *                         This object must not be null.
     * @return The response containing the details of the newly created librarian.
     * @throws ResourceBadRequestException If the provided {@code librarianRequest} is null.
     * @throws ResourceConflictException   If the email or contact of the librarian already exists in the system.
     */
    @Override
    public LibrarianResponse createLibrarian(LibrarianRequest librarianRequest) {
        if (null == librarianRequest) {
            throw new ResourceBadRequestException("library.librarian.cannot_be_null");
        }

        var admin = fetchAssociatedAdmin(librarianRequest);
        try {
            validateAndCheckLibrarianUniqueFieldsForUpdate(librarianRequest, 0L);
            var librarian = LibrarianMapper.mapToLibrarianEntity(librarianRequest, admin);
            var requestSaved = librarianRepository.save(librarian);
            return mapToLibrarianResponse(requestSaved);
        } catch (DataIntegrityViolationException e) {
            var errorMessage = MessageFormat.format(getMessage("library.librarian.email_and_contact_already_exists"),
                    e.getMessage());
            loggerMessage(Long.valueOf(errorMessage));
            throw new ResourceConflictException(errorMessage);
        }

    }

    @Override
    public LibrarianResponse updateLibrarian(LibrarianRequest librarianRequest, Long librarianId) {
        if (null == librarianRequest) {
            throw new ResourceBadRequestException("library.librarian.cannot_be_null");
        }

        validateLibrarianIdIsPositive(librarianId);

        // Fetch existing librarian details
        var existingLibrarian = librarianRepository.findById(librarianId)
                .orElseThrow(() -> new ResourceNotFoundException("Librarian not found with ID: " + librarianId));

        // Check if the admin exists
        var admin = fetchAssociatedAdmin(librarianRequest);

        // Validate unique fields (email, contact, and employee code)
        validateAndCheckLibrarianUniqueFieldsForUpdate(librarianRequest, librarianId);

        // Update fields on the existing entity
        existingLibrarian.setName(librarianRequest.getName());
        existingLibrarian.setEmail(librarianRequest.getEmail());
        existingLibrarian.setAddress(mapToAddress(librarianRequest.getAddress()));
        existingLibrarian.setContact(librarianRequest.getContact());
        existingLibrarian.setEmployeeCode(librarianRequest.getEmployeeCode());
        existingLibrarian.setAdmin(admin);  // Set associated admin

        // Save updated entity
        var updatedLibrarian = librarianRepository.save(existingLibrarian);

        return LibrarianMapper.mapToLibrarianResponse(updatedLibrarian);
    }

    /**
     * Updates a librarian's information in the system based on the provided librarian ID and a map of field updates.
     *
     * @param librarianId The ID of the librarian to be updated.
     *                    This parameter must be a positive integer.
     *                    If the ID is not a positive integer, a {@link ResourceBadRequestException} is thrown.
     *                    If no librarian is found with the provided ID, a {@link ResourceNotFoundException} is thrown.
     * @param updates     A map containing the fields to be updated and their corresponding values.
     *                    The keys of the map represent the field names, and the values represent the updated values.
     *                    The supported fields are: "name", "email", "address", "contact", and "employeeCode".
     *                    If an unsupported field is provided, an {@link IllegalArgumentException} is thrown.
     *                    If the admin ID is provided in the updates, the associated admin is updated as well.
     * @return The response containing the details of the updated librarian.
     * @throws ResourceBadRequestException If the provided {@code librarianId} is not a positive integer.
     * @throws ResourceNotFoundException   If no librarian is found with the provided ID.
     * @throws IllegalArgumentException    If an unsupported field is provided in the updates.
     */

    @Transactional
    @Override
    public LibrarianResponse patchLibrarian(Long librarianId, Map<String, Object> updates) {

        // Fetch existing librarian details
        var existingLibrarian = librarianRepository.findById(librarianId)
                .orElseThrow(() -> new ResourceNotFoundException("Librarian not found with ID: " + librarianId));

        // Handle admin update if provided
        if (updates.containsKey(ADMIN)) {
            Long adminId = ((Number) updates.get(ADMIN)).longValue();
            var admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + adminId));
            existingLibrarian.setAdmin(admin);  // Set associated admin
            updates.remove(ADMIN);
        }

        // Apply other field updates
        applyFieldUpdates(existingLibrarian, updates);

        // Save updated librarian
        var updatedLibrarian = librarianRepository.save(existingLibrarian);

        // Return the updated librarian response
        return LibrarianMapper.mapToLibrarianResponse(updatedLibrarian);
    }

    /**
     * Deletes a librarian from the system based on the provided librarian ID.
     *
     * @param librarianId The ID of the librarian to be deleted.
     *                    This parameter must be a positive integer.
     *                    If the ID is not a positive integer, a {@link ResourceBadRequestException} is thrown.
     *                    If no librarian is found with the provided ID, a {@link ResourceNotFoundException} is thrown.
     * @throws ResourceBadRequestException If the provided {@code librarianId} is not a positive integer.
     * @throws ResourceNotFoundException   If no librarian is found with the provided ID.
     */

    @Transactional
    @Override
    public void deleteLibrarianById(Long librarianId) {
        validateLibrarianIdIsPositive(librarianId);
        var foundedId = fetchLibrarianDetailsById(librarianId);
        librarianRepository.deleteById(foundedId.getId());
        var errorMessage = MessageFormat.format(getMessage("library.librarian.deletion_success"), librarianId);
        log.info(errorMessage);
    }

    /**
     * Validates the provided librarian ID to ensure it is a positive integer.
     * If the ID is not a positive integer, a {@link ResourceBadRequestException} is thrown with an appropriate error message.
     *
     * @param librarianId The ID of the librarian to be validated.
     * @throws ResourceBadRequestException If the provided {@code librarianId} is not a positive integer.
     */
    static void validateLibrarianIdIsPositive(Long librarianId) {
        if (librarianId <= 0) {
            var errorMessage = getMessage("library.librarian.bad_request", librarianId);
            log.error("The librarian id provide is less than or equal to zero {} ", librarianId);
            throw new ResourceBadRequestException(errorMessage);
        }
    }

    private static void loggerMessage(Long librarianId) {
        log.error("No librarian found with ID {}", librarianId);
    }

    /**
     * Fetches the details of a librarian from the database based on the provided librarian ID.
     * If the librarian is found, it maps the librarian entity to a response object and returns it.
     * If the librarian is not found, it throws a {@link ResourceNotFoundException} with an appropriate error message.
     *
     * @param librarianId The ID of the librarian to be fetched.
     * @return The response containing the details of the librarian.
     * @throws ResourceNotFoundException If no librarian is found with the provided ID.
     */
    private LibrarianResponse fetchLibrarianDetailsById(Long librarianId) {
        return librarianRepository.findById(librarianId)
                .map(LibrarianMapper::mapToLibrarianResponse)
                .orElseThrow(() -> {
                    var errorMessage = MessageFormat.format(getMessage("library.librarian.not_found"), librarianId);
                    loggerMessage(librarianId);
                    return new ResourceNotFoundException(errorMessage);
                });
    }

    /**
     * Fetches the associated admin from the database based on the provided librarian request.
     * If the admin is found, it returns the admin entity.
     * If the admin is not found, it throws a {@link ResourceNotFoundException} with an appropriate error message.
     *
     * @param librarianRequest The request containing the ID of the admin to be fetched.
     *                         This object must not be null.
     *                         The admin ID must be a positive integer.
     *                         If the ID is not a positive integer, a {@link ResourceNotFoundException} is thrown.
     *                         If no admin is found with the provided ID, a {@link ResourceNotFoundException} is thrown.
     * @return The admin entity associated with the provided librarian request.
     * @throws ResourceNotFoundException If no admin is found with the provided ID.
     */

    private Admin fetchAssociatedAdmin(LibrarianRequest librarianRequest) {
        return adminRepository.findById(librarianRequest.getAdmin())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Admin not found with ID: " + librarianRequest.getAdmin()));
    }

    /**
     * Validates and checks if a librarian with the same unique fields (email, contact, and employee code) already exists,
     * excluding the current librarian's own fields.
     *
     * @param librarianRequest The details of the librarian to be validated.
     *                         This object must not be null.
     * @param librarianId      The ID of the librarian to be excluded from the uniqueness checks.
     *                         This parameter must be a positive integer.
     *                         If the ID is not a positive integer, a {@link ResourceBadRequestException} is thrown.
     *                         If no librarian is found with the provided ID, a {@link ResourceNotFoundException} is thrown.
     * @throws ResourceConflictException If a librarian with the same unique fields already exists in the system,
     *                                   excluding the current librarian's own fields.
     */

    private void validateAndCheckLibrarianUniqueFieldsForUpdate(LibrarianRequest librarianRequest, Long librarianId) {
        // Exclude the current librarian's own fields from the uniqueness checks
        if (librarianRepository.existsLibrarianByEmailAndIdNot(librarianRequest.getEmail(), librarianId)) {
            throw new ResourceConflictException("library.librarian.email_exists");
        }
        if (librarianRepository.existsLibrarianByContactAndIdNot(librarianRequest.getContact(), librarianId)) {
            throw new ResourceConflictException("library.librarian.contact_exists");
        }
        if (librarianRepository.existsLibrarianByEmployeeCodeAndIdNot(librarianRequest.getEmployeeCode(),
                librarianId)) {
            throw new ResourceConflictException("librarian.employee_with_code_Already_exists");
        }
    }

    /**
     * Applies field updates to an existing librarian entity based on the provided map of updates.
     *
     * @param existingLibrarian The librarian entity to which the updates will be applied.
     *                          This parameter must not be null.
     * @param updates           A map containing the fields to be updated and their corresponding values.
     *                          The keys of the map represent the field names, and the values represent the updated values.
     *                          The supported fields are: "name", "email", "address", "contact", and "employeeCode".
     *                          If an unsupported field is provided, an {@link IllegalArgumentException} is thrown.
     *                          This parameter must not be null.
     * @throws IllegalArgumentException If an unsupported field is provided in the updates.
     */
    private void applyFieldUpdates(Librarian existingLibrarian, Map<String, Object> updates) {

        updates.forEach((field, value) -> {
            switch (field) {
                case "name":
                    existingLibrarian.setName((String) value);
                    break;
                case "email":
                    existingLibrarian.setEmail((String) value);
                    break;
                case "address":
                    AddressDTO addressDTO = (AddressDTO) value;
                    existingLibrarian.setAddress(LibrarianMapper.mapToAddress(addressDTO));
                    break;
                case "contact":
                    existingLibrarian.setContact((String) value);
                    break;
                case "employeeCode":
                    existingLibrarian.setEmployeeCode((String) value);
                    break;
                // Handle more fields if necessary
                default:
                    throw new IllegalArgumentException("Field " + field + " is not valid for patching.");
            }
        });
    }

}