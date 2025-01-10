package code.with.vanilson.libraryapplication.unit.admin;

import code.with.vanilson.libraryapplication.admin.*;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static code.with.vanilson.libraryapplication.admin.AdminMapper.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unused")
class AdminMapperTest {

    /**
     * Represents a request object for creating an Admin entity.
     */
    public AdminRequest request;

    /**
     * Represents a response object for an Admin entity.
     */
    public AdminResponse adminResponse;

    /**
     * Represents a mapper class for mapping between Admin entities and DTOs.
     */
    public AdminMapper adminMapper;

    /**
     * Represents an Admin entity with administrative privileges.
     */
    public Admin admin;

    /**
     * Represents a Data Transfer Object (DTO) for an address.
     */
    public AddressDTO addressDTO;

    /**
     * Represents an address object with street, city, state, country, and postal code information.
     */
    public Address address;

    @BeforeEach
    void setUp() {
        address = auxiliarMethodToAddress();
        addressDTO = auxiliarMethodToAddressDTO();
        adminResponse = auxiliarMethodToAdminResponse();
        admin = auxiliarMethodToAdmin();
        request = auxiliarMethodToAdminRequest();

    }

    @Test
    @DisplayName("Mapping AdminRequest to AdminEntity should set correct values")
    void testMapToAdminEntityTSuccess() {
        var adminExpect = mapToAdminEntity(request);

        assertNull(adminExpect.getId());
        assertEquals("John Doe", adminExpect.getName(), "John Doe");
        assertEquals("johndoe@example.com", adminExpect.getEmail(), "John Doe's email");
        assertNotEquals("johndoe@outlook.com", adminExpect.getEmail(), "John Doe's email");
        assertNotEquals("johndoe@outlook.com", adminExpect.getEmail(), "John Doe's email");
    }

    @Test
    @DisplayName("Mapping AdminRequest to Admin should throw exception when null is passed")
    void testMapToAdminEntityThrowsAnException() {
        assertThrows(ResourceBadRequestException.class, () -> mapToAdminEntity(null));
    }

    @Test
    @DisplayName("Mapping AdminRequest to AdminEntity should set correct values")
    void testMapToAdminResponseSuccess() {
        var adminResponseExpect = mapToAdminResponse(admin);

        assertNotNull(adminResponseExpect.getId());
        assertEquals("John Doe", adminResponseExpect.getName(), "John Doe");
        assertEquals("johndoe@example.com", adminResponseExpect.getEmail(), "johndoe@example.com");
        assertNotEquals("johndoe@outlook.com", adminResponseExpect.getEmail(), "johndoe@outlook.com");
    }

    @Test
    @DisplayName("Mapping AdminRequest to AdminEntity should set correct values")
    void testMapToAdminSuccess() {
        var adminExpect = mapToAdmin(adminResponse);

        assertNull(adminExpect.getId());
        assertEquals("John Doe", adminExpect.getName(), "John Doe");
        assertEquals("johndoe@example.com", adminExpect.getEmail(), "johndoe@example.com");
        assertNotEquals("johndoe@outlook.com", adminExpect.getEmail(), "johndoe@outlook.com");
    }

    @Test
    @DisplayName("Mapping Admin to AdminResponse should throw exception when null is passed")
    void testMapToAdminResponseThrowsAnException() {
        assertThrows(ResourceBadRequestException.class, () -> mapToAdminResponse(null),
                "Cannot create an admin because the provided address object is null. Please provide valid admin details.");
    }

    @Test
    @DisplayName("Mapping Address to AddressDto should throw exception when null is passed")
    void testMapToAddressDtoThrowsAnException() {
        assertThrows(ResourceBadRequestException.class, () -> mapToAddressDTO(null),
                "Cannot create an addressDTO because the provided address object is null. Please provide valid admin " +
                        "details.");
    }

    @Test
    @DisplayName("Mapping AddressDTO to Address should throw exception when null is passed")
    void testMapToAddressThrowsAnException() {
        assertThrows(ResourceBadRequestException.class, () -> mapToAddress(null),
                "Cannot create an address because the provided address object is null. Please provide valid admin " +
                        "details.");
    }

    @Test
    @DisplayName("Mapping AddressDTO to Address should throw exception when null is passed")
    void testMapToAdminThrowsAnException() {
        assertThrows(ResourceBadRequestException.class, () -> mapToAdmin(null),
                "Cannot create an admin because the provided address object is null. Please provide valid admin " +
                        "details.");
    }

    /**
     * Creates and returns an Address object with predefined values.
     *
     * @return an Address object with street, city, state, country, and postal code information
     **/
    protected Address auxiliarMethodToAddress() {
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
    protected AddressDTO auxiliarMethodToAddressDTO() {
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
    protected AdminResponse auxiliarMethodToAdminResponse() {
        var admins = new AdminResponse();
        admins.setId(1L);
        admins.setName("John Doe");
        admins.setEmail("johndoe@example.com");
        admins.setAddress(auxiliarMethodToAddressDTO());
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
    protected AdminRequest auxiliarMethodToAdminRequest() {
        var adminReq = new AdminRequest();
        adminReq.setName("John Doe");
        adminReq.setEmail("johndoe@example.com");
        adminReq.setAddress(auxiliarMethodToAddressDTO());
        adminReq.setContact("+531 990 900 000");
        adminReq.setAdminCode("ADM-001");
        adminReq.setRole(Role.SYSTEM_ADMIN);
        return adminReq;

    }

    /**
     * Creates and returns an Admin object with predefined values.
     *
     * @return an Admin object with predefined values for id, name, email, address, contact, admin code, and role
     */
    protected Admin auxiliarMethodToAdmin() {
        var admins = new Admin();
        admins.setId(1L);
        admins.setName("John Doe");
        admins.setEmail("johndoe@example.com");
        admins.setAddress(auxiliarMethodToAddress());
        admins.setContact("+531 990 900 000");
        admins.setAdminCode("ADM-001");
        admins.setRole(Role.SYSTEM_ADMIN);
        return admins;

    }

    /**
     * Creates and returns an Admin List object with predefined values.
     *
     * @return an Admin object with predefined values for id, name, email, address, contact, admin code, and role
     */
    protected List<Admin> auxiliarMethodToAllAdmin() {
        return List.of(auxiliarMethodToAdmin());

    }

    /**
     * Creates and returns an Admin List object with predefined values.
     *
     * @return an Admin object with predefined values for id, name, email, address, contact, admin code, and role
     */
    protected List<AdminResponse> auxiliarMethodToAllAdminResponse() {
        return List.of(auxiliarMethodToAdminResponse());

    }

}