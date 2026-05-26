package api.arduinothermohygrometer.manager;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import org.flywaydb.core.Flyway;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.postgresql.PostgreSQLContainer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TestcontainerManager {
    @ServiceConnection
    @SuppressWarnings("resource")
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:18.3")
            .withDatabaseName("postgres-test")
            .withUsername("postgres-test")
            .withPassword("postgres-test")
            .withStartupTimeout(Duration.ofSeconds(120));

    static {
        runPostgreSQLContainer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (postgreSQLContainer != null) {
                log.info("JVM is shutting down! Stopping PostgreSQL-testcontainer...");
                stopAndClosePostgreSQLContainer();
            }
        }));
    }

    private static synchronized void runPostgreSQLContainer() {
        final LocalDateTime start = LocalDateTime.now();

        log.info("Starting PostgreSQL-testcontainer...");

        startPostgreSQLContainer();
        migrateFlyway();

        log.info("PostgreSQL-testcontainer is ready after {} seconds.", Duration.between(start, LocalDateTime.now()).toSeconds());
    }

    private static void startPostgreSQLContainer() {
        postgreSQLContainer.start();
        log.info("PostgreSQL-testcontainer info: {}", postgreSQLContainer.getCurrentContainerInfo());
    }

    private static void migrateFlyway() {
        Flyway.configure()
                .dataSource(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword())
                .encoding(StandardCharsets.ISO_8859_1)
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }

    private static void stopAndClosePostgreSQLContainer() {
        postgreSQLContainer.stop();
        postgreSQLContainer.close();
        log.info("PostgreSQL-testcontainer stopped and closed successfully.");
    }
}
