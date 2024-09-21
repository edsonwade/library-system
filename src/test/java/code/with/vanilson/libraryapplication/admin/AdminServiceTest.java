package code.with.vanilson.libraryapplication.admin;

import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceConflictException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static code.with.vanilson.libraryapplication.common.utils.MessageProvider.getMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AdminServiceTest {
    private final AdminRepository adminRepository = mock(AdminRepository.class);
    private final AdminService adminService = new AdminService(adminRepository);
    public AdminMapperTest adminMapperTest;
    public List<Admin> admins;
    public Admin admin;
    String errorMessage = getMessage("library.admin.bad_request", 0L);

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
    @DisplayName("Test successful retrieval of all admins in AdminService")
    void testGetAllAdminsSuccess() {
        // Mock the behavior of adminRepository.findAll() to return a list of admins
        when(adminRepository.findAll()).thenReturn(admins);

        // Call the method under test to get the expected result
        var expected = adminService.getAllAdmins();

        // Call the auxiliary method to create the expected AdminResponse objects
        var actual = adminMapperTest.auxiliarMethodToAllAdminResponse();

        // Assert that the lists are equal
        assertEquals(expected, actual, "Retrieve all admins");

        // Assert that the objects are not the same instance
        assertNotSame(expected, actual, "The objects are not the same instance");

        // Verify that adminRepository.findAll() was called at least once
        verify(adminRepository, atLeast(1)).findAll();

        // Additional assertions to verify specific properties of the AdminResponse objects
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getId(), actual.get(i).getId(), "Admin ID matches");
            assertEquals(expected.get(i).getName(), actual.get(i).getName(), "Admin name matches");
            assertEquals(expected.get(i).getEmail(), actual.get(i).getEmail(), "Admin email matches");

        }
    }

    @Test
    @DisplayName("Get the Admin by provide Id - Success.")
    void testGetAdminById_found_with_success() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        // expected any value greater than zero.
        var expectedResult = adminService.getAdminById(1234_34546_00L);
        var actualResult = adminMapperTest.auxiliarMethodToAdminResponse();

        // Asserts
        assertEquals(expectedResult, actualResult, "Retrieve all admins");
        assertNotEquals(Long.valueOf(1234_34546_00L), expectedResult.getId(), "The id provide are not equals");
        assertNotNull(expectedResult, " The Object is not null");
        assertNotSame(actualResult, expectedResult, "Not same instantiate object ");
        assertNotSame(expectedResult, actualResult, "objects are not the same instance");

        verify(adminRepository, atLeast(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Get the Admin by provide Id - Success.")
    void testGetAdminByEmail_found_with_success() {
        when(adminRepository.findAdminByEmail(anyString())).thenReturn(Optional.of(admin));
        // expected any value greater than zero.
        var expectedResult = adminService.getAdminByEmail("test@test.com");
        var actualResult = adminMapperTest.auxiliarMethodToAdminResponse();

        // Asserts
        assertEquals(expectedResult, actualResult, "Retrieve all admins");
        assertNotEquals(Long.valueOf(1234_34546_00L), expectedResult.getId(), "The id provide are not equals");
        assertNotEquals("test@test.com", expectedResult.getEmail(), "The email provide are not equals");
        assertNotSame("test@test.com", expectedResult.getEmail(), "The email provide are not equals");
        assertNotNull(expectedResult, " The Object is not null");
        assertNotSame(actualResult, expectedResult, "Not same instantiate object ");
        assertNotSame(expectedResult, actualResult, "objects are not the same instance");

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
    @DisplayName("Verify bad request exception is thrown when id is zero or negative in getAdminById")
    void testGetAdminById_failed_with_bad_Request_exception_when_id_is_zero_or_negative() {
        // Mock the behavior of adminRepository.findById() to throw a ResourceBadRequestException for id <= 0
        when(adminRepository.findById(argThat(id -> id <= 0L))).thenThrow(
                new ResourceBadRequestException(errorMessage));
        // Verify that a ResourceBadRequestException is thrown when id is 1L
        assertThrows(ResourceBadRequestException.class, () -> adminService.getAdminById(0L), errorMessage);

        // Verify that adminRepository.findById() is never called for id <= 0
        verify(adminRepository, never()).findById(argThat(id -> id <= 0L));
    }

    /**
     * Tests that a {@link ResourceNotFoundException} is thrown when attempting
     * to retrieve an admin by an ID that does not exist in the database.
     *
     * <p>This test verifies that the repository's findById method is called
     * at least once when the ID does not exist.</p>
     */
    @Test
    @DisplayName("Verify ResourceNotFoundException is thrown for non-existent admin ID")
    void testGetAdminById_failed_with_not_found_exception_when_id_does_not_exists() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.getAdminById(1L), errorMessage);

        verify(adminRepository, atLeast(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Verify not found exception is thrown when email does not exists.")
    void testGetAdminByEmail_failed_with_not_found_exception_when_email_does_not_exists() {

        when(adminRepository.findAdminByEmail(anyString())).thenReturn(Optional.empty());
        var expectedMessage =
                assertThrows(ResourceNotFoundException.class, () -> adminService.getAdminByEmail("admin.@out.tst"));
        assertEquals("""
                        No admin found with the email "admin.@out.tst". Please verify the email and try again.""",
                expectedMessage.getMessage());
        verify(adminRepository, atLeast(1)).findAdminByEmail(anyString());
    }

    /**
     * Test for handling the case where the admin request object is null.
     * Verifies that a ResourceBadRequestException is thrown and no interaction with the repository occurs.
     */
    @DisplayName("Should throw ResourceBadRequestException for null admin request")
    @Test
    void testCreateAdmin_ThrowsBadRequestException_NullRequest() {
        // Given + When + Then
        assertThrows(ResourceBadRequestException.class, () -> adminService.createAdmin(null));
        verify(adminRepository, never()).existsAdminByEmail(anyString());
        verify(adminRepository, never()).save(any());
    }

    /**
     * Test for handling the case where the email already exists in the database.
     * Verifies that a ResourceConflictException is thrown and the save method is never called.
     */
    @DisplayName("Should throw ResourceConflictException when email already exists")
    @Test
    void testCreateAdmin_ThrowsConflictException_EmailExists() {
        // Given
        var adminRequest = adminMapperTest.auxiliarMethodToAdminRequest();

        // Mock the repository behavior
        when(adminRepository.existsAdminByEmail(adminRequest.getEmail())).thenReturn(true);

        // When + Then: Assert that the conflict exception is thrown
        var exception =
                assertThrows(ResourceConflictException.class, () -> adminService.createAdmin(adminRequest));

        // Retrieve the expected message using the MessageProvider
        var expectedMessage = adminService.formatMessage("library.admin.email_exists", adminRequest.getEmail());

        // Verify that the message is as expected
        assertEquals(expectedMessage, exception.getMessage());

        // Verify that the repository check for email uniqueness was called once
        verify(adminRepository, times(1)).existsAdminByEmail(adminRequest.getEmail());

        // Verify that the save method was never called
        verify(adminRepository, never()).save(any());
    }

    /**
     * Test for successfully creating a new admin in the database.
     * Verifies that the adminRepository.save method is called and returns the correct AdminResponse.
     */
    @DisplayName("Should successfully create a new admin")
    @Test
    void testCreateAdmin_Success() {
        // Given
        var adminRequest = adminMapperTest.auxiliarMethodToAdminRequest();
        adminRequest.setEmail("test@test.com");
        var adminEntity = admin;
        var savedAdmin = admin;
        savedAdmin.setId(2L);

        when(adminRepository.existsAdminByEmail(adminRequest.getEmail())).thenReturn(false);
        when(adminRepository.save(adminEntity)).thenReturn(savedAdmin);

        // When
        AdminResponse result = adminService.createAdmin(adminRequest);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId());
        verify(adminRepository, times(1)).save(adminEntity);
        verify(adminRepository, times(1)).existsAdminByEmail(adminRequest.getEmail());
    }

    /**
     * Test for handling the case where the email already exists in the database.
     * Verifies that a ResourceConflictException is thrown and the save method is never called.
     */
    @DisplayName("Should throw ResourceConflictException when email already exists")
    @Test
    void testCreateAdmin_ThrowsConflictException_UniqueConstraintViolation() {
        // Given
        var adminRequest = adminMapperTest.auxiliarMethodToAdminRequest();
        adminRequest.setEmail("johndoe@example.com");
        var adminEntity = admin;

        when(adminRepository.existsAdminByEmail(adminRequest.getEmail())).thenReturn(false);
        when(adminRepository.save(adminEntity)).thenThrow(DataIntegrityViolationException.class);

        // When + Then
        assertThrows(ResourceConflictException.class, () -> adminService.createAdmin(adminRequest));
        verify(adminRepository, times(1)).save(adminEntity);
    }

    /**
     * Test for successfully updating an admin in the database.
     * Verifies that the adminRepository.findById and save methods are called, and returns the correct AdminResponse.
     */
    @DisplayName("Should successfully update an existing admin")
    @Test
    void testUpdateAdmin_Success() {
        // Given
        var adminId = 1L;
        var adminRequest = adminMapperTest.auxiliarMethodToAdminRequest();
        var existingAdmin = admin;
        var updatedAdmin = admin;
        updatedAdmin.setId(2L);

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(existingAdmin));
        when(adminRepository.save(existingAdmin)).thenReturn(updatedAdmin);

        // When
        var result = adminService.updateAdmin(adminRequest, adminId);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId());
        verify(adminRepository, times(1)).findById(adminId);
        verify(adminRepository, times(1)).save(existingAdmin);
    }

    /**
     * Test for handling the case where the admin with the provided ID does not exist.
     * Verifies that a ResourceNotFoundException is thrown and the save method is not called.
     */

    @DisplayName("Should throw ResourceNotFoundException when admin not found")
    @Test
    void testUpdateAdmin_ThrowsNotFoundException() {
        // Given
        var adminId = 1L;
        var adminRequest = adminMapperTest.auxiliarMethodToAdminRequest();

        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        // When + Then
        assertThrows(ResourceNotFoundException.class, () -> adminService.updateAdmin(adminRequest, adminId));
        verify(adminRepository, times(1)).findById(adminId);
        verify(adminRepository, never()).save(any());
    }

    /**
     * Test for handling the case where the admin request object is null.
     * Verifies that a ResourceBadRequestException is thrown and no interaction with the repository occurs.
     */
    @DisplayName("Should throw ResourceBadRequestException for null admin request")
    @Test
    void testUpdateAdmin_ThrowsBadRequestException_NullRequest() {
        // Given
        Long adminId = 1L;

        // When + Then
        assertThrows(ResourceBadRequestException.class, () -> adminService.updateAdmin(null, adminId));
        verify(adminRepository, never()).findById(anyLong());
        verify(adminRepository, never()).save(any());
    }

    /**
     * Test for handling the case where an invalid admin ID (e.g., negative value) is provided.
     * Verifies that a ResourceBadRequestException is thrown and no interaction with the repository occurs.
     */
    @DisplayName("Should throw ResourceBadRequestException for invalid admin ID")
    @Test
    void testUpdateAdmin_ThrowsBadRequestException_InvalidAdminId() {
        // Given
        var adminId = -1L;

        // Mock the behavior of adminRepository.findById() to throw a ResourceBadRequestException for id <= 0
        when(adminRepository.findById(argThat(id -> id <= 0L))).thenThrow(
                new ResourceBadRequestException(errorMessage));

        // When + Then
        assertThrows(ResourceBadRequestException.class, () -> adminService.updateAdmin(new AdminRequest(), adminId));
        verify(adminRepository, never()).findById(anyLong());
        verify(adminRepository, never()).save(any());
    }

    /**
     * Test for successfully deleting an admin in the database.
     * Verifies that the adminRepository.findById and delete methods are called with the correct admin ID.
     */
    @DisplayName("Should successfully delete an admin by ID")
    @Test
    void testDeleteAdmin_Success() {
        // Given
        var adminId = 1L;

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
    @DisplayName("Should throw ResourceNotFoundException when admin not found")
    @Test
    void testDeleteAdmin_ThrowsNotFoundException() {
        // Given
        Long adminId = 1L;

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
    @DisplayName("Should throw ResourceBadRequestException for invalid admin ID")
    @Test
    void testDeleteAdmin_ThrowsBadRequestException_InvalidAdminId() {
        // Given
        var adminId = -1L;

        // Mock the behavior of adminRepository.findById() to throw a ResourceBadRequestException for id <= 0
        when(adminRepository.findById(argThat(id -> id <= 0L))).thenThrow(
                new ResourceBadRequestException(errorMessage));

        // When + Then
        assertThrows(ResourceBadRequestException.class, () -> adminService.deleteAdmin(adminId));
        verify(adminRepository, never()).findById(anyLong());
        verify(adminRepository, never()).delete(any());
    }

}