package api.arduinothermohygrometer.bases;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.postgresql.PostgreSQLContainer;

public abstract class TestcontainerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestcontainerBase.class);

    @ServiceConnection
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:18.3")
        .withDatabaseName("postgres-test")
        .withUsername("postgres-test")
        .withPassword("postgres-test")
        .withStartupTimeout(Duration.ofSeconds(120));

    static {
        runPostgreSQLContainer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (postgreSQLContainer != null) {
                LOGGER.info("JVM is shutting down! Stopping PostgreSQL-testcontainer...");
                postgreSQLContainer.stop();
            }
        }));
    }

    private static synchronized void runPostgreSQLContainer() {
        LocalDateTime start = LocalDateTime.now();

        LOGGER.info("Starting PostgreSQL-testcontainer...");

        startPostgreSQLContainer();
        migrateFlyway();

        LOGGER.info("PostgreSQL-testcontainer is ready after {} seconds", Duration.between(start, LocalDateTime.now()).toSeconds());
    }

    private static void startPostgreSQLContainer() {
        postgreSQLContainer.start();
        LOGGER.info("PostgreSQL-testcontainer info: {}", postgreSQLContainer.getCurrentContainerInfo());
    }

    private static void migrateFlyway() {
        Flyway.configure()
              .dataSource(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword())
              .encoding(StandardCharsets.ISO_8859_1)
              .locations("classpath:db/migrations", "classpath:db/undo_migrations")
              .load()
              .migrate();
    }
}
