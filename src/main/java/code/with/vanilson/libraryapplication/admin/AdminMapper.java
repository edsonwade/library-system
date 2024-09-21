package code.with.vanilson.libraryapplication.admin;

import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import org.springframework.stereotype.Component;

/**
 * AdminMapper
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-30
 */
@Component
public class AdminMapper {

    public static final String LIBRARY_ADMIN_CANNOT_BE_NULL = "library.admin.cannot_be_null";

    public static Admin mapToAdminEntity(AdminRequest request) {
        // Validate inputs and throw exceptions if necessary
        if (null == request) {
            throw new ResourceBadRequestException(LIBRARY_ADMIN_CANNOT_BE_NULL);
        }
        return new Admin(
                request.getName(),
                request.getEmail(),
                mapToAddress(request.getAddress()), // Ensure this method exists and is implemented correctly
                request.getContact(),
                request.getAdminCode(),
                request.getRole()
        );
    }

    /**
     * Maps an {@link Admin} object to an {@link AdminResponse} object.
     *
     * <p>This method is used to convert an {@link Admin} object into an {@link AdminResponse} object.
     * It checks if the input {@link Admin} is null and throws a {@link ResourceBadRequestException}
     * if it is. Otherwise, it creates a new {@link AdminResponse} object and populates it with the
     * corresponding data from the input {@link Admin} object.
     *
     * @param admin The {@link Admin} object to be mapped.
     * @return The mapped {@link AdminResponse} object.
     * @throws ResourceBadRequestException If the input {@link Admin} is null.
     */
    public static AdminResponse mapToAdminResponse(Admin admin) {
        if (null == admin) {
            throw new ResourceBadRequestException(LIBRARY_ADMIN_CANNOT_BE_NULL);
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

    /**
     * Maps a {@link Address} object to a {@link AddressDTO} object.
     *
     * <p>This method is used to convert a {@link Address} object into a {@link AddressDTO} object.
     * It checks if the input {@link Address} is null and throws a {@link ResourceBadRequestException}
     * if it is. Otherwise, it creates a new {@link AddressDTO} object and populates it with the
     * corresponding data from the input {@link Address} object.
     *
     * @param address The {@link Address} object to be mapped.
     * @return The mapped {@link AddressDTO} object.
     * @throws ResourceBadRequestException If the input {@link Address} is null.
     */

    public static AddressDTO mapToAddressDTO(Address address) {
        if (address == null) {
            throw new ResourceBadRequestException("Address cannot be null"); // Or handle this case as needed
        }
        return AddressDTO.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }

    /**
     * Maps a {@link AddressDTO} object to a {@link Address} object.
     *
     * @param address The {@link AddressDTO} object to be mapped.
     * @return The mapped {@link Address} object.
     * @throws ResourceBadRequestException If the input {@link AddressDTO} is null.
     */
    public static Address mapToAddress(AddressDTO address) {
        if (address == null) {
            throw new ResourceBadRequestException("library.address.cannot_be_null"); // Or handle this case as needed
        }
        return Address.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }

    public static Admin mapToAdmin(AdminResponse adminResponse) {
        if (null == adminResponse) {
            throw new ResourceBadRequestException(LIBRARY_ADMIN_CANNOT_BE_NULL);
        }

        return new Admin(
                adminResponse.getName(),
                adminResponse.getEmail(),
                mapToAddress(adminResponse.getAddress()),
                adminResponse.getContact(),
                adminResponse.getAdminCode(),
                adminResponse.getRole()
        );
    }

    private AdminMapper() {
        // Private constructor to prevent instantiation
    }
}