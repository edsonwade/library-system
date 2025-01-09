package code.with.vanilson.libraryapplication.member.steps;

/**
 * MemberSteps
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-01-07
 */

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminRepository;
import code.with.vanilson.libraryapplication.admin.Role;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianRepository;
import code.with.vanilson.libraryapplication.member.MemberRepository;
import code.with.vanilson.libraryapplication.member.MemberRequest;
import code.with.vanilson.libraryapplication.member.MemberService;
import code.with.vanilson.libraryapplication.member.MembershipStatus;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@SuppressWarnings("all")
public class MemberSteps {

    private MvcResult result;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private MemberService memberService;  // Use the actual service (not mocked)

    @Given("the members exist")
    public void theMembersExist() {
        // Save to the database
        memberService.createMember(createValidMemberRequest()); // This inserts into H2 if using actual database
    }

    @When("I request to retrieve all members")
    public void iRequestToRetrieveAllMembers() throws Exception {
        mockMvc.perform(get("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Then("I should receive a list of members with status 200 OK")
    public void iShouldReceiveAListOfMembersWithStatus200OK() throws Exception {
        // Optionally, the status is already verified in @When,
        // but if you'd like to assert the response content again, you can capture and verify the body

        var result = mockMvc.perform(get("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Expect 200 OK status again (optional)
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        // Assert the response contains a list of members (you can improve this by checking the structure of the JSON)
        assertThat(responseContent).isNotEmpty();  // Ensure that the body is not empty

        // Example: Check that a member name appears in the response
        assertThat(responseContent).contains("John Doe");  // Replace with a valid member name in your database
    }

    @Given("the member with ID {long} exists")
    public void theMemberWithIDExists(Long id) {
        // Check if the member with the given ID exists, if not, create it
        var member = memberService.getMemberById(id); // Fetch member using your service layer
        if (member == null) {
            // If not, create a member with the given ID (or a similar id)
            memberService.createMember(createValidMemberRequest()); // Ensure that the member gets created
        }
    }

    @When("I request to retrieve the member by ID {long}")
    public void iRequestToRetrieveTheMemberByID(Long id) throws Exception {
        mockMvc.perform(get("/api/v1/members/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Then("I should receive the member details with status 200 OK")
    public void iShouldReceiveTheMemberDetailsWithStatus200OK() {
        // Assertions are done in the @When step
    }

    @Given("a valid member request")
    public void aValidMemberRequest() {
        // Create a valid member request using your existing helper methods
        MemberRequest validMemberRequest = createValidMemberRequest();

        // Log the request body
        try {
            String requestBody = new ObjectMapper().writeValueAsString(validMemberRequest);
            System.out.println("Request Body: " + requestBody);

            // Using MockMvc to simulate creating a member
            mockMvc.perform(post("/api/v1/members/create-member")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated()) // Expect a successful creation (status 201)
                    .andDo(result -> {
                        // Log the response
                        System.out.println("Response: " + result.getResponse().getContentAsString());
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("I request to create a new member")
    public void iRequestToCreateANewMember() throws Exception {
        String newMemberRequest =
                "{ \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"contact\": \"+123 456-789-123\" }";
        mockMvc.perform(post("/api/v1/members/create-member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMemberRequest))
                .andExpect(status().isCreated());
    }

    @Then("the member should be created with status 201 CREATED")
    public void theMemberShouldBeCreatedWithStatus201CREATED() {
        // Assertions are done in the @When step
    }

    @Given("a valid update request")
    public void aValidUpdateRequest() {
        // Create a member with ID 1 (or another existing member ID)
        var memberId = 1L; // This can be dynamically chosen based on your test context
        MemberRequest updateRequest = createValidMemberRequest(); // Modify this if necessary for update scenarios

        // Set the new details for the member (e.g., updating the name, contact info)
        updateRequest.setName("Updated Name");
        updateRequest.setContact("+123 456-789-999");

        // You can update the member by calling your service directly or mockMvc
        try {
            mockMvc.perform(put("/api/v1/members/" + memberId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(updateRequest)))
                    .andExpect(status().isOk()); // Expect a 200 OK status after a successful update
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("I request to update the member by ID {long}")
    public void iRequestToUpdateTheMemberByID(Long id) throws Exception {
        String updateMemberRequest =
                "{ \"name\": \"John Doe Updated\", \"email\": \"john.doe.updated@example.com\", \"contact\": \"+123 456-789-124\" }";
        mockMvc.perform(put("/api/v1/members/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateMemberRequest))
                .andExpect(status().isOk());
    }

    @Then("the member should be updated with status 200 OK")
    public void theMemberShouldBeUpdatedWithStatus200OK() {
        // Assertions are done in the @When step
    }


    @When("I request to delete the member by ID {long}")
    public void iRequestToDeleteTheMemberByID(Long id) throws Exception {
        result = mockMvc.perform(delete("/api/v1/members/delete-member/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Then("the member should be deleted with status 200 OK")
    public void theMemberShouldBeDeletedWithStatus200OK() throws Exception {
        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(200);

        String responseContent = result.getResponse().getContentAsString();
        assertThat(responseContent).isEqualTo("The member with ID 1 has been successfully deleted."); // Assuming the response body is empty on successful deletion
    }

    @When("I request to delete the member by ID {long} and it does not exist")
    public void iRequestToDeleteTheMemberByIDAndItDoesNotExist(Long id) throws Exception {
        mockMvc.perform(delete("/api/v1/members/delete-member/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Then("I should receive a 404 Not Found status")
    public void iShouldReceiveA404NotFoundStatus() {
        // Assertions are done in the @When step
    }

    private MemberRequest createValidMemberRequest() {
        // Assuming AddressDTO and other fields are correctly populated
        var address1 = new Address();
        address1.setStreet("123 Main Street");
        address1.setCity("New York");
        address1.setCountry("United States");
        address1.setState("NY");
        address1.setPostalCode("10001");

        // Save ADMIN
        Admin admin1 = new Admin();
        admin1.setName("John");
        admin1.setEmail("unique_" + UUID.randomUUID() + "@example.com"); // Generate a unique email
        admin1.setContact("unique_contact" + UUID.randomUUID() + "+123 456-789-123");
        admin1.setAddress(address1);
        admin1.setAdminCode("unique_admin" + UUID.randomUUID() + "ABC123");
        admin1.setRole(Role.SYSTEM_ADMIN);
        admin1 = adminRepository.saveAndFlush(admin1); // Save admin to get managed entity

        // Save LIBRARIAN
        var librarian = new Librarian();
        librarian.setName("JOHN_DOE");
        librarian.setEmail("unique_" + UUID.randomUUID() + "@example.com"); // Generate a unique email
        librarian.setContact("unique_contact" + UUID.randomUUID() + "+235 456-789-233");
        librarian.setAddress(address1);
        librarian.setEmployeeCode("unique_emp" + UUID.randomUUID() + "ABC23");
        librarian.setAdmin(admin1);
        librarian = librarianRepository.saveAndFlush(librarian); // Save librarian to get managed entity

        return getMemberRequest(librarian, admin1);
    }

    private static MemberRequest getMemberRequest(Librarian librarian, Admin admin1) {
        var dto = new AddressDTO();
        dto.setStreet("123 Main Street");
        dto.setCity("New York");
        dto.setCountry("United States");
        dto.setState("NY");
        dto.setPostalCode("10001");

        // Prepopulate H2 database with test members
        var newMemberRequest = new MemberRequest();
        newMemberRequest.setName("John Doe");
        newMemberRequest.setEmail("unique_" + UUID.randomUUID() + "@example.com"); // Generate a unique email
        newMemberRequest.setContact("unique_contact" + UUID.randomUUID() + "+123 456-789-125");
        newMemberRequest.setAddress(dto);
        newMemberRequest.setMembershipStatus(MembershipStatus.ACTIVE);
        newMemberRequest.setLibrarianId(librarian.getId());
        newMemberRequest.setAdminId(admin1.getId());
        return newMemberRequest;
    }
}