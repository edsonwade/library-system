package code.with.vanilson.libraryapplication.member.runner;

/**
 * CucumberTest
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-01-07
 */

import code.with.vanilson.libraryapplication.LibraryManagementSystemApplication;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = LibraryManagementSystemApplication.class, properties = {"spring.profiles.active=test"})
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features/member", "src/test/resources/features/admin"},
        glue = "code.with.vanilson.libraryapplication.member.steps",
        plugin = {"pretty", "html:target/cucumber-reports.html", "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml"},
        tags = "not @ignore"
)
public class CucumberTest {
}