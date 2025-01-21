package code.with.vanilson.libraryapplication.e2e.admin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * AdminE2ETest
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-01-10
 */
@WithMockUser(username = "admin", roles = "ADMIN")
class AdminE2ETest {

    // todo: Set the path to the chromedriver executable ,need to install
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        // Set the path to the chromedriver executable
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080");
    }

    @Test
    @Disabled("This test is disabled because it requires a valid admin code")
    void testAdminCreation() {
        // Navigate to the admin creation page
        driver.findElement(By.id("createAdminButton")).click();

        // Fill in the admin details
        driver.findElement(By.id("name")).sendKeys("Jane Doe");
        driver.findElement(By.id("email")).sendKeys("jane.doe@example.com");
        driver.findElement(By.id("contact")).sendKeys("+123 456-789-125");
        driver.findElement(By.id("adminCode")).sendKeys("ADMIN12345");
        driver.findElement(By.id("role")).sendKeys("SUPER_ADMIN");

        // Submit the form
        driver.findElement(By.id("submit")).click();

        // Verify the success message
        WebElement successMessage = driver.findElement(By.id("successMessage"));
        assertEquals("Admin created successfully", successMessage.getText());
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}