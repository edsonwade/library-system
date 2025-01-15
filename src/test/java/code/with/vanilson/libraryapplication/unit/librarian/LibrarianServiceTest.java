package code.with.vanilson.libraryapplication.unit.librarian;

import code.with.vanilson.libraryapplication.TestDataHelper;
import code.with.vanilson.libraryapplication.admin.AdminRepository;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.librarian.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static code.with.vanilson.libraryapplication.common.utils.MessageProvider.getMessage;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")
@DisplayName("Librarian Service Test")
class LibrarianServiceTest {

    /**
     * Mocked {@link LibrarianRepository} for testing purposes.
     * This is used to simulate interactions with the repository in unit tests.
     */
    private final LibrarianRepository librarianRepository = mock(LibrarianRepository.class);
    /**
     * Mocked {@link AdminRepository} for testing purposes.
     * This simulates interactions with the admin repository in unit tests.
     */
    private final AdminRepository adminRepository = mock(AdminRepository.class);

    /**
     * {@link LibrarianService} instance used in the tests.
     * This is the service being tested in the unit tests, interacting with the mocked repositories.
     */
    private LibrarianService librarianService;

    /**
     * List of {@link Librarian} entities used in the tests.
     * This is used to mock data returned by the repository and for testing service methods.
     */
    private List<Librarian> librarians;

    /**
     * {@link LibrarianResponse} representing the librarian data in response format.
     * This object is used for testing the mapping from {@link Librarian} entity to response DTO.
     */
    private LibrarianResponse response;

    /**
     * {@link LibrarianRequest} representing the librarian data in request format.
     * This object is used for testing the mapping from request DTO to {@link Librarian} entity.
     */
    private LibrarianRequest request;

    /**
     * {@link Librarian} entity used for validating the mapping and business logic in the tests.
     * This object contains actual librarian data used in different test cases.
     */
    private Librarian librarian;

    /**
     * {@link TestDataHelper} class used to generate test data such as librarian entities, requests, and responses.
     * This helps avoid redundancy in test setup and facilitates creating consistent test data across multiple tests.
     */
    private final TestDataHelper testDataHelper = new TestDataHelper();

    /**
     * Initializes the necessary objects and dependencies before each test.
     * <p>
     * This method is executed before each test in the test class to ensure a clean and consistent environment for testing.
     * It sets up the {@link LibrarianService} instance, mocks the {@link LibrarianRepository} and {@link AdminRepository},
     * creates a sample {@link Librarian} entity, and generates lists of librarians for testing.
     * Additionally, it creates response and request objects for mapping and testing purposes.
     * </p>
     */
    @BeforeEach
    void setUp() {
        librarianService = new LibrarianService(librarianRepository, adminRepository);

        librarian =
                new Librarian("test 1", "test@test.com", testDataHelper.createAddress(), "+351 123-235-345", "EMPLO01",
                        testDataHelper.createAdmin());

        librarians = List.of(
                new Librarian("test 1", "test@test.com", testDataHelper.createAddress(), "+351 123-235-345", "EMPLO01",
                        testDataHelper.createAdmin()),
                new Librarian("test 2", "test1@test.com", testDataHelper.createAddress(), "+351 122-232-342", "EMPLO02",
                        testDataHelper.createAdmin()));

        response = testDataHelper.createLibrarianResponse();
        request = testDataHelper.createLibrarianRequest();
    }

    /**
     * Verifies that the librarian service returns all existing librarians when requested.
     *
     * @throws Exception If any unexpected exception occurs during the test.
     */
    /**
     * Verifies that when librarians exist in the repository,
     * the {@link LibrarianService#getAllLibrarians} method returns all librarians.
     */
    @Test
    @DisplayName("Should return all librarians when librarians exist")
    void shouldReturnAllLibrarians_When_Librarians_Exist() {
        // given
        when(librarianRepository.findAll()).thenReturn(librarians);

        // when
        var results = librarianService.getAllLibrarians();

        // then
        assertNotNull(results.toString(), "The results should not be null");
        assertEquals(librarian.getId(), results.get(0).getId(), "The ID of the first librarian should match");
        assertEquals(librarian.getName(), results.get(0).getName(), "The name of the first librarian should match");
        assertFalse(results.isEmpty(), "The results list should not be empty");

        // Verify that findAll() was called at least once
        verify(librarianRepository, atLeast(1)).findAll();
    }

