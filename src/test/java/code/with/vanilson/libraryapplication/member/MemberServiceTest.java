package code.with.vanilson.libraryapplication.member;


import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminRepository;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianRepository;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * MemberServiceTest
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-09-10
 */

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LibrarianRepository librarianRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private MemberService memberService;

    private MemberRequest memberRequest;
    private Member member;
    private Admin admin;
    private Address address;
    private Librarian librarian;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        memberRequest = createSampleMemberRequest();
        address = createSampleAddress();
        admin = createSampleAdmin(address);
        librarian = createSampleLibrarian(address, admin);
        member = createSampleMember(address, librarian, admin);
    }

    @Test
    void testGetAllMembers_Success() {
        when(memberRepository.findAll()).thenReturn(List.of(member));

        List<MemberResponse> members = memberService.getAllMembers();

        assertThat(members).hasSize(1);
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void testGetMemberById_Success() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        MemberResponse response = memberService.getMemberById(1L);

        assertThat(response.getName()).isEqualTo("John Doe");
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMemberById_InvalidId() {
        assertThrows(ResourceBadRequestException.class, () -> memberService.getMemberById(0L));
    }

    @Test
    void testGetMemberById_NotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.getMemberById(1L));
    }

    @Test
    void testGetMemberByEmail_Success() {
        when(memberRepository.findMemberByEmail(anyString())).thenReturn(Optional.of(member));

        MemberResponse response = memberService.getMemberByEmail("john.doe@example.com");

        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
        verify(memberRepository, times(1)).findMemberByEmail("john.doe@example.com");
    }

    @Test
    void testGetMemberByEmail_NotFound() {
        when(memberRepository.findMemberByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.getMemberByEmail("notfound@example.com"));
    }

    @Test
    void testCreateMember_Success() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(librarian));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberResponse response = memberService.createMember(memberRequest);

        assertThat(response.getName()).isEqualTo("John Doe");
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testCreateMember_NullRequest() {
        assertThrows(ResourceBadRequestException.class, () -> memberService.createMember(null));
    }

    @Test
    void testCreateMember_AdminNotFound() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.createMember(memberRequest));
    }

    @Test
    void testCreateMember_LibrarianNotFound() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.createMember(memberRequest));
    }

    void testCreateMember_DuplicateEmailOrContact() {
        // todo : create test here
    }

    @Test
    void testUpdateMember_Success() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(librarian));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberResponse response = memberService.updateMember(memberRequest, 1L);

        assertThat(response.getName()).isEqualTo("John Doe");
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testUpdateMember_NullRequest() {
        assertThrows(ResourceBadRequestException.class, () -> memberService.updateMember(null, 1L));
    }

    @Test
    void testUpdateMember_NotFound() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.updateMember(memberRequest, 1L));
    }

    @Test
    void testDeleteMemberById_Success() {
        when(memberRepository.existsById(anyLong())).thenReturn(true);

        memberService.deleteMemberById(1L);

        verify(memberRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteMemberById_InvalidId() {
        assertThrows(ResourceBadRequestException.class, () -> memberService.deleteMemberById(0L));
    }

    @Test
    void testDeleteMemberById_NotFound() {
        when(memberRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> memberService.deleteMemberById(1L));
    }

    // Helper methods to avoid repetition in tests
    private Address createSampleAddress() {
        address = new Address();
        address.setStreet("123 Main St");
        address.setState("NY");
        address.setCity("Anytown");
        address.setCountry("USA");
        address.setPostalCode("12345");
        return address;
    }

    private Admin createSampleAdmin(Address address) {
        Admin admin1 = new Admin();
        admin1.setId(1L);
        admin1.setName("John Doe");
        admin1.setEmail("john.doe@example.com");
        admin1.setContact("+123 456-789-123");
        admin1.setAddress(address);
        admin1.setAdminCode("ABC123");
        return admin1;
    }

    private Librarian createSampleLibrarian(Address address, Admin admin1) {
        Librarian librarian1 = new Librarian();
        librarian1.setId(1L);
        librarian1.setName("John Doe");
        librarian1.setEmail("john.doe@example.com");
        librarian1.setContact("+123 456-789-123");
        librarian1.setAddress(address);
        librarian1.setEmployeeCode("ABC123");
        librarian1.setAdmin(admin1);
        return librarian1;
    }

    private Member createSampleMember(Address address, Librarian librarian1, Admin admin1) {
        member = new Member();
        member.setId(1L);
        member.setName("John Doe");
        member.setEmail("john.doe@example.com");
        member.setContact("+123 456-789-123");
        member.setAddress(address);
        member.setMembershipStatus(MembershipStatus.ACTIVE);
        member.setLibrarian(librarian1);
        member.setAdmin(admin1);
        return member;
    }

    private MemberRequest createSampleMemberRequest() {
        memberRequest = new MemberRequest();
        memberRequest.setName("John Doe");
        memberRequest.setEmail("john.doe@example.com");
        memberRequest.setContact("+123 456-789-123");
        memberRequest.setAddress(new AddressDTO());
        memberRequest.setMembershipStatus(MembershipStatus.ACTIVE);
        memberRequest.setLibrarianId(1L);
        memberRequest.setAdminId(2L);
        return memberRequest;
    }
}
