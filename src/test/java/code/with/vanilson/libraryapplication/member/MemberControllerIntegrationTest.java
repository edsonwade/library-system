package code.with.vanilson.libraryapplication.member;

/**
 * MemberControllerIntegrationTest
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-01-06
 */

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminRepository;
import code.with.vanilson.libraryapplication.admin.Role;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianRepository;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@SuppressWarnings("all")
class MemberControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    // Store the IDs of the created entities
    private Long createdAdminId;
    private Long createdLibrarianId;
    private Long createdMemberId;

    @BeforeEach
    void setUp() {
        initializeDatabaseWithTestData();
        MemberResponse memberResponse = createSampleMemberEntry();
        createdMemberId = memberResponse.getId();
    }

    private void initializeDatabaseWithTestData() {
        memberRepository.deleteAll();
        adminRepository.deleteAll();
        librarianRepository.deleteAll();

        Admin admin1 = createAndSaveSampleAdmin();
        createdAdminId = admin1.getId();

        Librarian librarian1 = createAndSaveSampleLibrarian(admin1);
        createdLibrarianId = librarian1.getId();

    }

    private Admin createAndSaveSampleAdmin() {
        Admin admin1 = createSampleAdmin();
        return adminRepository.saveAndFlush(admin1);
    }

    private Librarian createAndSaveSampleLibrarian(Admin admin) {
        Librarian librarian1 = createSampleLibrarian(admin);
        return librarianRepository.saveAndFlush(librarian1);
    }

    private MemberResponse createSampleMemberEntry() {
        var memberRequest = createSampleMemberRequest(createdLibrarianId, createdAdminId);
        return memberService.createMember(memberRequest);
    }

    @Test
    void testListAllMembers_Success() {
        String url = "http://localhost:" + port + "/api/v1/members";
        ResponseEntity<MemberResponse[]> response = restTemplate.getForEntity(url, MemberResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testGetMemberById_NotFound() {
        var url = "http://localhost:" + port + "/api/v1/members/999";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void testGetMemberById_Success() {
        var url = "http://localhost:" + port + "/api/v1/members/" + createdMemberId;
        ResponseEntity<MemberResponse> response = restTemplate.getForEntity(url, MemberResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("John Doe");
    }

    @Test
    void testCreateNewMember_MissingFields() {
        var url = "http://localhost:" + port + "/api/v1/members/create-member";
        var newMember = new MemberRequest(); // Missing required fields
        var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<MemberRequest> request = new HttpEntity<>(newMember, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    // NOTE: may give 409 beacuse of unique email and contact
    @Test
    @Disabled("This test is disabled because the update operation is not yet implemented.")
    void testUpdateMember_Success() {
        var url = "http://localhost:" + port + "/api/v1/members/" + createdMemberId;
        var updatedMember = createSampleMemberRequest(1L, 1L);
        updatedMember.setName("Updated Name");
        updatedMember.setEmail("unique.email@example.com"); // Ensure the email is unique
        updatedMember.setContact("+123 456-789-124"); // Ensure the contact is unique
        var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<MemberRequest> request = new HttpEntity<>(updatedMember, headers);

        ResponseEntity<MemberResponse> response =
                restTemplate.exchange(url, HttpMethod.PUT, request, MemberResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Name");
        assertThat(response.getBody().getId()).isEqualTo(createdMemberId);
    }

    @Test
    void testUpdateMember_InvalidData() {
        var url = "http://localhost:" + port + "/api/v1/members/1";
        var updatedMember = createSampleMemberRequest(1L, 1L);
        updatedMember.setEmail("invalid-email"); // Invalid email format
        var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<MemberRequest> request = new HttpEntity<>(updatedMember, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    @Disabled("This test is disabled because the update operation is not yet implemented.")
    void testCreateNewMember_Success() {
        var url = "http://localhost:" + port + "/api/v1/members/create-member";
        var newMember = createSampleMemberRequest(1L, 1L);
        newMember.setEmail("unique.jane.doe@example.com"); // Ensure the email is unique
        newMember.setContact("+123 456-789-125"); // Ensure the contact is unique
        var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<MemberRequest> request = new HttpEntity<>(newMember, headers);

        ResponseEntity<MemberResponse> response = restTemplate.postForEntity(url, request, MemberResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("John Doe");
    }

    @Test
    @Disabled("This test is disabled because the update operation is not yet implemented.")
    void testDeleteMemberById_Success() {
        var url = "http://localhost:" + port + "/api/v1/members/delete-member/1";
        var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo("The member with ID 1 has been successfully deleted.");
    }

    @Test
    void testDeleteMemberById_NotFound() {
        var url = "http://localhost:" + port + "/api/v1/members/delete-member/999";
        var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    private MemberRequest createSampleMemberRequest(Long librarianId, Long adminId) {
        var memberRequest = new MemberRequest();
        memberRequest.setName("John Doe");
        memberRequest.setEmail("john.doe@example.com");
        memberRequest.setContact("+123 456-789-123");
        memberRequest.setAddress(createSampleAddressDTO());
        memberRequest.setMembershipStatus(MembershipStatus.ACTIVE);
        memberRequest.setLibrarianId(librarianId);
        memberRequest.setAdminId(adminId);
        return memberRequest;
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

    private AddressDTO createSampleAddressDTO() {
        AddressDTO address = new AddressDTO();
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

    private Librarian createSampleLibrarian(Admin admin) {
        Librarian librarian1 = new Librarian();
        librarian1.setId(1L);
        librarian1.setName("John Doe");
        librarian1.setEmail("john.doe@example.com");
        librarian1.setContact("+123 456-789-123");
        librarian1.setAddress(createSampleAddress());
        librarian1.setEmployeeCode("ABC123");
        librarian1.setAdmin(admin); // Set the managed admin
        return librarian1;
    }
}