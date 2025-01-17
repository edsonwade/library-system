package code.with.vanilson.libraryapplication.unit.librarian;

import code.with.vanilson.libraryapplication.TestDataHelper;
import code.with.vanilson.libraryapplication.admin.AdminMapper;
import code.with.vanilson.libraryapplication.admin.AdminRepository;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceConflictException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.librarian.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
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

    /**
     * This test ensures that the service returns a new librarian with the correct attributes
     * (e.g., email, name) when a librarian is created.
     */
    @DisplayName("Test librarian creation service")
    @Test
    void shouldReturnLibrarianWhenLibrarianIsCreated() {
        // Given
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(testDataHelper.createAdmin()));
        when(librarianRepository.save(any(Librarian.class))).thenReturn(librarian);

        // When
        var responseResult = librarianService.createLibrarian(request);

        // Then
        assertAll(
                () -> assertNull(responseResult.getId(), "The ID should be null when creating a new librarian."),
                () -> assertEquals(librarian.getEmail(), responseResult.getEmail(),
                        "The email of the response should match the librarian's email."),
                () -> assertEquals(librarian.getName(), responseResult.getName(),
                        "The name of the librarian should match."),
                () -> assertEquals(librarian.getContact(), responseResult.getContact(), "The phone number of the " +
                        "librarian should match."),
                () -> assertNotNull(String.valueOf(responseResult.getAddress()),
                        "The address of the librarian should not be null."),
                () -> assertNotNull(String.valueOf(responseResult.getAdmin()),
                        "The associated admin should not be null."),
                () -> assertEquals(librarian.getAdmin().getEmail(), responseResult.getAdmin().getEmail(),
                        "The email of the associated admin should match."),
                () -> assertTrue(responseResult.getName().contains("test"),
                        "The librarian's name should contain 'test'."),
                () -> assertDoesNotThrow(() -> librarianService.createLibrarian(request),
                        "Creating a librarian should not throw an exception.")
        );

        // Verify interaction with the repository
        verify(librarianRepository, times(2)).save(any(Librarian.class));
    }

    /**
     * This test ensures that when a librarian is created with an invalid admin ID,
     * a ResourceNotFoundException is thrown, and the appropriate error message is returned.
     * Specifically, it verifies that the error message correctly indicates that no admin
     * was found with the provided ID.
     */
    @DisplayName("Should throw ResourceNotFoundException when admin ID is not found during librarian creation")
    @Test
    void shouldThrowNotFoundExceptionWhenAdminIdIsNotFoundForCreatingLibrarian() {
        // Given
        when(librarianRepository.save(any(Librarian.class))).thenReturn(librarian);

        // Then
        var expectedMessage = assertThrows(ResourceNotFoundException.class,
                () -> librarianService.createLibrarian(request));
        var errorMessage = MessageFormat.format(getMessage("library.admin.not_found"), librarian.getAdmin().getId());
        assertEquals(errorMessage, expectedMessage.getMessage(), "Message should contain 'No Admin found with ID'");
        assertTrue(expectedMessage.getMessage().contains("No admin found with ID"),
                "Message should contain the 'No admin found with ID' part");

        // Verify interaction with the repository
        verify(librarianRepository, never()).save(any(Librarian.class));
    }

    /**
     * Test that verifies the creation of a librarian fails with a bad request exception
     * when the provided librarian data is null.
     * This simulates a scenario where the librarian creation service is called
     * with invalid input (null in this case), and checks that the appropriate
     * exception is thrown and the error message is as expected.
     *
     * @throws ResourceBadRequestException if the librarian data is null
     */
    @DisplayName("Should throw BadRequestException when creating a librarian with null data")
    @Test
    void shouldThrowBadRequestExceptionWhenCreatingLibrarianIsNull() {
        // Given
        when(librarianRepository.save(any(Librarian.class))).thenReturn(librarian);

        // Then
        var expectedMessage = assertThrows(ResourceBadRequestException.class,
                () -> librarianService.createLibrarian(null));

        var errorMessage =
                MessageFormat.format(getMessage("library.librarian.cannot_be_null"), librarian.getAdmin().getId());
        assertEquals(errorMessage, expectedMessage.getMessage(), "Message should contain 'No Admin found with ID'");
        assertTrue(expectedMessage.getMessage().contains("Cannot create a librarian"),
                "Should contain the same message");

        // Verify interaction with the repository
        verify(librarianRepository, never()).save(any(Librarian.class));
    }

    @DisplayName("Should throw ResourceConflictException when email already exists")
    @Test
    void shouldThrowResourceConflictExceptionWhenEmailExists() {
        // Given
        var librarianRequest = LibrarianRequest.builder()
                .email("existingEmail@example.com")
                .contact("+351 934-345-678")
                .employeeCode("E123")
                .build();
        when(librarianRepository.existsLibrarianByEmailAndIdNot(anyString(), anyLong())).thenReturn(true);

        // Then
        var exception = assertThrows(ResourceConflictException.class, () -> {
            librarianService.validateAndCheckLibrarianUniqueFieldsForUpdate(librarianRequest, 1L);
        });

        // Verify the error message
        assertEquals("library.librarian.email_exists", exception.getMessage());
    }

    @Test
    void findLibrarinByEmail_shouldReturnLibrarian() {
        // Given
        var email = "test@test.com";
        when(librarianRepository.findLibrariansByEmail(anyString())).thenReturn(Optional.of(librarian));
        //Wehn
        var response = librarianService.getLibrarianByEmail(email);

        //Then
        assertNotNull(response);
        assertEquals(email, response.getEmail());
        // Verify the error message
        verify(librarianRepository, times(1)).findLibrariansByEmail(email);
    }

    @Test
    void findLibrarinByEmail_shouldThrowNotFoundException_WhenProvideEmailDoesNotExist() {
        // Given
        var email = "librariantest.test";
        when(librarianRepository.findLibrariansByEmail(anyString())).thenReturn(Optional.empty());
        //Wehn
        assertThrows(ResourceNotFoundException.class, () -> librarianService.getLibrarianByEmail(email));
    }

    @DisplayName("Should throw ResourceConflictException when contact already exists")
    @Test
    void shouldThrowResourceConflictExceptionWhenContactExists() {
        // Given
        when(librarianRepository.existsLibrarianByContactAndIdNot(anyString(), anyLong())).thenReturn(true);

        // Then
        var exception = assertThrows(ResourceConflictException.class, () -> {
            librarianService.validateAndCheckLibrarianUniqueFieldsForUpdate(request, 1L);
        });

        // Verify the error message
        assertEquals("library.librarian.contact_exists", exception.getMessage());
    }

    @DisplayName("Should throw ResourceConflictException when employee code already exists")
    @Test
    void shouldThrowResourceConflictExceptionWhenEmployeeCodeExists() {
        // Given
        when(librarianRepository.existsLibrarianByEmployeeCodeAndIdNot(anyString(), anyLong())).thenReturn(true);

        // Then
        var exception = assertThrows(ResourceConflictException.class, () -> {
            librarianService.validateAndCheckLibrarianUniqueFieldsForUpdate(request, 1L);
        });

        // Verify the error message
        assertEquals("librarian.employee_with_code_Already_exists", exception.getMessage());
    }

    @DisplayName("Should catch DataIntegrityViolationException and throw ResourceConflictException")
    @Test
    void shouldCatchDataIntegrityViolationExceptionAndThrowResourceConflictException() {
        // Given
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(testDataHelper.createAdmin()));
        when(librarianRepository.save(any(Librarian.class))).thenThrow(DataIntegrityViolationException.class);

        // Then
        var message = "A librarian with the email test@test.com and contact +351 123-235-345 already exists.";
        var exception = assertThrows(ResourceConflictException.class, () -> {
            librarianService.createLibrarian(request);
        });

        // Verify the error message
        assertEquals(message, exception.getMessage(), "conatins the same message");
        assertTrue(exception.getMessage().contains("A librarian with the email"), "Should contain the same message");

        verify(adminRepository, times(1)).findById(anyLong());
    }

    @DisplayName("Should update librarian when librarian is saved")
    @Test
    void updateLibrarian_WhenLibrarianIsSave() {
        // Given
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(testDataHelper.createAdmin()));
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(librarian));
        when(librarianRepository.save(any(Librarian.class))).thenReturn(librarian);
        //When
        var librarianResponse = librarianService.updateLibrarian(request, 1L);
        //Then
        assertNotNull(librarianResponse);
        assertEquals(request.getEmail(), librarianResponse.getEmail(), "Should return the same email");
        assertEquals(request.getContact(), librarianResponse.getContact(), "Should return the same contact");
        verify(adminRepository, times(1)).findById(anyLong());
        verify(librarianRepository, times(1)).save(any(Librarian.class));
    }

    @DisplayName("Should throw NotFoundException when admin ID is not provided")
    @Test
    void updateLibrarian_ShouldThrowNotFoundException_WhenAdminIdIsNotProvided() {
        // When
        when(librarianRepository.save(any(Librarian.class))).thenReturn(librarian);
        //Then
        assertThrows(ResourceNotFoundException.class, () -> librarianService.updateLibrarian(request, 1L));

        verify(librarianRepository, never()).save(any(Librarian.class));

    }

    @DisplayName("Should throw BadRequestException when librarian is null")
    @Test
    void updateLibrarian_ShouldThrowBadRequestException_WhenLibrarianIsNull() {
        // When
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(testDataHelper.createAdmin()));
        when(librarianRepository.save(any(Librarian.class))).thenReturn(librarian);
        //Then
        assertThrows(ResourceBadRequestException.class, () -> librarianService.updateLibrarian(null, 1L));

        verify(librarianRepository, never()).save(any(Librarian.class));

    }

    @DisplayName("Should throw NotFoundException when librarian ID is not provided")
    @Test
    void updateLibrarian_ShouldThrowNotFoundException_WhenLibrarianIdIsNotProvided() {
        // When
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(testDataHelper.createAdmin()));
        when(librarianRepository.save(any(Librarian.class))).thenReturn(librarian);
        //Then
        assertThrows(ResourceNotFoundException.class, () -> librarianService.updateLibrarian(request, 99L));

        verify(librarianRepository, never()).save(any(Librarian.class));

    }

    @DisplayName("Should throw BadRequestException when librarian ID is less than or equal to zero")
    @Test
    void updateLibrarian_ShouldThrowNBadRequestException_WhenLibrarianIdIsLessThanOrEqualToZero() {
        // When
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(testDataHelper.createAdmin()));
        when(librarianRepository.save(any(Librarian.class))).thenReturn(librarian);
        //Then
        assertThrows(ResourceBadRequestException.class, () -> librarianService.updateLibrarian(request, -134L));

        verify(librarianRepository, never()).save(any(Librarian.class));

    }

    /**
     * Verifies that when a valid librarian ID is provided, the corresponding librarian is deleted from the service.
     * This test ensures that the service correctly deletes a librarian from the repository based on the provided ID.
     */
    @DisplayName("Should delete librarian when provided ID is present")
    @Test
    void deleteLibrarian_ShouldDelete_WhenProvidedIdIsPresent() {
        // Given
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(librarian));
        // When
        librarianRepository.deleteById(anyLong());
        librarianService.deleteLibrarianById(1L);

        // Then
        verify(librarianRepository, times(1)).deleteById(anyLong());
    }

    /**
     * Verifies that when an provide librarian ID (is less or eqauls to zero).
     * a {@link ResourceBadRequestException} is thrown.
     */
    @DisplayName("Should throw BadRequestException when provided ID is not present")
    @Test
    void deleteLibrarian_shoudThrowBadRequestException_WhenProvidedIdIsLessOrEqualsToZero() {
        // When
        var invalidId = 0L;
        // When
        var errorMessage = MessageFormat.format(getMessage("library.librarian.bad_request"), null);

        var expectedMessage = assertThrows(ResourceBadRequestException.class,
                () -> librarianService.deleteLibrarianById(invalidId));

        assertEquals(errorMessage, expectedMessage.getMessage());
        assertNotNull(String.valueOf(expectedMessage), "Expected message should not be null.");
        assertTrue(expectedMessage.getMessage().contains("Invalid data provided"),
                "Message should contain 'Invalid data provided'");

        // Verify that the repository method findById was not called because the exception was thrown
        verify(librarianRepository, times(0)).findById(invalidId);
    }

    /**
     * Verifies that when an invalid librarian ID (not present in the repository) is provided,
     * a {@link ResourceNotFoundException} is thrown.
     */
    @DisplayName("Should throw NotFoundException when provided ID is not present")
    @Test
    void deleteLibrarian_ShouldThrowNotFoundException_WhenProvidedIdIsNotPresent() {
        // Given
        var invalidId = 99L;
        // When
        var errorMessage = MessageFormat.format(getMessage("library.librarian.not_found"), invalidId);

        var expectedMessage = assertThrows(ResourceNotFoundException.class,
                () -> librarianService.deleteLibrarianById(invalidId));

        assertEquals(errorMessage, expectedMessage.getMessage());
        assertNotNull(String.valueOf(expectedMessage), "Expected message should not be null.");
        assertTrue(expectedMessage.getMessage().contains("No librarian found with ID"),
                "Message should contain 'No librarian found with ID'");

        // Verify that the repository method findById was not called because the exception was thrown
        verify(librarianRepository, times(1)).findById(invalidId);
    }

    @Test
    @DisplayName("Should patch librarian when valid updates are provided")
    void shouldPatchLibrarian_WhenValidUpdatesAreProvided() {
        // Given
        Long librarianId = 1L;
        Map<String, Object> updates = Map.of(
                "name", "Updated Name",
                "email", "updated.email@example.com",
                "address", testDataHelper.createAddressDTO(),
                "contact", "+351 987-654-321",
                "employeeCode", "EMPLO01"
        );
        Librarian existingLibrarian =
                new Librarian("Original Name", "original.email@example.com", testDataHelper.createAddress(),
                        "+351 123-456-789", "EMPLO01", testDataHelper.createAdmin());
        existingLibrarian.setAddress(AdminMapper.mapToAddress(testDataHelper.createAddressDTO()));

        when(librarianRepository.findById(librarianId)).thenReturn(Optional.of(existingLibrarian));
        when(librarianRepository.save(any(Librarian.class))).thenReturn(existingLibrarian);

        // When
        LibrarianResponse response = librarianService.patchLibrarian(librarianId, updates);

        // Then
        assertNotNull(response);
        assertEquals("Updated Name", response.getName());
        assertEquals("updated.email@example.com", response.getEmail());
        assertEquals("+351 987-654-321", response.getContact());
        verify(librarianRepository, times(1)).findById(librarianId);
        verify(librarianRepository, times(1)).save(existingLibrarian);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when librarian ID is not found")
    void shouldThrowResourceNotFoundException_WhenLibrarianIdIsNotFound() {
        // Given
        Long librarianId = 99L;
        Map<String, Object> updates = Map.of("name", "Updated Name");
        when(librarianRepository.findById(librarianId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> librarianService.patchLibrarian(librarianId, updates));
        verify(librarianRepository, times(1)).findById(librarianId);
        verify(librarianRepository, never()).save(any(Librarian.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when admin ID is not found")
    void shouldThrowResourceNotFoundException_WhenAdminIdIsNotFound() {
        // Given
        Long librarianId = 1L;
        Long adminId = 99L;
        Map<String, Object> updates = Map.of("admin", adminId);
        Librarian existingLibrarian =
                new Librarian("Original Name", "original.email@example.com", testDataHelper.createAddress(),
                        "+351 123-456-789", "EMPLO01", testDataHelper.createAdmin());
        when(librarianRepository.findById(librarianId)).thenReturn(Optional.of(existingLibrarian));
        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> librarianService.patchLibrarian(librarianId, updates));
        verify(librarianRepository, times(1)).findById(librarianId);
        verify(adminRepository, times(1)).findById(adminId);
        verify(librarianRepository, never()).save(any(Librarian.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when unsupported field is provided")
    void shouldThrowIllegalArgumentException_WhenUnsupportedFieldIsProvided() {
        // Given
        Long librarianId = 1L;
        Map<String, Object> updates = Map.of("unsupportedField", "value");
        Librarian existingLibrarian =
                new Librarian("Original Name", "original.email@example.com", testDataHelper.createAddress(),
                        "+351 123-456-789", "EMPLO01", testDataHelper.createAdmin());
        when(librarianRepository.findById(librarianId)).thenReturn(Optional.of(existingLibrarian));

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> librarianService.patchLibrarian(librarianId, updates));
        verify(librarianRepository, times(1)).findById(librarianId);
        verify(librarianRepository, never()).save(any(Librarian.class));
    }

}