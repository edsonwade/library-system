package code.with.vanilson.libraryapplication.unit.member;

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminRepository;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceConflictException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.librarian.LibrarianRepository;
import code.with.vanilson.libraryapplication.member.*;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
@SuppressWarnings("all")
@DisplayName("Member Service Test")
@WithMockUser(username = "admin", roles = "ADMIN")
class MemberServiceTest {

    public static final String EMAIL = "john.doe@example.com";
    private static final Long MEMBER_ID = 1L;
    private static final Long INVALID_ID = 0L;
    public static final String JOHN_DOE = "John Doe";

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LibrarianRepository librarianRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private MemberService memberService;

    private static MemberRequest memberRequest;
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

    /**
     * Tests the successful retrieval of all members from the repository.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Should return all members when repository is queried")
    void shouldReturnAllMembers_WhenRepositoryIsQueried() {
        // Given
        when(memberRepository.findAll()).thenReturn(List.of(member));
        // When
        List<MemberResponse> members = memberService.getAllMembers();
        // Then
        assertThat(members).hasSize(1);
        assertThat(members.get(0).getName()).isEqualTo(JOHN_DOE);
        assertEquals(members.get(0).getName(), JOHN_DOE);
        verify(memberRepository, times(1)).findAll();
    }

    /**
     * Tests the successful retrieval of a member by its ID.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Should return a member when found by ID")
    void shouldReturnMember_WhenFoundById() {
        // Given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        // When
        MemberResponse response = memberService.getMemberById(MEMBER_ID);
        // Then
        assertThat(response.getName()).isEqualTo(JOHN_DOE);
        verify(memberRepository, times(1)).findById(MEMBER_ID);
    }

    /**
     * Tests the scenario where a member is requested with an invalid ID (0).
     * This should throw a ResourceBadRequestException.
     *
     * @throws Exception if an error occurs during the request.
     */

    @Test
    @DisplayName("Should throw ResourceBadRequestException when invalid ID is provided")
    void shouldThrowBadRequestException_WhenInvalidIdIsProvided() {
        // Act & Assert
        assertThrows(ResourceBadRequestException.class, () -> memberService.getMemberById(INVALID_ID));
    }

