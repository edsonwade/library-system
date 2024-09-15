//package code.with.vanilson.libraryapplication.fine;
//
//import code.with.vanilson.libraryapplication.admin.AdminResponse;
//import code.with.vanilson.libraryapplication.admin.AdminService;
//import code.with.vanilson.libraryapplication.admin.Role;
//import code.with.vanilson.libraryapplication.librarian.LibrarianResponse;
//import code.with.vanilson.libraryapplication.librarian.LibrarianService;
//import code.with.vanilson.libraryapplication.member.MemberResponse;
//import code.with.vanilson.libraryapplication.member.MemberService;
//import code.with.vanilson.libraryapplication.member.MembershipStatus;
//import code.with.vanilson.libraryapplication.person.AddressDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class FineServiceTest {
//
//    @Mock
//    private FineRepository fineRepository;
//
//    @Mock
//    private MemberService memberService;
//
//    @Mock
//    private LibrarianService librarianService;
//
//    @Mock
//    private AdminService adminService;
//
//    @InjectMocks
//    private FineService fineService;
//
//    private FineRequest fineRequest;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize mock responses
//        LibrarianResponse librarianResponse = createLibrarianResponse();
//        AdminResponse adminResponse = createAdminResponse();
//        MemberResponse memberResponse = createMemberResponse();
//
//        // Mock the service responses
//        when(memberService.getMemberById(anyLong())).thenReturn(memberResponse);
//        when(librarianService.getLibrarianById(anyLong())).thenReturn(librarianResponse);
//        when(adminService.getAdminById(anyLong())).thenReturn(adminResponse);
//
//        // Mock the repository save method
//        Fine mockFine = new Fine();
//        when(fineRepository.save(any(Fine.class))).thenReturn(mockFine);
//
//        // Create a sample FineRequest
//        fineRequest = new FineRequest();
//        fineRequest.setAmount(10.50);
//        fineRequest.setIssueDate(LocalDate.now());
//        fineRequest.setDueDate(LocalDate.now()); // 1 day later
//        fineRequest.setIsPaid(false);
//        fineRequest.setMemberId(1L);
//        fineRequest.setLibrarianId(1L);
//        fineRequest.setAdminId(1L);
//    }
//
//    @Test
//    void testApplyFine() {
//        // Call the method under test
//        FineResponse response = fineService.applyFine(fineRequest);
//
//        // Verify interactions and assert results
//        verify(memberService).getMemberById(fineRequest.getMemberId());
//        verify(librarianService).getLibrarianById(fineRequest.getLibrarianId());
//        verify(adminService).getAdminById(fineRequest.getAdminId());
//        verify(fineRepository).save(any(Fine.class));
//
//        // Assert that the response is not null
//        assertNotNull(response);
//        // Add more assertions based on the expected response
//    }
//
//    private LibrarianResponse createLibrarianResponse() {
//        AdminResponse adminResponse = createAdminResponse();
//
//        LibrarianResponse librarianResponse = new LibrarianResponse();
//        librarianResponse.setId(1L);
//        librarianResponse.setName("Librarian Name");
//        librarianResponse.setEmail("librarian@example.com");
//        librarianResponse.setAddress(new AddressDTO("456 Librarian Ave", "Librarian City", "Librarian State", "country", "67890"));
//        librarianResponse.setContact("987-654-3210");
//        librarianResponse.setEmployeeCode("LIB123");
//        librarianResponse.setAdmin(adminResponse);
//
//        return librarianResponse;
//    }
//
//    private AdminResponse createAdminResponse() {
//        AdminResponse adminResponse = new AdminResponse();
//        adminResponse.setId(1L);
//        adminResponse.setName("Admin Name");
//        adminResponse.setEmail("admin@example.com");
//        adminResponse.setAddress(new AddressDTO("123 Admin St", "Admin City", "Admin State", "country", "12345"));
//        adminResponse.setContact("123-456-7890");
//        adminResponse.setAdminCode("ADMIN123");
//        adminResponse.setRole(Role.SYSTEM_ADMIN);
//
//        return adminResponse;
//    }
//
//    private MemberResponse createMemberResponse() {
//        AdminResponse adminResponse = createAdminResponse();
//        LibrarianResponse librarianResponse = createLibrarianResponse();
//
//        MemberResponse memberResponse = new MemberResponse();
//        memberResponse.setId(1L);
//        memberResponse.setName("Member Name");
//        memberResponse.setEmail("member@example.com");
//        memberResponse.setAddress(new AddressDTO("789 Member St", "Member City", "Member State", "country", "54321"));
//        memberResponse.setContact("321-654-0987");
//        memberResponse.setMembershipStatus(MembershipStatus.ACTIVE); // Set an appropriate status
//        memberResponse.setLibrarianResponse(new LibrarianResponse());
//        memberResponse.setAdminResponse(adminResponse);
//
//        return memberResponse;
//    }
//}
