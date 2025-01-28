package code.with.vanilson.libraryapplication.config;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
@SuppressWarnings("all")
public class TestContainerConfig {

    private PostgreSQLContainer<?> container;

    @Bean
    public PostgreSQLContainer<?> postgreSQLContainer() {
        container = new PostgreSQLContainer<>("postgres:15.2")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        container.start();
        return container;
    }

    @PreDestroy
    public void stopContainer() {
        if (container != null) {
            container.stop();
        }
    }
}