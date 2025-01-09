package code.with.vanilson.libraryapplication.admin;

import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@DisplayName("Admin Controller Test")
@Slf4j
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private MessageSource messageSource;

    @MockBean
    private AdminService adminService;

    private AdminResponse adminResponse;

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
        String expectedMessage = messageSource.getMessage("library.admin.cannot_be_null", null, Locale.getDefault());
        when(adminService.getAllAdmins()).thenThrow(new ResourceBadRequestException(expectedMessage));

        // Act: Perform the GET request
        mockMvc.perform(get(API_ADMIN_DETAIL))
                .andExpect(status().isBadRequest()) // Verify status code is 400
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verify response content type is JSON
                .andExpect(jsonPath("$.message").value(expectedMessage)); // Verify error message

        // Assert: Verify the service interaction
        verify(adminService, times(1)).getAllAdmins();
    }

}