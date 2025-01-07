package code.with.vanilson.libraryapplication.member.steps;

import code.with.vanilson.libraryapplication.LibraryManagementSystemApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * CucumberSpringConfiguration
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-01-07
 */
@CucumberContextConfiguration
@SpringBootTest(classes = LibraryManagementSystemApplication.class, properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
@Transactional
public class CucumberSpringConfiguration {

    @Autowired
    private WebApplicationContext webApplicationContext;
}
