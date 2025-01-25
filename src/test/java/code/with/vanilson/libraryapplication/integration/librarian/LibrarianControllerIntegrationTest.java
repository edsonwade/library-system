package code.with.vanilson.libraryapplication.integration.librarian;

import code.with.vanilson.libraryapplication.TestDataHelper;
import code.with.vanilson.libraryapplication.admin.AdminRequest;
import code.with.vanilson.libraryapplication.admin.AdminResponse;
import code.with.vanilson.libraryapplication.admin.Role;
import code.with.vanilson.libraryapplication.librarian.LibrarianRequest;
import code.with.vanilson.libraryapplication.librarian.LibrarianResponse;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Librarian Controller Integration Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SuppressWarnings("unused")
class LibrarianControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private LibrarianRequest librarianRequest;

    private final TestDataHelper testDataHelper = new TestDataHelper();

    private static final String BASE_URL = "/api/v1";
    private static final String ADMIN_URL = BASE_URL + "/admins/create-admin";
    private static final String LIBRARIAN_URL = BASE_URL + "/librarians/create-librarian";

    @BeforeEach
    public void setUp() {
        librarianRequest = testDataHelper.createLibrarianRequest();

    }

    @DisplayName("Should create a new Admin - Success")
    @Test
    @Order(1)
    void testCreateAdmin_WhenValidRequestProvided_CreatesAdmin() throws Exception {
        // Arrange
        JSONObject jsonObject = createAdminRequestEntity();
        HttpHeaders headers = createHeaders();
        HttpEntity<String> adminRequestEntity = new HttpEntity<>(jsonObject.toString(), headers);
        // Act
        ResponseEntity<AdminResponse> adminResponse = restTemplate.postForEntity(
                ADMIN_URL,
                adminRequestEntity,
                AdminResponse.class
        );
        // Assert
        assertEquals(HttpStatus.CREATED, adminResponse.getStatusCode(), "Admin should be created");
        assertNotNull(adminResponse.getBody(), "Admin response body should not be null");
    }

    @DisplayName("Should create a new Librarian - Success")
    @Test
    @Order(2)
    void testCreateLibrarian_WhenValidRequestProvided_CreatesLibrarian() throws Exception {
        // Arrange
        librarianRequest.setAdmin(testDataHelper.createAdmin().getId());
        HttpEntity<String> request = createLibrarianRequestEntity(librarianRequest);

        // Act
        ResponseEntity<LibrarianResponse> response =
                restTemplate.postForEntity(LIBRARIAN_URL, request, LibrarianResponse.class);

        // Assert
        assertNotNull(response, "response should not be null");
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "status code should be CREATED");
        assertNotNull(response.getBody(), "response body should not be null");
        assertEquals(librarianRequest.getName(), response.getBody().getName(), "Librarian name should match");
    }

    @DisplayName("Should not create a new Librarian when Librarian entity is null - Failure")
    @Test
    @Order(3)
    void testCreateLibrarian_WhenLibrarianRequestIsNull_ReturnsBadRequest() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(null, createHeaders());

        // Act
        ResponseEntity<LibrarianResponse> response =
                restTemplate.postForEntity(LIBRARIAN_URL, request, LibrarianResponse.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "status code should be BAD REQUEST");
        assertNotNull(response.getBody(), "response body should not be null");
    }

    @DisplayName("Should return All Librarians - Success")
    @Test
    @Order(4)
    void testGetLibrarians_WhenLibrariansExists_ReturnsLibrarians() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(null, createHeaders());

        // Act
        ResponseEntity<List<LibrarianResponse>> response =
                restTemplate.exchange(BASE_URL + "/librarians", HttpMethod.GET, request,
                        new ParameterizedTypeReference<>() {});

        // Assert
        assertNotNull(response, "response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "status code should be OK");
        assertNotNull(response.getBody(), "response body should not be null");
        assertTrue(response.getStatusCode().is2xxSuccessful(), "Status code should be 200");
    }

    @DisplayName("Should return Librarian when Valid Librarian Id Provided - Success")
    @Test
    @Order(5)
    void testGetLibrarian_WhenValidLibrarianIdProvided_ReturnsLibrarian() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(null, createHeaders());

        // Act
        ResponseEntity<LibrarianResponse> response =
                restTemplate.exchange(BASE_URL + "/librarians/1", HttpMethod.GET, request, LibrarianResponse.class);

        // Assert
        assertNotNull(response, "response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "status code should be OK");
        assertNotNull(response.getBody(), "response body should not be null");
        assertTrue(response.getStatusCode().is2xxSuccessful(), "Status code should be 200");
        assertEquals("test 1", response.getBody().getName(), "Librarian name should match");
    }

    @DisplayName("Should return Not Found when Missing Librarian Id - Failure")
    @Test
    @Order(6)
    void testGetLibrarian_WhenMissingLibrarianId_ReturnsNotFound() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(null, createHeaders());

        // Act
        ResponseEntity<LibrarianResponse> response =
                restTemplate.exchange(BASE_URL + "/librarians/99", HttpMethod.GET, request, LibrarianResponse.class);

        // Assert
        assertNotNull(response, "response should not be null");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "status code should be 404");
        assertNotNull(response.getBody(), "response body should be null");
        assertTrue(response.getStatusCode().is4xxClientError(), "Status code should be 404");
    }

    @DisplayName("Should update an existing Librarian - Success")
    @Test
    @Order(7)
    void testUpdateLibrarian_WhenValidRequestProvided_UpdatesLibrarian() {
        // Arrange
        Long adminId = createAdmin("+345 234-345-677", new AddressDTO("123 Main Street", "New York", "NY", "Portugal"
                , "10001"));
        Long librarianId = createLibrarian(adminId,"+456 456-567-890", new AddressDTO("456 Main Street", "New York",
                "NY", "Portugal",
                "10002"));

        LibrarianRequest updateRequest = LibrarianRequest.builder()
                .name("Updated Name")
                .email("updatedemail@example.com")
                .contact("+531 990-900-002")
                .address(new AddressDTO("789 Main Street", "New York", "NY", "Angola", "34456"))
                .employeeCode("EMP-002")
                .admin(adminId)
                .build();

        HttpEntity<LibrarianRequest> updateRequestEntity = new HttpEntity<>(updateRequest, createHeaders());

        // Act
        ResponseEntity<LibrarianResponse> updateResponse =
                restTemplate.exchange(BASE_URL + "/librarians/" + librarianId, HttpMethod.PUT, updateRequestEntity,
                        LibrarianResponse.class);

        // Assert
        assertNotNull(updateResponse, "response should not be null");
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode(), "status code should be OK");
        assertNotNull(updateResponse.getBody(), "response body should not be null");
        assertEquals(updateRequest.getName(), updateResponse.getBody().getName(), "Librarian name should match");
    }

    @DisplayName("Should return Not Found when Updating Non-Existent Librarian - Failure")
    @Test
    @Order(8)
    void testUpdateLibrarian_WhenLibrarianIdNotFound_ReturnsNotFound() {
        // Arrange
        LibrarianRequest updateRequest = LibrarianRequest.builder()
                .name("Updated Name")
                .email("updatedemail@example.com")
                .contact("+531 990-900-002")
                .address(new AddressDTO("789 Main Street", "New York", "NY", "United States", "1004"))
                .employeeCode("EMP-002")
                .admin(1L) // Assuming admin with ID 1 exists
                .build();

        HttpEntity<LibrarianRequest> updateRequestEntity = new HttpEntity<>(updateRequest, createHeaders());

        // Act
        ResponseEntity<LibrarianResponse> updateResponse =
                restTemplate.exchange(BASE_URL + "/librarians/99", HttpMethod.PUT, updateRequestEntity,
                        LibrarianResponse.class);

        // Assert
        assertNotNull(updateResponse, "response should not be null");
        assertEquals(HttpStatus.NOT_FOUND, updateResponse.getStatusCode(), "status code should be 404");
        assertNotNull(updateResponse.getBody(), "response body should be null");
    }

    @DisplayName("Should delete an existing Librarian - Success")
    @Test
    @Order(9)
    void testDeleteLibrarian_WhenValidLibrarianIdProvided_DeletesLibrarian() {
        // Arrange
        Long adminId = createAdmin("+345 245-345-677", new AddressDTO("123 Main Street", "New York", "NY", "Spain",
                "4568"));
        Long librarianId = createLibrarian(adminId, "+456 456-567-800",new AddressDTO("456 Main Street", "New York",
                "NY", "Spain",
                "4567"));

        HttpEntity<String> request = new HttpEntity<>(null, createHeaders());

        // Act
        ResponseEntity<String> response =
                restTemplate.exchange(BASE_URL + "/librarians/delete-librarian/" + librarianId, HttpMethod.DELETE,
                        request, String.class);

        // Assert
        assertNotNull(response, "response should not be null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "status code should be OK");
        assertEquals("The librarian with ID " + librarianId + " has been successfully deleted.", response.getBody(),
                "Response message should match");
    }

    @DisplayName("Should return Not Found when Deleting Non-Existent Librarian - Failure")
    @Test
    @Order(10)
    void testDeleteLibrarian_WhenLibrarianIdNotFound_ReturnsNotFound() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(null, createHeaders());

        // Act
        ResponseEntity<String> response =
                restTemplate.exchange(BASE_URL + "/librarians/delete-librarian/99", HttpMethod.DELETE, request,
                        String.class);

        // Assert
        assertNotNull(response, "response should not be null");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "status code should be 404");
    }

    @DisplayName("Should return Bad Request when Creating Librarian with Missing Required Fields - Failure")
    @Test
    @Order(11)
    void testCreateLibrarian_WhenMissingRequiredFields_ReturnsBadRequest() {
        // Arrange
        LibrarianRequest invalidRequest = new LibrarianRequest();
        HttpEntity<LibrarianRequest> request = new HttpEntity<>(invalidRequest, createHeaders());

        // Act
        ResponseEntity<LibrarianResponse> response =
                restTemplate.postForEntity(LIBRARIAN_URL, request, LibrarianResponse.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "status code should be BAD REQUEST");
        assertNotNull(response.getBody(), "response body should not be null");
    }

    @DisplayName("Should return Bad Request when Updating Librarian with Invalid Data - Failure")
    @Test
    @Order(12)
    void testUpdateLibrarian_WhenInvalidDataProvided_ReturnsBadRequest() {
        // Arrange
        Long adminId = createAdmin("+345 237-345-677", new AddressDTO("123 Main Street", "New York", "NY", "Portugal"
                , "10001"));
        Long librarianId = createLibrarian(adminId, "+357 456-567-890",new AddressDTO("456 Main Street", "New York",
                "NY", "Portugal",
                "10002"));

        LibrarianRequest invalidUpdateRequest = LibrarianRequest.builder()
                .name("") // Invalid name
                .email("invalid-email") // Invalid email
                .contact("invalid-contact") // Invalid contact
                .address(new AddressDTO("", "", "", "", "")) // Invalid address
                .employeeCode("") // Invalid employee code
                .admin(adminId)
                .build();

        HttpEntity<LibrarianRequest> updateRequestEntity = new HttpEntity<>(invalidUpdateRequest, createHeaders());

        // Act
        ResponseEntity<LibrarianResponse> updateResponse =
                restTemplate.exchange(BASE_URL + "/librarians/" + librarianId, HttpMethod.PUT, updateRequestEntity,
                        LibrarianResponse.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, updateResponse.getStatusCode(), "status code should be BAD REQUEST");
        assertNotNull(updateResponse.getBody(), "response body should not be null");
    }

    @DisplayName("Should return Bad Request when Deleting Librarian with Invalid ID - Failure")
    @Test
    @Order(13)
    void testDeleteLibrarian_WhenInvalidIdProvided_ReturnsBadRequest() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(null, createHeaders());

        // Act
        ResponseEntity<String> response =
                restTemplate.exchange(BASE_URL + "/librarians/delete-librarian/-1", HttpMethod.DELETE, request,
                        String.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "status code should be BAD REQUEST");
        assertNotNull(response.getBody(), "response body should not be null");
    }

    // Create a librarian with the given admin id and address details
    private Long createLibrarian(Long adminId,String contact,
                                 AddressDTO address) {
        LibrarianRequest lib = new LibrarianRequest();
        lib.setName("Jane Doe");
        lib.setEmail("lane" + UUID.randomUUID() + "@example.com");
        lib.setContact(contact);
        lib.setEmployeeCode("EMP"+ UUID.randomUUID() + "-001");
        lib.setAdmin(adminId);
        lib.setAddress(address);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<LibrarianRequest> librarianRequestEntity = new HttpEntity<>(lib, headers);

        ResponseEntity<LibrarianResponse> librarianResponse = restTemplate.postForEntity(
                LIBRARIAN_URL,
                librarianRequestEntity,
                LibrarianResponse.class
        );

        assertEquals(HttpStatus.CREATED, librarianResponse.getStatusCode(), "Librarian should be created");
        assertNotNull(librarianResponse.getBody(), "Librarian response body should not be null");

        return librarianResponse.getBody().getId();
    }

    @DisplayName("Should return Conflict when Creating Librarian with Duplicate Email - Failure")
    @Test
    @Order(14)
    void testCreateLibrarian_WhenDuplicateEmailProvided_ReturnsConflict() {
        // Arrange
        Long adminId = createAdmin("+345 234-678-677", new AddressDTO("123 Main Street", "New York", "NY", "Portugal",
                "10001"));
        LibrarianRequest librarianRequest1 = new LibrarianRequest("Jane Doe", "janedoe@example.com", new AddressDTO(
                "456 Main Street", "New York", "NY", "Portugal", "10002"), " +531 990-900-001", "EMP-001", adminId);
        LibrarianRequest librarianRequest2 = new LibrarianRequest("John Smith", "janedoe@example.com",
                new AddressDTO("789 Main Street", "New York", "NY", "Portugal", "10003"), "+531 990-900-002", "EMP" +
                "-002", adminId);

        HttpEntity<LibrarianRequest> request1 = new HttpEntity<>(librarianRequest1, createHeaders());
        HttpEntity<LibrarianRequest> request2 = new HttpEntity<>(librarianRequest2, createHeaders());

        // Act
        restTemplate.postForEntity(LIBRARIAN_URL, request1, LibrarianResponse.class);
        ResponseEntity<LibrarianResponse> response =
                restTemplate.postForEntity(LIBRARIAN_URL, request2, LibrarianResponse.class);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode(), "status code should be CONFLICT");
    }

    @DisplayName("Should return Bad Request when Retrieving Librarian with Invalid ID Format - Failure")
    @Test
    @Order(15)
    void testGetLibrarian_WhenInvalidIdFormatProvided_ReturnsBadRequest() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(null, createHeaders());

        // Act
        ResponseEntity<LibrarianResponse> response = restTemplate.exchange(BASE_URL + "/librarians/-1", HttpMethod.GET
                , request, LibrarianResponse.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "status code should be BAD REQUEST");
    }

    @DisplayName("Should return Bad Request when Updating Librarian with Missing Required Fields - Failure")
    @Test
    @Order(16)
    void testUpdateLibrarian_WhenMissingRequiredFields_ReturnsBadRequest() {
        // Arrange
        Long adminId = createAdmin("+345 567-000-900", new AddressDTO("123 Main Street", "New York", "NY", "Portugal",
                "10001"));
        Long librarianId = createLibrarian(adminId,"+456 000-500-890", new AddressDTO("456 Main Street", "New York", "NY",
                        "Portugal", "10002"));

        LibrarianRequest invalidUpdateRequest = new LibrarianRequest();
        HttpEntity<LibrarianRequest> updateRequestEntity = new HttpEntity<>(invalidUpdateRequest, createHeaders());

        // Act
        ResponseEntity<LibrarianResponse> updateResponse =
                restTemplate.exchange(BASE_URL + "/librarians/" + librarianId, HttpMethod.PUT, updateRequestEntity,
                        LibrarianResponse.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, updateResponse.getStatusCode(), "status code should be BAD REQUEST");
    }

    @DisplayName("Should return Bad Request when Deleting Librarian with Non-Numeric ID - Failure")
    @Test
    @Order(17)
    void testDeleteLibrarian_WhenNonNumericIdProvided_ReturnsBadRequest() {
        // Arrange
        HttpEntity<String> request = new HttpEntity<>(null, createHeaders());

        // Act
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + "/librarians/delete-librarian/0",
                HttpMethod.DELETE, request, String.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "status code should be BAD REQUEST");
    }

    private static HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setDate(Instant.now());
        return headers;
    }

    private static JSONObject createAdminRequestEntity() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "John Doe");
        jsonObject.put("email", "johndoe@example.com");
        jsonObject.put("contact", "+531 990-900-000");
        jsonObject.put("adminCode", "ADM-001");
        jsonObject.put("role", "SYSTEM_ADMIN");

        JSONObject address = new JSONObject();
        address.put("street", "123 Main Street");
        address.put("city", "New York");
        address.put("country", "United States");
        address.put("state", "NY");
        address.put("postalCode", "10001");

        jsonObject.put("address", address);
        return jsonObject;
    }

    private HttpEntity<String> createLibrarianRequestEntity(LibrarianRequest librarianRequest) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(librarianRequest);
        return new HttpEntity<>(requestJson, createHeaders());
    }

    // Create an admin with the given address details
    private Long createAdmin(String contact, AddressDTO address) {
        AdminRequest adminRequest = new AdminRequest();
        adminRequest.setName("John Doe");
        adminRequest.setEmail("vani" + UUID.randomUUID() + "@test.com");
        adminRequest.setContact(contact);
        adminRequest.setAdminCode("ADM-" + UUID.randomUUID().toString().substring(0, 3).toUpperCase());
        adminRequest.setRole(Role.SYSTEM_ADMIN);
        adminRequest.setAddress(address);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<AdminRequest> adminRequestEntity = new HttpEntity<>(adminRequest, headers);

        ResponseEntity<AdminResponse> adminResponse = restTemplate.postForEntity(
                ADMIN_URL,
                adminRequestEntity,
                AdminResponse.class
        );

        assertEquals(HttpStatus.CREATED, adminResponse.getStatusCode(), "Admin should be created");
        assertNotNull(adminResponse.getBody(), "Admin response body should not be null");

        return adminResponse.getBody().getId();
    }

}