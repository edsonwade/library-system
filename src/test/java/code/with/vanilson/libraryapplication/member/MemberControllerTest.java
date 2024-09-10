package code.with.vanilson.libraryapplication.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
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
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private MemberResponse memberResponse;

    @BeforeEach
    void setUp() {
        memberResponse = new MemberResponse();
        memberResponse.setId(1L);
        memberResponse.setName("John Doe");
        memberResponse.setEmail("john.doe@example.com");
        memberResponse.setContact("+123 456-789-123");
    }

    @Test
    void testListAllMembers_Success() throws Exception {
        List<MemberResponse> memberResponses = Collections.singletonList(memberResponse);
        when(memberService.getAllMembers()).thenReturn(memberResponses);

        mockMvc.perform(get("/api/v1/members")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andDo(print());
    }

    @Test
    void testGetMemberById_Success() throws Exception {
        when(memberService.getMemberById(1L)).thenReturn(memberResponse);

        mockMvc.perform(get("/api/v1/members/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andDo(print());
    }

    @Test
    void testCreateNewMember_Failure() throws Exception {
        when(memberService.createMember(Mockito.any(MemberRequest.class))).thenThrow(
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data"));

        mockMvc.perform(post("/api/v1/members/create-member")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"\", \"email\":\"invalid\", \"contact\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid input data."))
                .andDo(print());
    }

    @Test
    void testDeleteMemberById_Success() throws Exception {
        doNothing().when(memberService).deleteMemberById(1L);

        mockMvc.perform(delete("/api/v1/members/delete-member/1")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("The member with ID 1 has been successfully deleted."))
                .andDo(print());
    }
}
