package code.with.vanilson.libraryapplication.librarian;

import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminResponse;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/**
 * LibrarianMapper
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
@SuppressWarnings("unused")
@Data
@Builder
@Slf4j
public class LibrarianMapper {

    public static final String LIBRARY_LIBRARIAN_CANNOT_BE_NULL = "library.librarian.cannot_be_null";

    private LibrarianMapper() {
        // Private constructor to prevent instantiation
    }

    // Maps Librarian entity to LibrarianResponse DTO
    public static LibrarianResponse mapToLibrarianResponse(Librarian librarian) {
        if (null == librarian) {
            log.error("Librarian is null" + null);
            throw new ResourceBadRequestException(LIBRARY_LIBRARIAN_CANNOT_BE_NULL);
        }

        if (null == librarian.getAdmin()) {
            log.error(MessageFormat.format("admin is null {0}", (Object) null));
            throw new ResourceBadRequestException("library.admin.association_must_exists");
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
            throw new ResourceBadRequestException(LIBRARY_LIBRARIAN_CANNOT_BE_NULL);
        }

        if (null == admin) {
            throw new ResourceBadRequestException("library.admin.cannot_be_null");
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

    // Example mapping method for AddressDTO to Address
    protected static AddressDTO mapToAddressDTO(Address address) {
        if (null == address) {
            return null;
        }
        return new AddressDTO(
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getPostalCode()
        );
    }

    // Example mapping method for AddressDTO to Address
    static Address mapToAddress(AddressDTO addressDTO) {
        if (null == addressDTO) {
            return null;
        }
        return new Address(
                addressDTO.getStreet(),
                addressDTO.getCity(),
                addressDTO.getState(),
                addressDTO.getCountry(),
                addressDTO.getPostalCode()
        );
    }

    private static AdminResponse mapToAdminResponse(Admin admin) {
        if (null == admin) {
            return null;
        }

        return AdminResponse.builder()
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .address(mapToAddressDTO(admin.getAddress()))
                .contact(admin.getContact())
                .adminCode(admin.getAdminCode())
                .role(admin.getRole())
                .build();
    }

    // Maps LibrarianResponse DTO back to LibrarianRequest DTO (used for PATCH operations)
    public static LibrarianRequest mapToLibrarianRequest(LibrarianResponse response) {
        if (response == null) {
            throw new ResourceBadRequestException(LIBRARY_LIBRARIAN_CANNOT_BE_NULL);
        }

        return LibrarianRequest.builder()
                .name(response.getName())
                .email(response.getEmail())
                .address(mapToAddressDTO(mapToAddress(response.getAddress()))) // Convert Address to AddressDTO
                .contact(response.getContact())
                .employeeCode(response.getEmployeeCode())
                .admin(response.getAdmin().getId()) // Use the admin ID for the request
                .build();
    }

}