    /**
     * Tests the scenario where a member is requested with a non-existing ID.
     * This should throw a ResourceNotFoundException.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Should throw ResourceNotFoundException when member is not found by ID")
    void shouldThrowNotFoundException_WhenMemberNotFoundById() {
        // Arrange
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> memberService.getMemberById(MEMBER_ID));

        // Assert exception details (optional, but helps clarify the failure)
        assertThat(exception.getMessage()).contains("No member found with ID 1. Please verify the ID and try again.");
        verify(memberRepository, times(1)).findById(MEMBER_ID);
    }

    /**
     * Tests the successful retrieval of a member by their email address.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Should return a member when found by email")
    void shouldReturnMember_WhenFoundByEmail() {
        // Arrange
        when(memberRepository.findMemberByEmail(EMAIL)).thenReturn(Optional.of(member));

        // Act
        MemberResponse response = memberService.getMemberByEmail(EMAIL);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(EMAIL);
        verify(memberRepository, times(1)).findMemberByEmail(EMAIL);
    }

    /**
     * Tests the scenario when a member is not found by their email address.
     * This should throw a ResourceNotFoundException when the member is not found.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Should throw ResourceNotFoundException when member is not found by email")
    void shouldThrowResourceNotFoundException_WhenEmailNotFound() {
        // Arrange: Mock the repository to return an empty Optional when searching by email
        when(memberRepository.findMemberByEmail(EMAIL)).thenReturn(Optional.empty());

        // Act & Assert: Try to retrieve a member by email and expect ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class,
                () -> memberService.getMemberByEmail(EMAIL));
    }

    /**
     * Tests the scenario when a member with the given email already exists.
     * This should throw a ResourceConflictException to indicate a conflict.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Should throw ResourceConflictException when email already exists")
    @Disabled("This test is disabled because the method is not implemented yet.")
    void shouldThrowResourceConflictException_WhenEmailAlreadyExists() {
        // Arrange: Mock the repository to return an existing member when searching by email
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(librarian));
        when(memberRepository.findMemberByEmail(EMAIL)).thenReturn(Optional.of(member));

        // Act & Assert: Try to create a new member with an already existing email and expect ResourceConflictException
        var memberRequests = new MemberRequest();
        memberRequests.setName(JOHN_DOE);
        memberRequests.setEmail(EMAIL);  // Set the email that already exists
        memberRequests.setContact("+123 456-789-123");
        memberRequests.setAddress(auxiliarMethodToAddressDTO());
        memberRequests.setMembershipStatus(MembershipStatus.ACTIVE);
        memberRequests.setLibrarianId(1L);
        memberRequests.setAdminId(2L);

        assertThrows(ResourceConflictException.class,
                () -> memberService.createMember(memberRequests));
    }

    /**
     * Tests the scenario where a member is requested by email, but no member is found.
     * This should throw a ResourceNotFoundException.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Should throw ResourceNotFoundException when member is not found by email")
    void shouldThrowNotFoundException_WhenMemberNotFoundByEmail() {
        // Arrange
        when(memberRepository.findMemberByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> memberService.getMemberByEmail("notfound@example.com"));
    }

    /**
     * Tests the successful creation of a new member.
     * The test ensures that when the request is valid, the member is saved and returned successfully.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Should create a new member when request is valid")
    void shouldCreateNewMember_WhenRequestIsValid() {
        // Arrange
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(librarian));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        // Act
        MemberResponse response = memberService.createMember(memberRequest);

        // Assert
        assertThat(response.getName()).isEqualTo(JOHN_DOE);
        assertTrue(response.getId() > 0);
        assertNotEquals(response.getId(), 0);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    /**
     * Tests the scenario where a null request is passed to create a member.
     * This should throw a ResourceBadRequestException.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Should throw ResourceBadRequestException when request is null")
    void shouldThrowBadRequestException_WhenRequestIsNull() {
        assertThrows(ResourceBadRequestException.class, () -> memberService.createMember(null));
    }

    /**
     * Tests the scenario where the admin is not found in the database during member creation.
     * This should throw a ResourceNotFoundException.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Should throw ResourceNotFoundException when admin is not found")
    void shouldThrowNotFoundException_WhenAdminNotFound() {
        // Arrange
        when(adminRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> memberService.createMember(memberRequest));
    }

    /**
     * Tests the scenario where the librarian is not found in the database during member creation.
     * This should throw a ResourceNotFoundException.
     *
     * @throws Exception if an error occurs during the request.
     */
    @Test
    @DisplayName("Should throw ResourceNotFoundException when librarian is not found")
    void shouldThrowNotFoundException_WhenLibrarianNotFound() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.createMember(memberRequest));
    }

    /**
     * Tests the successful update of a member when the request is valid.
     * It ensures that the member is fetched, the update is applied, and the member is saved back to the repository.
     */
    @Test
    @DisplayName("Should update the member successfully when the request is valid")
    void shouldUpdateMemberSuccessfully_WhenRequestIsValid() {
        // Arrange
        mockCommonMemberRepositoryCalls();

        // Act
        MemberResponse response = memberService.updateMember(memberRequest, MEMBER_ID);

        // Assert
        assertThat(response.getName()).isEqualTo(JOHN_DOE);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    /**
     * Tests the scenario where a null request is passed for updating a member.
     * This should throw a ResourceBadRequestException.
     */
    @Test
    @DisplayName("Should throw ResourceBadRequestException when the request is null")
    void should_ThrowBadRequestException_WhenRequestIsNull() {
        assertThrows(ResourceBadRequestException.class, () -> memberService.updateMember(null, MEMBER_ID));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when the member is not found for update")
    /**
     * Tests the scenario where a member is not found for updating.
     * This should throw a ResourceNotFoundException.
     */
    void shouldThrowNotFoundException_WhenMemberNotFoundForUpdate() {
        // Arrange
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> memberService.updateMember(memberRequest, MEMBER_ID));
    }

    /**
     * Tests the successful deletion of a member by ID.
     * It ensures the deletion process is called and the member is deleted from the repository.
     */
    @Test
    @DisplayName("Should delete the member successfully by ID")
    void shouldDeleteMemberSuccessfully_WhenIdIsValid() {
        // Arrange
        when(memberRepository.existsById(MEMBER_ID)).thenReturn(true);

        // Act
        memberService.deleteMemberById(MEMBER_ID);

        // Assert
        verify(memberRepository, times(1)).deleteById(MEMBER_ID);
    }

    /**
     * Tests the scenario where the provided ID is invalid (e.g., 0).
     * This should throw a ResourceBadRequestException.
     */
    @Test
    @DisplayName("Should throw ResourceBadRequestException when the ID is invalid for deletion")
    void shouldThrowBadRequestException_WhenIdIsInvalidForDeletion() {
        // Act & Assert
        assertThrows(ResourceBadRequestException.class,
                () -> memberService.deleteMemberById(INVALID_ID));
    }

    /**
     * Tests the scenario where the member does not exist for deletion.
     * This should throw a ResourceNotFoundException.
     */
    @Test
    @DisplayName("Should throw ResourceNotFoundException when member is not found by ID for deletion")
    void shouldThrowNotFoundException_WhenMemberNotFoundByIdForDeletion() {
        // Arrange
        when(memberRepository.existsById(MEMBER_ID)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> memberService.deleteMemberById(MEMBER_ID));
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

    // Helper methods to avoid repetition in tests
    private Admin createSampleAdmin(Address address) {
        Admin admin1 = new Admin();
        admin1.setId(MEMBER_ID);
        admin1.setName(JOHN_DOE);
        admin1.setEmail("john.doe@example.com");
        admin1.setContact("+123 456-789-123");
        admin1.setAddress(address);
        admin1.setAdminCode("ABC123");
        return admin1;
    }

    private Librarian createSampleLibrarian(Address address, Admin admin1) {
        Librarian librarian1 = new Librarian();
        librarian1.setId(MEMBER_ID);
        librarian1.setName(JOHN_DOE);
        librarian1.setEmail("john.doe@example.com");
        librarian1.setContact("+123 456-789-123");
        librarian1.setAddress(address);
        librarian1.setEmployeeCode("ABC123");
        librarian1.setAdmin(admin1);
        return librarian1;
    }

    private Member createSampleMember(Address address, Librarian librarian1, Admin admin1) {
        member = new Member();
        member.setId(MEMBER_ID);
        member.setName(JOHN_DOE);
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
        memberRequest.setName(JOHN_DOE);
        memberRequest.setEmail("john.doe@example.com");
        memberRequest.setContact("+123 456-789-123");
        memberRequest.setAddress(new AddressDTO());
        memberRequest.setMembershipStatus(MembershipStatus.ACTIVE);
        memberRequest.setLibrarianId(1L);
        memberRequest.setAdminId(2L);
        return memberRequest;
    }

    // Reusable method to mock common member-related repositories
    private void mockCommonMemberRepositoryCalls() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(librarian));
        when(memberRepository.save(any(Member.class))).thenReturn(member);
    }

    /**
     * Creates and returns an AddressDTO object with predefined values.
     *
     * @return an AddressDTO object with street, city, state, country, and postal code information
     */
    protected AddressDTO auxiliarMethodToAddressDTO() {
        var addressDTO1 = new AddressDTO();
        addressDTO1.setStreet("123 Main Street");
        addressDTO1.setCity("New York");
        addressDTO1.setCountry("United States");
        addressDTO1.setState("NY");
        addressDTO1.setPostalCode("10001");
        return addressDTO1;
    }
}
