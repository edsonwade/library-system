package code.with.vanilson.libraryapplication.unit.admin;

import code.with.vanilson.libraryapplication.TestDataHelper;
import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminMapper;
import code.with.vanilson.libraryapplication.admin.AdminRequest;
import code.with.vanilson.libraryapplication.admin.AdminResponse;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    private final TestDataHelper testDataHelper = new TestDataHelper();

    @BeforeEach
    void setUp() {
        address = testDataHelper.createAddress();
        addressDTO = testDataHelper.createAddressDTO();
        adminResponse = testDataHelper.createAdminResponse();
        admin = testDataHelper.createAdmin();
        request = testDataHelper.createAdminRequest();

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

}