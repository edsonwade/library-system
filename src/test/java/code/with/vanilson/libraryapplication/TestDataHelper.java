package code.with.vanilson.libraryapplication;

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminRequest;
import code.with.vanilson.libraryapplication.admin.AdminResponse;
import code.with.vanilson.libraryapplication.admin.Role;
import code.with.vanilson.libraryapplication.librarian.LibrarianRequest;
import code.with.vanilson.libraryapplication.librarian.LibrarianResponse;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.AddressDTO;

import java.util.List;

/**
 * TestDataHelper
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-01-15
 */
public class TestDataHelper {
    /**
     * Creates and returns an Admin object with predefined values.
     *
     * @return an Admin object with predefined values for id, name, email, address, contact, admin code, and role
     */
    public Admin createAdmin() {
        var admins = new Admin();
        admins.setId(1L);
        admins.setName("John Doe");
        admins.setEmail("johndoe@example.com");
        admins.setAddress(createAddress());
        admins.setContact("+531 990 900 000");
        admins.setAdminCode("ADM-001");
        admins.setRole(Role.SYSTEM_ADMIN);
        return admins;

    }

    /**
     * Creates and returns an Address object with predefined values.
     *
     * @return an Address object with street, city, state, country, and postal code information
     **/
    public Address createAddress() {
        var address1 = new Address();
        address1.setStreet("123 Main Street");
        address1.setCity("New York");
        address1.setCountry("United States");
        address1.setState("NY");
        address1.setPostalCode("10001");
        return address1;
    }

    /**
     * Creates and returns an AddressDTO object with predefined values.
     *
     * @return an AddressDTO object with street, city, state, country, and postal code information
     */
    public AddressDTO createAddressDTO() {
        var addressDTO1 = new AddressDTO();
        addressDTO1.setStreet("123 Main Street");
        addressDTO1.setCity("New York");
        addressDTO1.setCountry("United States");
        addressDTO1.setState("NY");
        addressDTO1.setPostalCode("10001");
        return addressDTO1;
    }

    /**
     * Creates and returns an AdminResponse object with predefined values.
     *
     * @return an AdminResponse object with predefined values for id, name, email, address, contact, admin code, and role
     */
    public AdminResponse createAdminResponse() {
        var admins = new AdminResponse();
        admins.setId(1L);
        admins.setName("John Doe");
        admins.setEmail("johndoe@example.com");
        admins.setAddress(createAddressDTO());
        admins.setContact("+531 990 900 000");
        admins.setAdminCode("ADM-001");
        admins.setRole(Role.SYSTEM_ADMIN);
        return admins;

    }

    /**
     * Creates and returns an AdminRequest object with predefined values.
     *
     * @return an AdminRequest object with predefined values for name, email, address, contact, admin code, and role
     */
    public AdminRequest createAdminRequest() {
        var adminReq = new AdminRequest();
        adminReq.setName("John Doe");
        adminReq.setEmail("johndoe@example.com");
        adminReq.setAddress(createAddressDTO());
        adminReq.setContact("+531 990 900 000");
        adminReq.setAdminCode("ADM-001");
        adminReq.setRole(Role.SYSTEM_ADMIN);
        return adminReq;

    }

    /**
     * Creates and returns an Admin List object with predefined values.
     *
     * @return an Admin object with predefined values for id, name, email, address, contact, admin code, and role
     */
    public List<Admin> auxiliarMethodToAllAdmin() {
        return List.of(createAdmin());

    }

    /**
     * Creates and returns an Admin List object with predefined values.
     *
     * @return an Admin object with predefined values for id, name, email, address, contact, admin code, and role
     */
    public List<AdminResponse> auxiliarMethodToAllAdminResponse() {
        return List.of(createAdminResponse());

    }

    /**
     * Creates a {@link LibrarianResponse} object populated with mock data.
     * This method is used to generate a sample response object for testing purposes,
     * typically when simulating the return value of a service or controller method.
     *
     * @return a {@link LibrarianResponse} object populated with mock data
     */
    public LibrarianResponse createLibrarianResponse() {
        return LibrarianResponse.builder()
                .id(1L)
                .name("test 1")
                .email("test@test.com")
                .address(createAddressDTO())
                .contact("+351 123-235-345")
                .employeeCode("EMPLO01")
                .admin(createAdminResponse())
                .build();
    }

    /**
     * Creates a {@link LibrarianRequest} object populated with mock data.
     * This method is used to generate a sample request object for testing purposes,
     * typically when simulating the input of a librarian creation or update process.
     *
     * @return a {@link LibrarianRequest} object populated with mock data
     */
    public LibrarianRequest createLibrarianRequest() {
        return LibrarianRequest.builder()
                .name("test 1")
                .email("test@test.com")
                .contact("+351 123-235-345")
                .address(createAddressDTO())
                .employeeCode("EMPLO01")
                .admin(createAdminResponse().getId())
                .build();
    }
}