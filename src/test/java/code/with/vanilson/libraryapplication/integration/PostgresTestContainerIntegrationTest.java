package code.with.vanilson.libraryapplication.integration;

import code.with.vanilson.libraryapplication.config.TestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("container-test")
@ContextConfiguration(classes = TestContainerConfig.class)
class PostgresTestContainerIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PostgreSQLContainer<?> postgreSQLContainer;

    @Test
    void testPostgresContainerIsRunning() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void testDataSourceIsConfigured() throws Exception {
        assertThat(dataSource.getConnection().isValid(1)).isTrue();
    }
}