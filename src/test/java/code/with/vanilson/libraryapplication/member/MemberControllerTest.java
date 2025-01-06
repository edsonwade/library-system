package code.with.vanilson.libraryapplication.member;

import code.with.vanilson.libraryapplication.person.AddressDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
@DisplayName("Member Controller Test")
class MemberControllerTest {
    private static final long ID = 1L;
    public static final String INVALID_INPUT_DATA = "Invalid input data.";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private MemberResponse memberResponse;

    private static final String API_URL = "/api/v1/members/create-member";

    @BeforeEach
    void setUp() {
        memberResponse = new MemberResponse();
        memberResponse.setId(ID);
        memberResponse.setName("John Doe");
        memberResponse.setEmail("john.doe@example.com");
        memberResponse.setContact("+123 456-789-123");
    }

    /**
     * Test the successful retrieval of all members.
     * It expects a 200 OK response with a list of member responses.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Test that retrieving all members returns a list of members with status 200 OK")
    void testRetrieveAllMembersSuccessfully() throws Exception {
        // Arrange: Prepare the response for all members (mocked)
        List<MemberResponse> memberResponses = Collections.singletonList(memberResponse);
        when(memberService.getAllMembers()).thenReturn(memberResponses);

        // Act: Perform the GET request to retrieve all members
        mockMvc.perform(get("/api/v1/members")
                        .contentType(APPLICATION_JSON))

                // Assert: Expect a 200 OK status and validate the response content
                .andExpect(status().isOk())  // 200 OK status
                .andExpect(jsonPath("$.length()").value(1))  // Ensure that the response contains 1 member
                .andExpect(jsonPath("$[0].name").value("John Doe"))  // Validate the member's name in the response
                .andDo(print());  // Print the response for debugging
    }

    /**
     * Test the successful retrieval of a member by ID.
     * It expects a 200 OK response with the correct member details.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Test that retrieving a member by ID returns the member's details with status 200 OK")
    void testRetrieveMemberByIdSuccessfully() throws Exception {
        // Arrange: Prepare the mock response for retrieving a member by ID
        when(memberService.getMemberById(ID)).thenReturn(memberResponse);

        // Act: Perform the GET request to retrieve the member by ID
        mockMvc.perform(get("/api/v1/members/1")
                        .contentType(APPLICATION_JSON))

                // Assert: Expect a 200 OK status and validate the member's details in the response
                .andExpect(status().isOk())  // 200 OK status
                .andExpect(jsonPath("$.name").value("John Doe"))  // Validate the member's name in the response
                .andDo(print());  // Print the response for debugging
    }

    /**
     * Tests the successful creation of a new member.
     * It expects a 201 CREATED status with the member's ID and name in the response.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Test that creating a new member returns a 201 CREATED status and the correct member details")
    void testCreateNewMemberSuccessfully() throws Exception {
        // Arrange: Prepare a valid MemberRequest with real values
        var newMemberRequest = createValidMemberRequest();

        // Prepare a mock MemberResponse
        var mockResponse = new MemberResponse();
        mockResponse.setId(ID);  // Mocked member ID
        mockResponse.setName("John Doe");  // Mocked member name

        // Mock the service method to return the mock response
        Mockito.when(memberService.createMember(Mockito.any(MemberRequest.class))).thenReturn(mockResponse);

        // Act: Perform the POST request and validate the result
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newMemberRequest)))  // Convert to JSON

                // Assert: Expect 201 status code and validate the response fields
                .andExpect(status().isCreated())  // Expect 201 status code
                .andExpect(jsonPath("$.id").value(1))  // Validate that the response contains ID 1
                .andExpect(
                        jsonPath("$.name").value("John Doe"));  // Validate that the response contains the correct name
    }

    /**
     * Tests that creating a new member with invalid data results in a Bad Request response (400).
     * It expects a 400 BAD_REQUEST status due to missing required fields.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Test that creating a new member with invalid data results in a 400 BAD_REQUEST status")
    void testCreateNewMemberBadRequest() throws Exception {
        // Arrange: Prepare an invalid MemberRequest (missing required fields)
        var invalidRequest = new MemberRequest();
        invalidRequest.setName("");  // Invalid name (empty)

        // Act: Perform the POST request and expect a BadRequest status (400)
        mockMvc.perform(post("/api/v1/members/create-member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidRequest)))  // Convert to JSON

                // Assert: Expect 400 status code
                .andExpect(status().isBadRequest());  // Expect 400 status code
    }

    /**
     * Tests the failure scenario when creating a new member with invalid data.
     * It expects a 400 BAD_REQUEST status with a specific error message.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Test that creating a new member with invalid data results in a 400 BAD_REQUEST status")
    void testCreateNewMemberFailure() throws Exception {
        // Arrange: Mock the service method to throw an exception for invalid data
        when(memberService.createMember(Mockito.any(MemberRequest.class))).thenThrow(
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data"));

        // Act: Perform the POST request with invalid data
        mockMvc.perform(post("/api/v1/members/create-member")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"\", \"email\":\"invalid\", \"contact\":\"\"}"))

                // Assert: Expect 400 status code and check if the error message matches
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(INVALID_INPUT_DATA))
                .andDo(print());
    }

    /**
     * Tests the successful update of a member.
     * It expects a 200 OK status with the updated member details.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Test that updating a member returns a 200 OK status and updated member details")
    void testUpdateMemberSuccessfully() throws Exception {
        // Arrange: Prepare a valid updated MemberRequest
        MemberRequest updatedMemberRequest = createValidMemberRequest();

        // Prepare a mock updated MemberResponse
        MemberResponse mockUpdatedResponse = new MemberResponse();
        mockUpdatedResponse.setId(ID);  // Set member ID
        mockUpdatedResponse.setName("John Doe Updated");  // Set updated name

        // Mock the service method to return the updated member response
        Mockito.when(memberService.updateMember(Mockito.any(MemberRequest.class), Mockito.eq(ID)))
                .thenReturn(mockUpdatedResponse);

        // Act: Perform the PUT request and validate the result
        mockMvc.perform(put("/api/v1/members/{memberId}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedMemberRequest)))  // Convert to JSON

                // Assert: Expect 200 status code and validate the response contains updated member data
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))  // Check if the response contains ID 1
                .andExpect(jsonPath("$.name").value(
                        "John Doe Updated"))  // Check if the response contains the updated name
                .andDo(print());
    }

    /**
     * Tests the failure scenario when updating a member with invalid data.
     * It expects a 400 BAD_REQUEST status with a specific error message.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Test that updating a member with invalid data results in a 400 BAD_REQUEST status")
    void testUpdateMemberFailure() throws Exception {
        // Arrange: Mock the service method to throw an exception for invalid data
        when(memberService.updateMember(Mockito.any(MemberRequest.class), Mockito.eq(ID)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data"));

        // Act: Perform the PUT request with invalid data
        mockMvc.perform(put("/api/v1/members/{memberId}", ID)
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"\", \"email\":\"invalid\", \"contact\":\"\"}"))

                // Assert: Expect 400 status code and check if the error message matches
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(INVALID_INPUT_DATA))
                .andDo(print());
    }

    /**
     * Tests the successful deletion of a member by ID.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    void testDeleteMemberById_Success() throws Exception {
        doNothing().when(memberService).deleteMemberById(ID);

        mockMvc.perform(delete("/api/v1/members/delete-member/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("The member with ID 1 has been successfully deleted."))
                .andDo(print());
    }

    /**
     * Tests the successful deletion of a member by ID.
     * It expects a 200 OK status with a success message indicating the member was deleted.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Test that deleting a member by ID results in a 200 OK status and success message")
    void testDeleteMemberByIdSuccessfully() throws Exception {
        // Arrange: Mock the service method to perform deletion successfully
        doNothing().when(memberService).deleteMemberById(ID);

        // Act: Perform the DELETE request to delete a member by ID
        mockMvc.perform(delete("/api/v1/members/delete-member/{memberId}", ID)
                        .contentType(APPLICATION_JSON))

                // Assert: Expect 200 status code and verify the success message
                .andExpect(status().isOk())
                .andExpect(content().string("The member with ID 1 has been successfully deleted."))
                .andDo(print());
    }

    /**
     * Test that when updating a member with invalid data (e.g., invalid email format),
     * the system responds with a 400 Bad Request status and returns a meaningful error message.
     */
    @Test
    @DisplayName("Test that updating member with invalid data returns 400 BadRequest")
    void testUpdateMemberWithInvalidDataReturnsBadRequest() throws Exception {
        // Arrange: Create an invalid member request (e.g., invalid email format)
        MemberRequest invalidMemberRequest = createInvalidMemberRequest();

        // Act: Send the request to the update endpoint
        mockMvc.perform(put("/api/v1/members/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidMemberRequest)))

                // Assert: Expect 400 BadRequest status and check for error message
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(INVALID_INPUT_DATA))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"));
    }

    /**
     * Helper method to create a MemberRequest.
     */
    private MemberRequest createValidMemberRequest() {
        var newMemberRequest = new MemberRequest();
        newMemberRequest.setName("John Doe");
        newMemberRequest.setEmail("unique.jane.doe@example.com");
        newMemberRequest.setContact("+123 456-789-125");

        // Assuming AddressDTO and other fields are correctly populated
        var addressDTO = new AddressDTO();
        addressDTO.setStreet("123 Elm Street");
        addressDTO.setCity("Sample City");
        addressDTO.setPostalCode("12345");
        newMemberRequest.setAddress(addressDTO);

        // Other fields (membershipStatus, librarianId, adminId)
        newMemberRequest.setMembershipStatus(MembershipStatus.ACTIVE);
        newMemberRequest.setLibrarianId(ID);
        newMemberRequest.setAdminId(ID);

        return newMemberRequest;
    }

    /**
     * Helper method to create an invalid MemberRequest with an invalid email.
     */
    private MemberRequest createInvalidMemberRequest() {
        MemberRequest invalidRequest = new MemberRequest();
        invalidRequest.setName("John Doe");
        invalidRequest.setEmail("invalid-email");  // Invalid email format
        invalidRequest.setContact("+123 456-789-125");
        invalidRequest.setMembershipStatus(MembershipStatus.ACTIVE);
        invalidRequest.setLibrarianId(1L);
        invalidRequest.setAdminId(1L);
        return invalidRequest;
    }
}
