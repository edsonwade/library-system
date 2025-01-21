package code.with.vanilson.libraryapplication.unit.member;

import code.with.vanilson.libraryapplication.member.*;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
@WithMockUser(username = "admin", roles = "ADMIN")
class MemberMapperTest {

    @Mock
    private Admin admin;

    @Mock
    private Librarian librarian;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for successfully mapping a member entity to a MemberResponse DTO.
     */
    @Test
    @DisplayName("Test mapping member entity to MemberResponse - Success")
    void testMapToMemberResponse_Success() {
        // Given
        Address address = createSampleAddress();
        Admin admin1 = createSampleAdmin(address);
        Librarian librarian1 = createSampleLibrarian(address, admin1);
        Member member = createSampleMember(address, librarian1, admin1);

        // When
        MemberResponse response = MemberMapper.mapToMemberResponse(member);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(response.getLibrarianResponse()).isNotNull();
        assertThat(response.getAdminResponse()).isNotNull();
    }

    /**
     * Test case for mapping a null member entity, expecting ResourceBadRequestException.
     */
    @Test
    @DisplayName("Test mapping null member entity throws ResourceBadRequestException")
    void testMapToMemberResponse_ThrowsResourceBadRequestException_WhenMemberIsNull() {
        // Given
        Member member = null;

        // When & Then
        assertThrows(ResourceBadRequestException.class, () -> MemberMapper.mapToMemberResponse(member));
    }

    /**
     * Test case for mapping a member entity with a null Librarian, expecting ResourceBadRequestException.
     */
    @Test
    @DisplayName("Test mapping member with null Librarian throws ResourceBadRequestException")
    void testMapToMemberResponse_ThrowsResourceBadRequestException_WhenLibrarianIsNull() {
        // Given
        Member member = new Member();
        member.setLibrarian(null);
        member.setAdmin(admin);

        // When & Then
        ResourceBadRequestException exception = assertThrows(ResourceBadRequestException.class, () -> MemberMapper.mapToMemberResponse(member));

        assertThat(exception.getMessage()).isEqualTo("library.member.association_must_exists");
    }

    /**
     * Test case for mapping a member entity with a null Admin, expecting ResourceBadRequestException.
     */
    @Test
    @DisplayName("Test mapping member with null Admin throws ResourceBadRequestException")
    void testMapToMemberResponse_ThrowsResourceBadRequestException_WhenAdminIsNull() {
        // Given
        Member member = new Member();
        member.setLibrarian(librarian);
        member.setAdmin(null);

        // When & Then
        ResourceBadRequestException exception = assertThrows(ResourceBadRequestException.class, () -> MemberMapper.mapToMemberResponse(member));

        assertThat(exception.getMessage()).isEqualTo("library.admin.association_must_exists");
    }

    /**
     * Test case for successfully mapping a MemberRequest DTO to a member entity.
     */
    @Test
    @DisplayName("Test mapping MemberRequest DTO to member entity - Success")
    void testMapToMemberEntity_Success() {
        // Given
        MemberRequest memberRequest = createSampleMemberRequest();

        // When
        Member member = MemberMapper.mapToMemberEntity(memberRequest, admin, librarian);

        // Then
        assertThat(member).isNotNull();
        assertThat(member.getName()).isEqualTo("John Doe");
        assertThat(member.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(member.getLibrarian()).isEqualTo(librarian);
        assertThat(member.getAdmin()).isEqualTo(admin);
    }

    /**
     * Test case for mapping a null MemberRequest DTO, expecting ResourceBadRequestException.
     */
    @Test
    @DisplayName("Test mapping null MemberRequest DTO throws ResourceBadRequestException")
    void testMapToMemberEntity_ThrowsResourceBadRequestException_WhenRequestIsNull() {
        // Given
        MemberRequest memberRequest = null;

        // When & Then
        assertThrows(ResourceBadRequestException.class, () -> MemberMapper.mapToMemberEntity(memberRequest, admin, librarian));
    }

    /**
     * Test case for mapping a MemberRequest DTO with a null Librarian ID, expecting ResourceBadRequestException.
     */
    @Test
    @DisplayName("Test mapping MemberRequest with null Librarian ID throws ResourceBadRequestException")
    void testMapToMemberEntity_ThrowsResourceBadRequestException_WhenLibrarianIdIsNull() {
        // Given
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setLibrarianId(null);
        memberRequest.setAdminId(2L);

        // When & Then
        assertThrows(ResourceBadRequestException.class, () -> MemberMapper.mapToMemberEntity(memberRequest, admin, librarian));
    }

    /**
     * Test case for mapping a MemberRequest DTO with a null Admin ID, expecting ResourceBadRequestException.
     */
    @Test
    @DisplayName("Test mapping MemberRequest with null Admin ID throws ResourceBadRequestException")
    void testMapToMemberEntity_ThrowsResourceBadRequestException_WhenAdminIdIsNull() {
        // Given
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setLibrarianId(1L);
        memberRequest.setAdminId(null);

        // When & Then
        assertThrows(ResourceBadRequestException.class, () -> MemberMapper.mapToMemberEntity(memberRequest, admin, librarian));
    }

    // Helper methods to avoid repetition in tests
    private Address createSampleAddress() {
        Address address = new Address();
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
        Member member = new Member();
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
        MemberRequest memberRequest = new MemberRequest();
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

