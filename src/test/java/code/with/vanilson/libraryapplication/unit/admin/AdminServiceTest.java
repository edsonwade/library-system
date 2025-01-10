package code.with.vanilson.libraryapplication.unit.admin;

import code.with.vanilson.libraryapplication.admin.*;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceConflictException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static code.with.vanilson.libraryapplication.common.utils.MessageProvider.getMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("Admin Service Test")
@SuppressWarnings("all")
class AdminServiceTest {
    public static final long ADMIN_ID = 0L;
    public static final long ID = 1L;
    public static final String PLEASE_VERIFY_THE_EMAIL_AND_TRY_AGAIN =
            "No admin found with the email \"admin.@out.tst\". Please verify the email and try again.";
    String errorMessage = getMessage("library.admin.bad_request", ADMIN_ID);
    String expected = getMessage("library.admin.cannot_be_null", ADMIN_ID);

    private final AdminRepository adminRepository = mock(AdminRepository.class);
    private final AdminService adminService = new AdminService(adminRepository);
    public AdminMapperTest adminMapperTest;
    public List<Admin> admins;
    public Admin admin;

    @BeforeEach
    void setUp() {
        adminMapperTest = new AdminMapperTest();
        admins = adminMapperTest.auxiliarMethodToAllAdmin();
        admin = adminMapperTest.auxiliarMethodToAdmin();
    }

    /**
     * Test method to verify the successful retrieval of all admins in AdminService.
     *
     * <p>
     * This test method mocks the behavior of adminRepository.findAll() to return a list of admins,
     * then calls the method under test to get the expected result. It compares the expected AdminResponse
     * objects with the actual ones created using an auxiliary method. Additional assertions are made to
     * verify specific properties of the AdminResponse objects.
     * </p>
     *
     * @implNote The method uses @link to refer to the auxiliary method auxiliarMethodToAllAdminResponse().
     */
    @Test
    @DisplayName("Should return a list of all admins successfully")
    void shouldReturnAllAdmins_WhenRepositoryFindsAll() {

        when(adminRepository.findAll()).thenReturn(admins);
        var expected = adminService.getAllAdmins();
        var actual = adminMapperTest.auxiliarMethodToAllAdminResponse();

        assertEquals(expected, actual, "Admin list should match");
        assertNotSame(expected, actual, "Objects should not be the same instance");
        verify(adminRepository, atLeast(1)).findAll();

        IntStream.range(0, expected.size()).forEach(i -> {
            Assertions.assertEquals(expected.get(i).getId(), actual.get(i).getId(), "Admin ID matches");
            Assertions.assertEquals(expected.get(i).getName(), actual.get(i).getName(), "Admin name matches");
            Assertions.assertEquals(expected.get(i).getEmail(), actual.get(i).getEmail(), "Admin email matches");
        });
    }

    @Test
    @DisplayName("Should throw an exception when admins is null")
    void shouldThrownBadRequest_WhenAdminIsNull() {
        when(adminRepository.findAll()).thenReturn(null);
        var message = assertThrows(ResourceBadRequestException.class, adminService::getAllAdmins);
        assertEquals(expected, message.getMessage());

        verify(adminRepository, atLeast(1)).findAll();

    }

    @DisplayName("Retrieve admins - should return empty list when no admin exists")
    @Test
    void shouldReturnEmptyListWhenNoAdminExists() {
        when(adminRepository.findAll()).thenReturn(Collections.emptyList());
        var expected = adminService.getAllAdmins();
        assertTrue(expected.isEmpty(), "Admin list should be empty");
        verify(adminRepository, atLeast(1)).findAll();

    }

    @Test
    @DisplayName("Should retrieve admin by ID successfully")
    void shouldReturnAdminById_WhenIdIsValid() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        // expected any value greater than zero.
        var expectedResult = adminService.getAdminById(1234_34546_00L);
        var actualResult = adminMapperTest.auxiliarMethodToAdminResponse();

