package code.with.vanilson.libraryapplication.unit.admin;

import code.with.vanilson.libraryapplication.TestDataHelper;
import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.admin.AdminRepository;
import code.with.vanilson.libraryapplication.admin.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class AdminRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AdminRepository adminRepository;

    TestDataHelper testDataHelper = new TestDataHelper();
    private Admin admins;

    @BeforeEach
    void setup() {
        admins = new Admin();
        admins.setId(1L);
        admins.setName("John Doe");
        admins.setEmail("johndoe@example.com");
        admins.setAddress(testDataHelper.createAddress());
        admins.setContact("+531 990 900 000");
        admins.setAdminCode("ADM-001");
        admins.setRole(Role.SYSTEM_ADMIN);
    }

    // Test case for saving an admin entity
    @DisplayName("Save Admin - Should Store Admin Details When Valid Admin Details Provided")
    @Test
    void testAdmin_WhenValidAdminDetailsProvided_ShouldReturnStoreAdminDetails() {
        // Given

        admins = entityManager.merge(admins);
        entityManager.flush();

        // When
        Admin savedAdmin = entityManager.find(Admin.class, admins.getId());

        // Then
        assertThat(savedAdmin).isNotNull();
        assertThat(savedAdmin.getId()).isEqualTo(admins.getId());
        assertThat(savedAdmin.getName()).isEqualTo(admins.getName());
        assertThat(savedAdmin.getEmail()).isEqualTo(admins.getEmail());
        assertThat(savedAdmin.getContact()).isEqualTo(admins.getContact());
        assertThat(savedAdmin.getAddress()).isEqualTo(admins.getAddress());
        assertThat(savedAdmin.getAdminCode()).isEqualTo(admins.getAdminCode());
    }

    @DisplayName("Find Admin By Email - Should Return Admin When Email Exists")
    @Test
    void testFindAdminByEmail_WhenEmailExists_ShouldReturnAdmin() {
        // Given
        admins = entityManager.merge(admins);
        entityManager.persistAndFlush(admins);

        // When
        Optional<Admin> foundAdmin = Optional.ofNullable(entityManager.find(Admin.class, admins.getId()));

        // Then
        assertThat(foundAdmin).isPresent();
        assertThat(foundAdmin.get().getEmail()).isEqualTo(admins.getEmail());

    }

    @DisplayName("Find Admin By Email - Should Return Empty When Email Does Not Exist")
    @Test
    void testFindAdminByEmail_WhenEmailDoesNotExist_ShouldReturnEmpty() {
        // When
        Optional<Admin> foundAdmin = Optional.ofNullable(entityManager.find(Admin.class, -12L));

        // Then
        assertThat(foundAdmin).isNotPresent();
    }

    @DisplayName("Exists Admin By Email - Should Return False When Email Does Not Exist")
    @Test
    void testExistsAdminByEmail_WhenEmailDoesNotExist_ShouldReturnFalse() {
        // Given
        String nonExistentEmail = "nonexistent@example.com";

        // When
        boolean exists = adminRepository.existsAdminByEmail(nonExistentEmail);

        // Then
        assertThat(exists).isFalse();
    }

    @DisplayName("Exists Admin By Email - Should Return False When Email Does Not Exist")
    @Test
    void testExistsAdminByEmail_WhenEmailIsProvided_ShouldReturnTrue() {
        // Given
        entityManager.merge(admins);

        // When
        boolean exists = adminRepository.existsAdminByEmail(admins.getEmail());

        // Then
        assertThat(exists).isTrue();
    }

    @DisplayName("Find Admin By Email - Should Return Admin When Email Exists")
    @Test
    void testFindAdminByEmail_WhenEmailIsProvided_ShouldReturnAdmin() {
        // Given
        entityManager.merge(admins);

        // When
        Optional<Admin> exists = adminRepository.findAdminByEmail(admins.getEmail());

        // Then
        assertThat(exists).isPresent();
        assertEquals(exists.get().getEmail(), admins.getEmail());
    }
}
