package code.with.vanilson.libraryapplication.unit.librarian;

import code.with.vanilson.libraryapplication.TestDataHelper;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianMapper;
import code.with.vanilson.libraryapplication.librarian.LibrarianRequest;
import code.with.vanilson.libraryapplication.librarian.LibrarianResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class LibrarianMapperTest {

    /**
     * The response object representing the librarian data in response format.
     * This is used for testing the mapping between the librarian entity and its response DTO.
     * It is instantiated as {@link LibrarianResponse response}.
     */
    private LibrarianResponse response;

    /**
     * The request object representing the librarian data in request format.
     * This is used for testing the mapping between the librarian request DTO and the entity.
     * It is instantiated as {@link LibrarianRequest request}.
     */
    private LibrarianRequest request;

    /**
     * The librarian entity that is used in tests to validate the mapping and business logic.
     * This object contains the actual librarian data which is used in various test cases.
     * It is instantiated as {@link Librarian librarian}.
     */
    private Librarian librarian;

    /**
     * A helper class for creating test data such as librarian entities, requests, responses, etc.
     * This class is used to avoid redundancy and make it easier to generate test data for multiple tests.
     * It is instantiated as {@link TestDataHelper testDataHelper}.
     */
    private final TestDataHelper testDataHelper = new TestDataHelper();


    @BeforeEach
    void setUp() {
        librarian =
                new Librarian("test 1", "test@test.com", testDataHelper.createAddress(), "+351 123-235-345", "EMPLO01",
                        testDataHelper.createAdmin());
        response = testDataHelper.createLibrarianResponse();
        request = testDataHelper.createLibrarianRequest();
    }

    /**
     * Tests the mapping of a Librarian entity to a LibrarianResponse DTO.
     * It verifies that the mapping correctly handles the Librarian fields and associated Admin object.
     * It also checks that exceptions are thrown when the Librarian or Admin is null.
     */
    @DisplayName("Should map Librarian entity to LibrarianResponse DTO")
    @Test
    void testMappingLibrarianToLibrarianResponse() {
        var result = LibrarianMapper.mapToLibrarianResponse(librarian);
        assertEquals(response.getName(), result.getName());
        assertEquals(response.getEmail(), result.getEmail());
        assertNotNull(result);

    }

    /**
     * Tests that a ResourceBadRequestException is thrown when the input Librarian entity is null.
     * This validates that the method handles null input appropriately.
     */
    @DisplayName("Should throw exception when Librarian is null while mapping to LibrarianResponse")
    @Test
    void testMappingLibrarianToLibrarianResponseShouldThrowExceptionForNullLibrarian() {
        // Test implementation here
        var expectedMessage = assertThrows(ResourceBadRequestException.class,
                () -> LibrarianMapper.mapToLibrarianResponse(null));
        assertEquals(
                "Cannot create a librarian because the provided book object is null. Please provide librarian book details.",
                expectedMessage.getMessage());
    }

    /**
     * Tests that a ResourceBadRequestException is thrown when the Librarian entity has no associated Admin.
     * This ensures the method validates the existence of an associated Admin before proceeding with the mapping.
     */
    @DisplayName("Should throw exception when Librarian's admin is null while mapping to LibrarianResponse")
    @Test
    void testMappingLibrarianToLibrarianResponseShouldThrowExceptionForNullAdmin() {
        var lb = new Librarian("test 1", "test@test.com", testDataHelper.createAddress(), "+351 123-235-345",
                "EMPLO01",
                null);

        var expectedMessage = assertThrows(ResourceBadRequestException.class,
                () -> LibrarianMapper.mapToLibrarianResponse(lb));
        assertEquals(
                "Librarian must have an associated admin.",
                expectedMessage.getMessage());

    }

    @DisplayName("Should throw exception when LibrarianRequest DTO is null while mapping to Librarian entity")
    @Test
    void testMappingLibrarianRequestToLibrarianEntityShouldThrowExceptionForNullRequest() {

        var expectedMessage = assertThrows(ResourceBadRequestException.class,
                () -> LibrarianMapper.mapToLibrarianEntity(null, librarian.getAdmin()));
        assertEquals(
                "Cannot create a librarian because the provided librarian request is null. Please provide valid librarian request details.",
                expectedMessage.getMessage());
        assertNotNull(expectedMessage, "Exception should not be null.");
        assertTrue(expectedMessage.getMessage().contains("librarian request"),
                "Message should contain 'librarian request'");

    }

    /**
     * Tests that a ResourceBadRequestException is thrown when the Admin object is null while mapping from LibrarianRequest DTO to Librarian entity.
     * This ensures that the Admin is required for successful mapping.
     */
    @DisplayName("Should throw exception when Admin is null while mapping to Librarian entity")
    @Test
    void testMappingLibrarianRequestToLibrarianEntityShouldThrowExceptionForNullAdmin
    () {
        var expectedMessage = assertThrows(ResourceBadRequestException.class,
                () -> LibrarianMapper.mapToLibrarianEntity(request, null));
        assertEquals(
                "Cannot create an admin because the provided address object is null. Please provide valid admin details.",
                expectedMessage.getMessage());
        assertNotNull(expectedMessage, "Expected message should not be null.");
        assertTrue(expectedMessage.getMessage().contains("Cannot create an admin"),
                "Message should contain 'Cannot create an admin'");

    }

    @DisplayName("Should map request librarian to librarian")
    @Test
    void shouldMappingLibrarianRequestToLibrarianEntity() {
        // Arrange: Mapping the request to the expected result
        var expectedResult = LibrarianMapper.mapToLibrarianEntity(request, librarian.getAdmin());

        // Assert all values in one block
        assertAll("Librarian Entity Mapping",
                () -> assertEquals(expectedResult.getName(), librarian.getName(),
                        "The librarian name was not mapped correctly from the request."),
                () -> assertEquals(expectedResult.getEmail(), librarian.getEmail(),
                        "The librarian email was not mapped correctly from the request."),
                () -> assertNotNull(expectedResult, "The librarian entity should not be null after mapping."),
                () -> assertNotNull(expectedResult.getAddress(),
                        "The librarian address should not be null after mapping."),
                () -> assertEquals(expectedResult.getAddress().getStreet(), librarian.getAddress().getStreet(),
                        "The librarian address street was not mapped correctly."),
                () -> assertEquals(expectedResult.getContact(), librarian.getContact(),
                        "The librarian contact was not mapped correctly from the request."),
                () -> assertEquals(expectedResult.getEmployeeCode(), librarian.getEmployeeCode(),
                        "The librarian employee code was not mapped correctly from the request."),
                () -> assertNotNull(expectedResult.getAdmin(), "The librarian entity should have an associated admin."),
                () -> assertEquals(expectedResult.getAdmin().getId(), librarian.getAdmin().getId(),
                        "The librarian's admin ID was not mapped correctly.")
        );
    }

    @DisplayName("Should throw exception when LibrarianResponse DTO is null while mapping to Librarian entity")
    @Test
    void testMappingLibrarianResponseToLibrarianShouldThrowExceptionForNullResponse() {

        var expectedMessage = assertThrows(ResourceBadRequestException.class,
                () -> LibrarianMapper.mapToLibrarian(null));
        assertEquals(
                "Cannot create a librarian because the provided librarian response is null. Please provide valid librarian request details.",
                expectedMessage.getMessage());
        assertNotNull(expectedMessage, "Exception should not be null.");
        assertTrue(expectedMessage.getMessage().contains("librarian response"),
                "Message should contain 'librarian response'");

    }

    @DisplayName("Should map response librarian to librarian")
    @Test
    void shouldMappingLibrarianResponseToLibrarian() {
        // Arrange: Mapping the request to the expected result
        var expectedResult = LibrarianMapper.mapToLibrarian(response);
        // Assert all values in one block
        assertAll("Librarian Entity Mapping",
                () -> assertEquals(expectedResult.getName(), librarian.getName(),
                        "The librarian name was not mapped correctly from the request."),
                () -> assertEquals(expectedResult.getEmail(), librarian.getEmail(),
                        "The librarian email was not mapped correctly from the request."),
                () -> assertNotNull(expectedResult, "The librarian entity should not be null after mapping."),
                () -> assertNotNull(expectedResult.getAddress(),
                        "The librarian address should not be null after mapping."),
                () -> assertEquals(expectedResult.getAddress().getStreet(), librarian.getAddress().getStreet(),
                        "The librarian address street was not mapped correctly."),
                () -> assertEquals(expectedResult.getContact(), librarian.getContact(),
                        "The librarian contact was not mapped correctly from the request."),
                () -> assertEquals(expectedResult.getEmployeeCode(), librarian.getEmployeeCode(),
                        "The librarian employee code was not mapped correctly from the request."),
                () -> assertNotNull(expectedResult.getAdmin(), "The librarian entity should have an associated admin."),
                () -> assertNull(expectedResult.getAdmin().getId(),
                        "The librarian's admin is null")
        );
    }
}