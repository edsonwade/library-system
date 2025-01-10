package code.with.vanilson.libraryapplication.bdd.steps;

import code.with.vanilson.libraryapplication.LibraryManagementSystemApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.context.WebApplicationContext;

/**
 * CucumberSpringConfiguration
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-01-07
 */
@CucumberContextConfiguration
@SpringBootTest(classes = LibraryManagementSystemApplication.class)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SuppressWarnings("all")
public class CucumberSpringConfiguration {
    @Autowired
    private WebApplicationContext webApplicationContext;
}