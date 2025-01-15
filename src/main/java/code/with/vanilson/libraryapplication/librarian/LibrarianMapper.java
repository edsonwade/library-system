package code.with.vanilson.libraryapplication.librarian;

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

import static code.with.vanilson.libraryapplication.admin.AdminMapper.*;
import static code.with.vanilson.libraryapplication.admin.AdminService.formatMessage;

/**
 * LibrarianMapper
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
@SuppressWarnings("all")
@Data
@Builder
@Slf4j
public class LibrarianMapper {

    public static final String LIBRARY_LIBRARIAN_CANNOT_BE_NULL = "library.librarian.cannot_be_null";

    // Maps Librarian entity to LibrarianResponse DTO
    public static LibrarianResponse mapToLibrarianResponse(Librarian librarian) {
        if (null == librarian) {
            log.error("Librarian is null" + null);
            String message = formatMessage(LIBRARY_LIBRARIAN_CANNOT_BE_NULL, (Object) null);
            throw new ResourceBadRequestException(message);
        }

        if (null == librarian.getAdmin()) {
            log.error(MessageFormat.format("admin is null {0} ", (Object) null));
            String message = formatMessage("library.admin.association_must_exists", (Object) null);
            throw new ResourceBadRequestException(message);
        }

        // Map the Librarian entity to the response DTO
        return LibrarianResponse.builder()
                .id(librarian.getId())
                .name(librarian.getName())
                .email(librarian.getEmail())
                .address(mapToAddressDTO(librarian.getAddress()))
                .contact(librarian.getContact())
                .employeeCode(librarian.getEmployeeCode())
                .admin(mapToAdminResponse(librarian.getAdmin())) // Ensure admin is never null
                .build();
    }

    // Maps LibrarianRequest DTO to Librarian entity
    public static Librarian mapToLibrarianEntity(LibrarianRequest request, Admin admin) {
        if (null == request) {
            String message = formatMessage("library.librarian.request_null", (Object) null);
            throw new ResourceBadRequestException(message);
        }

        if (null == admin) {
            String message = formatMessage("library.admin.cannot_be_null", (Object) null);
            throw new ResourceBadRequestException(message);
        }

        // Map the request DTO to the Librarian entity
        return new Librarian(
                request.getName(),
                request.getEmail(),
                mapToAddress(request.getAddress()), // Ensure this method maps the AddressDTO to Address entity
                request.getContact(),
                request.getEmployeeCode(),
                admin // Set the admin from the service layer
        );
    }

    public static Librarian mapToLibrarian(LibrarianResponse librarianResponse) {
        if (null == librarianResponse) {
            String message = formatMessage("library.librarian.response_null", (Object) null);
            throw new ResourceBadRequestException(message);
        }
        return new Librarian(
                librarianResponse.getName(),
                librarianResponse.getEmail(),
                mapToAddress(librarianResponse.getAddress()),
                librarianResponse.getContact(),
                librarianResponse.getEmployeeCode(),
                mapToAdmin(librarianResponse.getAdmin()));
    }

}

