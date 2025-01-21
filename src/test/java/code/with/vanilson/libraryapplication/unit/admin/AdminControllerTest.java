package code.with.vanilson.libraryapplication.unit.admin;

import code.with.vanilson.libraryapplication.admin.*;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceConflictException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static code.with.vanilson.libraryapplication.admin.AdminService.formatMessage;
import static code.with.vanilson.libraryapplication.common.utils.MessageProvider.getMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Test Admin Controller")
@AutoConfigureMockMvc(addFilters = false) //skip CSRF token .. not best solution.
@SpringBootTest
@ActiveProfiles(profiles = "test")
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private AdminService adminService;

    private AdminResponse adminResponse;
    private AdminResponse newAdminResponse;
    private AdminRequest adminRequest;

    private static final String API_ADMIN_DETAIL = "http://localhost:8080/api/v1/admins";

    @BeforeEach
    void setUp() {
        adminResponse = AdminResponse.builder()
                .id(1L)
                .name("Admin ")
                .email("vanilson@test.test")
                .address(AddressDTO.builder()
                        .street("1234 Main St")
                        .city("Springfield")
                        .state("IL")
                        .country("USA")
                        .postalCode("62701")
                        .build())
                .contact("+351 123-456-789")
                .adminCode("admin123")
                .role(Role.SYSTEM_ADMIN)
                .build();

        adminRequest = AdminRequest.builder()
                .name("New Admin")
                .email("newadmin@test.test")
                .address(AddressDTO.builder()
                        .street("5678 Elm St")
                        .city("Metropolis")
                        .state("NY")
                        .country("USA")
                        .postalCode("10001")
                        .build())
                .contact("+351 987-654-321")
                .adminCode("newadmin123")
                .role(Role.SYSTEM_ADMIN)
                .build();

        newAdminResponse = AdminResponse.builder()
                .id(2L)
                .name("New Admin")
                .email("newadmin@test.test")
                .address(AddressDTO.builder()
                        .street("5678 Elm St")
                        .city("Metropolis")
                        .state("NY")
                        .country("USA")
                        .postalCode("10001")
                        .build())
                .contact("+351 987-654-321")
                .adminCode("newadmin123")
                .role(Role.SYSTEM_ADMIN)
                .build();
    }

    /**
     * Test to verify that the API retrieves all admins successfully,
     * returning the expected HTTP status and all admin details.
     *
     * @throws Exception if the request fails
     */
    @DisplayName("Retrieve all admins - should return HTTP 200 OK and all admin details")
    @Test
    void retrieveAllAdmins_ShouldReturnExpectedStatusAndAllAdmins() throws Exception {
        // Arrange: Prepare the response for all admins (mocked)
        List<AdminResponse> adminResponses = Collections.singletonList(adminResponse);
        when(adminService.getAllAdmins()).thenReturn(adminResponses);

        // Act: Perform the GET request
        mockMvc.perform(get(API_ADMIN_DETAIL))
                .andExpect(status().isOk()) // Verify status code is 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verify response content type is JSON

                // Verify JSON response matches the mocked admin data
                .andExpect(jsonPath("$[0].id").value(adminResponse.getId()))
                .andExpect(jsonPath("$[0].name").value(adminResponse.getName()))
                .andExpect(jsonPath("$[0].email").value(adminResponse.getEmail()))
                .andExpect(jsonPath("$[0].address.street").value(adminResponse.getAddress().getStreet()))
                .andExpect(jsonPath("$[0].address.city").value(adminResponse.getAddress().getCity()))
                .andExpect(jsonPath("$[0].address.state").value(adminResponse.getAddress().getState()))
                .andExpect(jsonPath("$[0].address.country").value(adminResponse.getAddress().getCountry()))
                .andExpect(jsonPath("$[0].address.postalCode").value(adminResponse.getAddress().getPostalCode()))
                .andExpect(jsonPath("$[0].contact").value(adminResponse.getContact()))
                .andExpect(jsonPath("$[0].adminCode").value(adminResponse.getAdminCode()))
                .andExpect(jsonPath("$[0].role").value(adminResponse.getRole().toString()));

        // Assert: Verify the size and content of the response list
        assertEquals(1, adminResponses.size(), "The size of adminResponses should be 1");
        assertEquals(adminResponse, adminResponses.get(0),
                "The returned adminResponse should match the mocked response");
        assertNotNull(adminResponses, "The adminResponses should not be null");

        verify(adminService, times(1)).getAllAdmins();
    }

    /**
     * Tests the behavior of retrieving all admins when no admins exist in the system.
     * <p>
     * This test simulates the scenario where the admin service returns an empty list
     * of admins. It verifies that the controller correctly returns an HTTP 204 No Content
     * status and that the service method is called once.
     * </p>
     */
    @DisplayName("Retrieve all admins - should return HTTP 204 No Content when no admins exist")
    @Test
    
    void shouldReturnEmptyListAndExpectedStatusWhenNoAdminsExist() throws Exception {
        when(adminService.getAllAdmins()).thenReturn(Collections.emptyList());
        // Act: Perform the GET request
        mockMvc.perform(get(API_ADMIN_DETAIL))
                .andExpect(status().isNoContent());

        // Assert: Verify the service interaction
        verify(adminService, times(1)).getAllAdmins();
    }

    /**
     * Test to verify that the API returns HTTP 400 Bad Request
     * when the admin list is null.
     *
     * @throws Exception if the request fails
     */
    @DisplayName("Retrieve admins - should return HTTP 400 Bad Request when the admin list is null")
    @Test
    
    void retrieveAdmins_ShouldReturnBadRequest_WhenNoAdminsExist() throws Exception {
        // Arrange: Mock the service to throw a ResourceBadRequestException
        var expectedMessage = formatMessage(getMessage("library.admin.cannot_be_null", (Object) null));
        when(adminService.getAllAdmins()).thenThrow(new ResourceBadRequestException(expectedMessage));

        // Act: Perform the GET request
        mockMvc.perform(get(API_ADMIN_DETAIL))
                .andExpect(status().isBadRequest()) // Verify status code is 400
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verify response content type is JSON
                .andExpect(jsonPath("$.message").value(expectedMessage)); // Verify error message

        // Assert: Verify the service interaction
        verify(adminService, times(1)).getAllAdmins();
    }

    /**
     * Test to verify that the API retrieves a specific admin successfully,
     * returning the expected HTTP status and the specific admin details.
     *
     * @throws Exception if the request fails
     */
    @DisplayName("Retrieve a specific admin by provide the id - should return HTTP 200 OK and the admin details")
    @Test
    
    void retrieveExistingAdminsReturnsExpectedStatusAndAdmin() throws Exception {
        // Arrange: Prepare the response for all admins (mocked)
        when(adminService.getAdminById(adminResponse.getId())).thenReturn(adminResponse);

        // Act: Perform the GET request
        mockMvc.perform(get("http://localhost:8080/api/v1/admins/%d".formatted(adminResponse.getId())))
                .andExpect(status().isOk()) // Verify status code is 200

                // Verify JSON response matches the mocked admin data
                .andExpect(jsonPath("$.id").value(adminResponse.getId()))
                .andExpect(jsonPath("$.name").value(adminResponse.getName()))
                .andExpect(jsonPath("$.email").value(adminResponse.getEmail()))
                .andExpect(jsonPath("$.role").value(adminResponse.getRole().toString()));

        // Assert: Verify the size and content of the response list
        assertEquals(1L, adminResponse.getId(), "should be teh same");
        assertEquals("Admin ", adminResponse.getName(), "should be the same");
        assertNotNull(adminResponse, "The adminResponses should not be null");

        verify(adminService, times(1)).getAdminById(adminResponse.getId());
    }

    /**
     * Test to verify that the API returns HTTP 404 Not Found when a specific admin
     * is requested by ID but does not exist in the system.
     *
     * <p>This test simulates the scenario where the admin service returns a valid
     * admin response for a given ID. It verifies that the controller correctly
     * returns an HTTP 200 OK status and the expected admin details.</p>
     *
     * @throws Exception if the request fails
     */
    @DisplayName("Retrieve a specific admin by providing the ID - should return HTTP 404 Not Found")
    @Test
    
    void retrieveAdmins_ShouldReturnNotFound_WhenNoAdminsExist() throws Exception {
        // Arrange: Mock the service to throw a ResourceNotFoundException
        var nonExistentAdminId = 999L;
        var expectedMessage = formatMessage(getMessage("library.admin.not_found"), nonExistentAdminId);

        when(adminService.getAdminById(nonExistentAdminId))
                .thenThrow(new ResourceNotFoundException(expectedMessage));

        // Act: Perform the GET request
        mockMvc.perform(get("http://localhost:8080/api/v1/admins/{id}", nonExistentAdminId))
                .andExpect(status().isNotFound()) // Verify status code is 404
                .andExpect(jsonPath("$.message").value(expectedMessage)); // Verify error message

        // Assert: Verify the service interaction
        verify(adminService, times(1)).getAdminById(nonExistentAdminId);
    }

    /**
     * Test to verify that the API retrieves a specific admin successfully,
     * returning the expected HTTP status and the specific admin details.
     *
     * @throws Exception if the request fails
     */
    @DisplayName("Retrieve a specific admin by provide the email - should return HTTP 200 OK and the admin details")
    @Test
    
    void retrieveExistingAdminsByEmailAndReturnsTheExpectedStatusAndAdmin() throws Exception {
        // Arrange: Prepare the response for all admins (mocked)
        when(adminService.getAdminByEmail(adminResponse.getEmail())).thenReturn(adminResponse);

        // Act: Perform the GET request
        mockMvc.perform(get("http://localhost:8080/api/v1/admins/email/%s".formatted(adminResponse.getEmail())))
                .andExpect(status().isOk())

                // Verify JSON response matches the mocked admin data
                .andExpect(jsonPath("$.id").value(adminResponse.getId()))
                .andExpect(jsonPath("$.name").value(adminResponse.getName()))
                .andExpect(jsonPath("$.email").value(adminResponse.getEmail()))
                .andExpect(jsonPath("$.role").value(adminResponse.getRole().toString()));

        // Assert: Verify the size and content of the response list
        assertEquals(1L, adminResponse.getId(), "should be the equals");
        assertTrue(adminResponse.getId() > 0, "should be greater than zero");
        assertEquals("vanilson@test.test", adminResponse.getEmail(), "should be the same");
        assertNotNull(adminResponse, "The adminResponses should not be null");

        verify(adminService, times(1)).getAdminByEmail(adminResponse.getEmail());
    }

    @DisplayName("Retrieve a specific admin by providing the email - should return HTTP 404 Not Found")
    @Test
    
    void retrieveAdminsByEmail_ShouldReturnNotFound_WhenNoAdminsExist() throws Exception {
        // Arrange: Prep"are the response for all admins (mocked)
        var email = "test@test.test";
        var expectedMessage = formatMessage("library.admin_email_not_found", email);

        when(adminService.getAdminByEmail(email)).thenThrow(new ResourceNotFoundException(
                expectedMessage));
        // Act: Perform the GET request
        mockMvc.perform(get("http://localhost:8080/api/v1/admins/email/%s".formatted(email)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(expectedMessage));

        // Assert: Verify the service interaction
        verify(adminService, times(1)).getAdminByEmail(email);
    }

    /**
     * Test to verify that the API creates a new admin successfully,
     * returning the expected HTTP status and the created admin details.
     *
     * @throws Exception if the request fails
     */
    @DisplayName("Create a new admin - should return HTTP 201 Created and the admin details")
    @Test
    
    void createAdmin_ShouldReturnCreatedStatusAndAdmin() throws Exception {
        // Arrange

        when(adminService.createAdmin(any(AdminRequest.class))).thenReturn(newAdminResponse);

        // Act & Assert
        mockMvc.perform(post("http://localhost:8080/api/v1/admins/create-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(adminRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newAdminResponse.getId()))
                .andExpect(jsonPath("$.name").value(newAdminResponse.getName()))
                .andExpect(jsonPath("$.email").value(newAdminResponse.getEmail()))
                .andExpect(jsonPath("$.address.street").value(newAdminResponse.getAddress().getStreet()))
                .andExpect(jsonPath("$.address.city").value(newAdminResponse.getAddress().getCity()))
                .andExpect(jsonPath("$.address.state").value(newAdminResponse.getAddress().getState()))
                .andExpect(jsonPath("$.address.country").value(newAdminResponse.getAddress().getCountry()))
                .andExpect(jsonPath("$.address.postalCode").value(newAdminResponse.getAddress().getPostalCode()))
                .andExpect(jsonPath("$.contact").value(newAdminResponse.getContact()))
                .andExpect(jsonPath("$.adminCode").value(newAdminResponse.getAdminCode()))
                .andExpect(jsonPath("$.role").value(newAdminResponse.getRole().toString()));

        assertEquals(newAdminResponse, adminService.createAdmin(adminRequest), "should be the same");
        assertNotNull(newAdminResponse, "The newAdminResponse should not be null");

        verify(adminService, times(2)).createAdmin(any(AdminRequest.class));
    }

    /**
     * Test to verify that the API returns HTTP 400 Bad Request
     * when the admin request is null or required fields are missing.
     *
     * @throws Exception if the request fails
     */
    @Test
    @DisplayName("Create a new admin - should return HTTP 400 Bad Request when admin request is null")
    
    void createAdmin_returnsBadRequest_whenAdminRequestIsNull() throws Exception {
        // Arrange
        var expectedMessage =
                formatMessage("library.admin.invalid_request_body");  // This should match the new error message
        // Act & Assert
        mockMvc.perform(post("/api/v1/admins/create-admin")
                        .contentType(MediaType.APPLICATION_JSON))  // No request body
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedMessage));

        verify(adminService, times(0)).createAdmin(any(AdminRequest.class));  // Ensure service is not called
    }

    @Test
    @DisplayName("Create a new admin - should return HTTP 400 Bad Request when name is missing")
    
    void createAdmin_returnsBadRequest_whenNameIsMissing() throws Exception {
        // Arrange
        var adminRequestNew = new AdminRequest();
        adminRequest.setEmail("test@example.com");  // Valid email
        adminRequest.setAddress(new AddressDTO()); // Valid address
        adminRequest.setContact("+351 234-567-890"); // Valid contact
        adminRequest.setAdminCode("admin123");     // Valid admin code
        adminRequest.setRole(Role.SYSTEM_ADMIN);          // Valid role

        var expectedMessage =
                formatMessage("library.admin.input_data");  // The message defined in the validation annotations
        // Act & Assert
        mockMvc.perform(post("/api/v1/admins/create-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(adminRequestNew)))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedMessage));  // Check the error message

        verify(adminService, times(0)).createAdmin(any(AdminRequest.class));  // Ensure service is not called
    }

    @Test
    @DisplayName("Create a new admin - should return HTTP 400 Bad Request when email is invalid")
    
    void createAdmin_returnsBadRequest_whenEmailIsInvalid() throws Exception {
        // Arrange
        var newAdminReq = new AdminRequest();
        newAdminReq.setName("John Doe");
        newAdminReq.setEmail("mockmock.com");  // Invalid email format (should not be valid)
        newAdminReq.setAddress(new AddressDTO());
        newAdminReq.setContact("+1 234-567-890");
        newAdminReq.setAdminCode("admin123");
        newAdminReq.setRole(Role.SUPER_ADMIN);

        var expectedMessage = formatMessage("library.admin.input_data");  // Expected error message

        // Act & Assert
        mockMvc.perform(post("/api/v1/admins/create-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newAdminReq)))
                .andExpect(status().isBadRequest())  // Expecting 400 Bad Request
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedMessage));// Validate the error message

        verify(adminService, times(0)).createAdmin(any(AdminRequest.class));  // Ensure service is not called
    }

    @Test
    @DisplayName("Create a new admin - should return HTTP 409 Conflict when email already exists")
    
    void createAdmin_returnsConflict_whenEmailAlreadyExists() throws Exception {
        // Arrange
        var request = new AdminRequest();
        request.setName("John Doe");
        request.setEmail("mock@mock.com");
        request.setAddress(new AddressDTO());
        request.setContact("+1 234-567-890");
        request.setAdminCode("admin123");
        request.setRole(Role.SYSTEM_ADMIN);

        var expectedMessage =
                formatMessage("library.admin.email_exists");

        // Mock the repository to simulate the email already existing
        when(adminRepository.existsAdminByEmail(request.getEmail())).thenReturn(true);

        // Mock service to simulate behavior when the email exists (it should throw a ResourceConflictException)
        when(adminService.createAdmin(any(AdminRequest.class)))
                .thenThrow(new ResourceConflictException(expectedMessage));

        // Act & Assert
        mockMvc.perform(post("/api/v1/admins/create-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isConflict())  // Expecting HTTP 409 Conflict
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedMessage));  // Check the error message

        // Verify that the service was called
        verify(adminService, times(1)).createAdmin(any(AdminRequest.class));
    }

    /**
     * Test to verify that the API updates an existing admin successfully,
     * returning the expected HTTP status and the updated admin details.
     *
     * @throws Exception if the request fails
     */
    @DisplayName("Update an existing admin - should return HTTP 200 OK and the updated admin details")
    
    @Test
    void updateAdmin_ShouldReturnOkStatusAndUpdatedAdmin() throws Exception {
        // Arrange
        when(adminService.updateAdmin(any(AdminRequest.class), eq(adminResponse.getId())))
                .thenReturn(adminResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/admins/update-admin/{adminId}", adminResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(adminRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(adminResponse.getId()))
                .andExpect(jsonPath("$.name").value(adminResponse.getName()))
                .andExpect(jsonPath("$.email").value(adminResponse.getEmail()))
                .andExpect(jsonPath("$.address.street").value(adminResponse.getAddress().getStreet()))
                .andExpect(jsonPath("$.address.city").value(adminResponse.getAddress().getCity()))
                .andExpect(jsonPath("$.address.state").value(adminResponse.getAddress().getState()))
                .andExpect(jsonPath("$.address.country").value(adminResponse.getAddress().getCountry()))
                .andExpect(jsonPath("$.address.postalCode").value(adminResponse.getAddress().getPostalCode()))
                .andExpect(jsonPath("$.contact").value(adminResponse.getContact()))
                .andExpect(jsonPath("$.adminCode").value(adminResponse.getAdminCode()))
                .andExpect(jsonPath("$.role").value(adminResponse.getRole().toString()));

        verify(adminService, times(1)).updateAdmin(any(AdminRequest.class), eq(adminResponse.getId()));
    }

    /**
     * Test to verify that the API returns HTTP 404 Not Found
     * when attempting to update a non-existent admin.
     *
     * @throws Exception if the request fails
     */
    @DisplayName("Update an existing admin - should return HTTP 404 Not Found when admin does not exist")
    
    @Test
    void updateAdmin_ShouldReturnNotFound_WhenAdminDoesNotExist() throws Exception {
        // Arrange
        Long nonExistentAdminId = 999L;
        var expectedMessage = formatMessage("library.admin.not_found", nonExistentAdminId);
        when(adminService.updateAdmin(any(AdminRequest.class), eq(nonExistentAdminId)))
                .thenThrow(new ResourceNotFoundException(expectedMessage));

        // Act & Assert
        mockMvc.perform(put("/api/v1/admins/update-admin/{adminId}", nonExistentAdminId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(adminRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedMessage));

        verify(adminService, times(1)).updateAdmin(any(AdminRequest.class), eq(nonExistentAdminId));
    }

    /**
     * Test to verify that the API deletes an existing admin successfully,
     * returning the expected HTTP status and a success message.
     *
     * @throws Exception if the request fails
     */
    @Test
    @DisplayName("Delete an existing admin - should return HTTP 200 OK and a success message")
    
    void deleteAdmin_ShouldReturnOkStatusAndSuccessMessage() throws Exception {
        // Arrange
        doNothing().when(adminService).deleteAdmin(adminResponse.getId());
        var expectedMessage = formatMessage("library.admin.delete_success", adminResponse.getId());

        // Act & Assert
        mockMvc.perform(delete("http://localhost:8080/api/v1/admins/delete-admin/{adminId}", adminResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedMessage));

        verify(adminService, times(1)).deleteAdmin(adminResponse.getId());
    }

    @Test
    @DisplayName(
            "Delete an existing admin - should return HTTP 400 Bad Request when admin provide value is equal to or less than zero")
    
    void deleteAdmin_ShouldThrownBadRequestWhenTheValueIsEqualsOrLessThanZero() throws Exception {
        // Arrange
        Long invalidAdminId = 0L; // or any value less than or equal to zero
        var expectedMessage = formatMessage("library.admin.bad_request", invalidAdminId);
        doThrow(new ResourceBadRequestException(expectedMessage)).when(adminService).deleteAdmin(invalidAdminId);

        // Act & Assert
        mockMvc.perform(delete("http://localhost:8080/api/v1/admins/delete-admin/{adminId}", invalidAdminId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedMessage));

        verify(adminService, times(1)).deleteAdmin(invalidAdminId);
    }

    /**
     * Test to verify that the API returns HTTP 404 Not Found
     * when attempting to delete a non-existent admin.
     *
     * @throws Exception if the request fails
     */
    @DisplayName("Delete an existing admin - should return HTTP 404 Not Found when admin does not exist")
    @Test
    
    void deleteAdmin_ShouldReturnNotFound_WhenAdminDoesNotExist() throws Exception {
        // Arrange
        Long invalidAdminId = 999L; // or any value less than or equal to zero
        var expectedMessage = formatMessage("library.admin.not_found", invalidAdminId);
        doThrow(new ResourceNotFoundException(expectedMessage)).when(adminService).deleteAdmin(invalidAdminId);
        // Act & Assert
        mockMvc.perform(delete("http://localhost:8080/api/v1/admins/delete-admin/{adminId}", invalidAdminId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedMessage));

        verify(adminService, times(1)).deleteAdmin(invalidAdminId);
    }

}