    /**
     * Verifies that when a valid librarian ID is provided, the corresponding librarian is returned from the service.
     * This test ensures that the service correctly fetches a librarian from the repository based on the provided ID.
     */
    @Test
    @DisplayName("Should return librarian when provided ID exists")
    void shouldReturnLibrarianWhenProvidedIdExists() {
        // given
        when(librarianRepository.findById(1L)).thenReturn(Optional.of(librarian));
        // when
        var responseResult = librarianService.getLibrarianById(1L);
        //then
        assertAll(
                () -> assertNotNull(String.valueOf(responseResult), "The response should not be null"),

                () -> assertEquals(librarian.getId(), responseResult.getId(), "The librarian ID should match"),

                () -> assertEquals(librarian.getName(), responseResult.getName(), "The librarian name should match"),

                () -> assertEquals(librarian.getEmail(), responseResult.getEmail(), "The librarian email should match"),

                () -> assertEquals(librarian.getContact(), responseResult.getContact(),
                        "The librarian contact number should match")
        );

        verify(librarianRepository, atLeast(1)).findById(1L);

    }

    /**
     * Verifies that when an invalid librarian ID (not present in the repository) is provided,
     * a {@link ResourceNotFoundException} is thrown.
     * <p>
     * This test ensures that the service correctly handles non-existent librarian IDs by throwing the appropriate exception.
     * The test also verifies that the exception message contains the expected details and that the repository method is called.
     * </p>
     */
    @DisplayName("Should throw ResourceNotFoundException when provided ID is not present")
    @Test
    void shouldThrowNotFoundExceptionWhenProvidedIdIsNotPresent() {
        // given
        var invalidId = 99L;
        // when
        var errorMessage = MessageFormat.format(getMessage("library.librarian.not_found"), invalidId);

        var expectedMessage = assertThrows(ResourceNotFoundException.class,
                () -> librarianService.getLibrarianById(invalidId));

        assertEquals(errorMessage, expectedMessage.getMessage());
        assertNotNull(String.valueOf(expectedMessage), "Expected message should not be null.");
        assertTrue(expectedMessage.getMessage().contains("No librarian found with ID"),
                "Message should contain 'No librarian found with ID'");

        // Verify that the repository method findById was not called because the exception was thrown
        verify(librarianRepository, times(1)).findById(invalidId);

    }

    /**
     * Verifies that when an invalid ID (less than or equal to zero) is provided,
     * the {@link InvalidIdException} is thrown.
     * This test ensures that the service correctly handles invalid IDs by throwing the appropriate exception.
     */
    @DisplayName("Should throw InvalidIdException when provided ID is less than or equal to zero")
    @Test
    void shouldThrowInvalidIdExceptionWhenProvidedIdIsLessThanOrEqualToZero() {
        // given
        var invalidId = 0L;
        // when
        var errorMessage = MessageFormat.format(getMessage("library.librarian.bad_request"), null);

        var expectedMessage = assertThrows(ResourceBadRequestException.class,
                () -> librarianService.getLibrarianById(invalidId));

        assertEquals(errorMessage, expectedMessage.getMessage());
        assertNotNull(String.valueOf(expectedMessage), "Expected message should not be null.");
        assertTrue(expectedMessage.getMessage().contains("Invalid data provided"),
                "Message should contain 'Invalid data provided'");

        // Verify that the repository method findById was not called because the exception was thrown
        verify(librarianRepository, times(0)).findById(invalidId);
    }

}