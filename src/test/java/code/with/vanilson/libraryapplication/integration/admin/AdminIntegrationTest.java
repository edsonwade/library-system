package code.with.vanilson.libraryapplication.integration.admin;

import code.with.vanilson.libraryapplication.admin.*;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * AdminIntegrationTest
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-01-10
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@SuppressWarnings("all")
@AutoConfigureMockMvc
class AdminIntegrationTest {

    public static final String APPLICATION_HAL_JSON = " application/hal+json";
    @LocalServerPort
    private int port;
    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    private Long createdAdminId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        initializeDatabaseWithTestData();
        var adminResponse = createSampleAdminEntry();
        createdAdminId = adminResponse.getId();

    }

    private void initializeDatabaseWithTestData() {
        adminRepository.deleteAll();
        var admin = createAndSaveSampleAdmin();
        createdAdminId = admin.getId();

    }

    /**
     * Test to verify that all admins are listed successfully.
     */
    @Test
    @DisplayName("List All Admins - Success")
    void testListAllAdmins_Success() {
        given()
                .when()
                .get("/api/v1/admins")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(notNullValue());
    }

    @Test
    @DisplayName("List All Admins - Empty List Success")
    @Disabled("This test is disabled because the database is initialized with test data.")
    void testListAllAdmins_EmptyList_Success() {
        given()
                .when()
                .get("/api/v1/admins")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("isEmpty()", equalTo(true));
    }

    /**
     * Test to verify that an admin can be retrieved by ID successfully.
     */
    @Test
    @DisplayName("Get Admin by ID - Success")
    void testGetAdminById_Success() {
        given()
                .when()
                .get("/api/v1/admins/" + createdAdminId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(APPLICATION_HAL_JSON)
                .header("Content-Type", equalTo("application/hal+json"))
                .body(notNullValue())
                .body("id", equalTo(createdAdminId.intValue()))
                .body("name", equalTo("Sandro Doe"))
                .body("email", equalTo("sando@test.test"));
    }

    /**
     * Test to verify that a 404 Not Found status is returned when an admin is not found by ID.
     */
    @DisplayName("Get Admin by ID - Not Found")
    @Test
    void testGetAdminById_NotFound() {
        given()
                .when()
                .get("/api/v1/admins/999")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Get Admin by Email - Success")
    void testGetAdminByEmail_Success() {
        given()
                .when()
                .get("/api/v1/admins/email/sando@test.test")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(APPLICATION_HAL_JSON)
                .body("email", equalTo("sando@test.test"));
    }

    @Test
    @DisplayName("Get Admin by Email - Not Found")
    void testGetAdminByEmail_NotFound() {
        given()
                .when()
                .get("/api/v1/admins/email/nonexistent@test.test")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testCreateNewAdmin_Success() {
        var newAdmin = createSampleAdminRequest(1L);
        newAdmin.setName("Jane Doe");
        newAdmin.setEmail("unique.jane.doe@example.com"); // Ensure the email is unique
        newAdmin.setContact("+123 456-789-125"); // Ensure the contact is unique
        newAdmin.setAdminCode("ADMIN12345"); // Ensure the contact is unique

        given()
                .contentType(ContentType.JSON)
                .body(newAdmin)
                .when()
                .post("/api/v1/admins/create-admin")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo("Jane Doe"));
    }

    @Test
    void testUpdateAdmin_Success() {
        var updatedAdmin = createSampleAdminRequest(createdAdminId);
        updatedAdmin.setName("Updated Name");
        updatedAdmin.setEmail("updated.email@example.com"); // Ensure the email is unique
        updatedAdmin.setContact("+333 456-789-999"); // Ensure the contact is unique
        updatedAdmin.setAdminCode("UPDATED123"); // Ensure the admin code is unique

        given()
                .contentType(ContentType.JSON)
                .body(updatedAdmin)
                .when()
                .put("/api/v1/admins/update-admin/" + createdAdminId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Updated Name"))
                .body("email", equalTo("updated.email@example.com"))
                .body("contact", equalTo("+333 456-789-999"))
                .body("adminCode", equalTo("UPDATED123"));
    }

    @Test
    void testUpdateAdmin_Failure() {
        var updatedAdmin = createSampleAdminRequest(999L); // Use a non-existent ID
        updatedAdmin.setName("Non Existent Admin");
        updatedAdmin.setEmail("non.existent@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(updatedAdmin)
                .when()
                .put("/api/v1/admins/update-admin/999")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testDeleteAdminById_Success() {
        given()
                .when()
                .delete("/api/v1/admins/delete-admin/" + createdAdminId)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testDeleteAdminById_NotFound() {
        given()
                .when()
                .delete("/api/v1/admins/delete-admin/999")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private Admin createAndSaveSampleAdmin() {
        Admin admin1 = createSampleAdmin();
        return adminRepository.saveAndFlush(admin1);
    }

    private Address createSampleAddress() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setState("NY");
        address.setCity("Anytown");
        address.setCountry("USA");
        address.setPostalCode("12345");
        return address;
    }

    private Admin createSampleAdmin() {
        Admin admin1 = new Admin();
        admin1.setId(1L);
        admin1.setName("John Doe");
        admin1.setEmail("john.doe@example.com");
        admin1.setContact("+123 456-789-123");
        admin1.setAddress(createSampleAddress());
        admin1.setAdminCode("ABC123");
        admin1.setRole(Role.SYSTEM_ADMIN);
        return admin1;
    }

    private AdminResponse createSampleAdminEntry() {
        var adminRequest = createSampleAdminRequest(createdAdminId);
        return adminService.createAdmin(adminRequest);
    }

    private AdminRequest createSampleAdminRequest(Long createdAdminId) {
        var adminRequest = new AdminRequest();
        adminRequest.setName("Sandro Doe");
        adminRequest.setEmail("sando@test.test");
        adminRequest.setContact("+345 456-789-123");
        adminRequest.setAddress(AddressDTO.builder()
                .street("123 Main St")
                .state("NY")
                .city("Anytown")
                .country("USA")
                .postalCode("12345")
                .build());
        adminRequest.setAdminCode("ABCD123");
        adminRequest.setRole(Role.SUPER_ADMIN);
        return adminRequest;
    }

}