        // Asserts
        Assertions.assertEquals(expectedResult, actualResult, "Retrieve all admins");
        assertNotEquals(Long.valueOf(1234_34546_00L), expectedResult.getId(), "The id provide are not equals");
        assertNotNull(expectedResult, " The Object is not null");
        Assertions.assertNotSame(actualResult, expectedResult, "Not same instantiate object ");
        Assertions.assertNotSame(expectedResult, actualResult, "objects are not the same instance");

        verify(adminRepository, atLeast(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should retrieve admin by email successfully")
    void shouldReturnAdminByEmail_WhenEmailIsValid() {
        when(adminRepository.findAdminByEmail(anyString())).thenReturn(Optional.of(admin));
        // expected any value greater than zero.
        var expectedResult = adminService.getAdminByEmail("test@test.com");
        var actualResult = adminMapperTest.auxiliarMethodToAdminResponse();

        // Asserts
        Assertions.assertEquals(expectedResult, actualResult, "Admin details should match");
        assertEquals("John Doe", expectedResult.getName(), "Admin name should match");
        assertNotNull(expectedResult, "Admin object should not be null");
        Assertions.assertNotSame(actualResult, expectedResult, "Objects should not be the same instance");

        verify(adminRepository, atLeast(1)).findAdminByEmail(anyString());
    }

    /**
     * Verify bad request exception is thrown when id is zero or negative in getAdminById.
     *
     * <p>
     * This test method verifies that a bad request exception is thrown when the id is zero or negative in the getAdminById method.
     * </p>
     *
     * @implNote This method is currently empty and needs to be implemented with the test logic.
     */
    @Test
    @DisplayName("Should throw BadRequestException when ID is zero or negative")
    void shouldThrowBadRequestException_WhenIdIsZeroOrNegative() {
        when(adminRepository.findById(argThat(id -> id <= ADMIN_ID))).thenThrow(
                new ResourceBadRequestException(errorMessage));
        var message = assertThrows(ResourceBadRequestException.class, () -> adminService.getAdminById(ADMIN_ID),
                errorMessage);
        assertEquals(errorMessage, message.getMessage());
        verify(adminRepository, never()).findById(argThat(id -> id <= ADMIN_ID));
    }

    /**
     * Tests that a {@link ResourceNotFoundException} is thrown when attempting
     * to retrieve an admin by an ID that does not exist in the database.
     *
     * <p>This test verifies that the repository's findById method is called
     * at least once when the ID does not exist.</p>
     */
    @Test
    @DisplayName("Should throw NotFoundException when admin ID does not exist")
    void shouldThrowNotFoundException_WhenAdminIdNotFound() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.empty());

        var message = assertThrows(ResourceNotFoundException.class, () -> adminService.getAdminById(ID), errorMessage);
        assertEquals("No admin found with ID 1. Please verify the ID and try again.", message.getMessage());

        verify(adminRepository, atLeast(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw NotFoundException when admin email does not exist")
    void shouldThrowNotFoundException_WhenAdminEmailNotFound() {
        when(adminRepository.findAdminByEmail(anyString())).thenReturn(Optional.empty());
        var expectedMessage =
                assertThrows(ResourceNotFoundException.class, () -> adminService.getAdminByEmail("admin.@out.tst"));
        assertEquals(PLEASE_VERIFY_THE_EMAIL_AND_TRY_AGAIN, expectedMessage.getMessage());
        verify(adminRepository, atLeast(1)).findAdminByEmail(anyString());
    }

    /**
     * Test for handling the case where the admin request object is null.
     * Verifies that a ResourceBadRequestException is thrown and no interaction with the repository occurs.
     */
    @DisplayName("Should throw BadRequestException when admin request is null")
    @Test
    void shouldThrowBadRequestException_WhenAdminRequestIsNull() {
        // Given + When + Then
        var message = assertThrows(ResourceBadRequestException.class, () -> adminService.createAdmin(null));
        assertEquals(expected, message.getMessage());
        verify(adminRepository, never()).existsAdminByEmail(anyString());
        verify(adminRepository, never()).save(any());
    }

    /**
     * Test for handling the case where the email already exists in the database.
     * Verifies that a ResourceConflictException is thrown and the save method is never called.
     */
    @DisplayName("Should throw ConflictException when email already exists")
    @Test
    void shouldThrowConflictException_WhenEmailAlreadyExists() {
        var adminRequest = adminMapperTest.auxiliarMethodToAdminRequest();
        when(adminRepository.existsAdminByEmail(adminRequest.getEmail())).thenReturn(true);
        var exception = assertThrows(ResourceConflictException.class, () -> adminService.createAdmin(adminRequest));
        var expectedMessage = adminService.formatMessage("library.admin.email_exists", adminRequest.getEmail());
        assertEquals(expectedMessage, exception.getMessage());
        verify(adminRepository, times(1)).existsAdminByEmail(adminRequest.getEmail());
        verify(adminRepository, never()).save(any());
    }

    /**
     * Test for successfully creating a new admin in the database.
     * Verifies that the adminRepository.save method is called and returns the correct AdminResponse.
     */
    @DisplayName("Should successfully create a new admin")
    @Test
    void shouldCreateAdminSuccessfully_WhenRequestIsValid() {
        var adminRequest = adminMapperTest.auxiliarMethodToAdminRequest();
        adminRequest.setEmail("test@test.com");
        var adminEntity = admin;
        var savedAdmin = admin;
        savedAdmin.setId(2L);
        when(adminRepository.existsAdminByEmail(adminRequest.getEmail())).thenReturn(false);
        when(adminRepository.save(adminEntity)).thenReturn(savedAdmin);
        AdminResponse result = adminService.createAdmin(adminRequest);
        assertNotNull(result);
        assertEquals(2L, result.getId());
        verify(adminRepository, times(1)).save(adminEntity);
        verify(adminRepository, times(1)).existsAdminByEmail(adminRequest.getEmail());
    }

    /**
     * Test for handling the case where the email already exists in the database.
     * Verifies that a ResourceConflictException is thrown and the save method is never called.
     */
    @DisplayName("Should throw ConflictException for unique constraint violation")
    @Test
    void shouldThrowConflictException_WhenUniqueConstraintViolation() {
        var adminRequest = adminMapperTest.auxiliarMethodToAdminRequest();
        adminRequest.setEmail("johndoe@example.com");
        var adminEntity = admin;
        when(adminRepository.existsAdminByEmail(adminRequest.getEmail())).thenReturn(false);
        when(adminRepository.save(adminEntity)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(ResourceConflictException.class, () -> adminService.createAdmin(adminRequest));
        verify(adminRepository, times(1)).save(adminEntity);
    }

    /**
     * Test for successfully updating an admin in the database.
     * Verifies that the adminRepository.findById and save methods are called, and returns the correct AdminResponse.
     */
    @DisplayName("Should successfully update an existing admin")
    @Test
    void shouldUpdateAdminSuccessfully_WhenAdminExists() {
        var adminId = 1L;
        var adminRequest = adminMapperTest.auxiliarMethodToAdminRequest();
        var existingAdmin = admin;
        var updatedAdmin = admin;
        updatedAdmin.setId(2L);
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(existingAdmin));
        when(adminRepository.save(existingAdmin)).thenReturn(updatedAdmin);
        var result = adminService.updateAdmin(adminRequest, adminId);
        assertNotNull(result);
        assertEquals(2L, result.getId());
        verify(adminRepository, times(1)).findById(adminId);
        verify(adminRepository, times(1)).save(existingAdmin);
    }

    /**
     * Test for handling the case where the admin with the provided ID does not exist.
     * Verifies that a ResourceNotFoundException is thrown and the save method is not called.
     */

    @DisplayName("Should throw NotFoundException when admin to update is not found")
    @Test
    void shouldThrowNotFoundException_WhenAdminToUpdateNotFound() {
        var adminId = 1L;
        var adminRequest = adminMapperTest.auxiliarMethodToAdminRequest();
        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> adminService.updateAdmin(adminRequest, adminId));
        verify(adminRepository, times(1)).findById(adminId);
        verify(adminRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BadRequestException when update request is null")
    void shouldThrowBadRequestException_WhenUpdateRequestIsNull() {
        Long adminId = 1L;
        assertThrows(ResourceBadRequestException.class, () -> adminService.updateAdmin(null, adminId));
        verify(adminRepository, never()).findById(anyLong());
        verify(adminRepository, never()).save(any());
    }

    /**
     * Test for handling the case where the admin request object is null.
     * Verifies that a ResourceBadRequestException is thrown and no interaction with the repository occurs.
     */
    @DisplayName("Should throw BadRequestException for invalid admin ID during update")
    @Test
    void shouldThrowBadRequestException_WhenAdminIdIsInvalid() {
        var adminId = -1L;
        when(adminRepository.findById(argThat(id -> id <= 0L))).thenThrow(
                new ResourceBadRequestException(errorMessage));
        assertThrows(ResourceBadRequestException.class, () -> adminService.updateAdmin(new AdminRequest(), adminId));
        verify(adminRepository, never()).findById(anyLong());
        verify(adminRepository, never()).save(any());
    }

    /**
     * Test for handling the case where an invalid admin ID (e.g., negative value) is provided.
     * Verifies that a ResourceBadRequestException is thrown and no interaction with the repository occurs.
     */
    @DisplayName("Should throw ResourceBadRequestException for invalid admin ID")
    @Test
    void shouldDeleteAdminSuccessfully_WhenIdIsValid() {
        var adminId = 1L;
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
        adminService.deleteAdmin(adminId);
        verify(adminRepository, times(1)).findById(adminId);
        verify(adminRepository, times(1)).delete(admin);
    }

    /**
     * Test for successfully deleting an admin in the database.
     * Verifies that the adminRepository.findById and delete methods are called with the correct admin ID.
     */
    @DisplayName("Should successfully delete an admin by ID")
    @Test
    void shouldThrowNotFoundException_WhenAdminToDeleteNotFound() {
        // Given
        var adminId = ID;

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));

        // When
        adminService.deleteAdmin(adminId);

        // Then
        verify(adminRepository, times(1)).findById(adminId);
        verify(adminRepository, times(1)).delete(admin);
    }

    /**
     * Test for handling the case where the admin with the provided ID does not exist.
     * Verifies that a ResourceNotFoundException is thrown and the delete method is not called.
     */
    @DisplayName("Should throw NotFoundException when deleting non-existing admin")
    @Test
    void testDeleteAdmin_ThrowsNotFoundException_WhenAdminNotFound() {
        // Given
        Long adminId = ID;

        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        // When + Then
        assertThrows(ResourceNotFoundException.class, () -> adminService.deleteAdmin(adminId));
        verify(adminRepository, times(1)).findById(adminId);
        verify(adminRepository, never()).delete(any());
    }

    /**
     * Test for handling the case where an invalid admin ID (e.g., negative value) is provided.
     * Verifies that a ResourceBadRequestException is thrown and no interaction with the repository occurs.
     */
    @DisplayName("Should throw BadRequestException for invalid admin ID during delete")
    @Test
    void shouldThrowBadRequestException_WhenAdminIdIsInvalidForDelete() {
        // Given
        var adminId = -ID;

        // Mock the behavior of adminRepository.findById() to throw a ResourceBadRequestException for id <= 0
        when(adminRepository.findById(argThat(id -> id <= ADMIN_ID))).thenThrow(
                new ResourceBadRequestException(errorMessage));

        // When + Then
        assertThrows(ResourceBadRequestException.class, () -> adminService.deleteAdmin(adminId));
        verify(adminRepository, never()).findById(anyLong());
        verify(adminRepository, never()).delete(any());
    }

}