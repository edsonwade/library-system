package code.with.vanilson.libraryapplication.bdd.runner;


import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * CucumberTest
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-01-07
 */

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")  // Adjust this package name as necessary
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "code.with.vanilson.libraryapplication.bdd.steps")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @ignore")
public class CucumberTest {
}