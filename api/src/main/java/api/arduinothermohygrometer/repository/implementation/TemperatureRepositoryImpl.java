package api.arduinothermohygrometer.repository.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import api.arduinothermohygrometer.model.Temperature;
import api.arduinothermohygrometer.repository.TemperatureRepository;

@Repository
public class TemperatureRepositoryImpl implements TemperatureRepository {
    private final JdbcClient jdbcClient;

    public TemperatureRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Temperature> getTemperatureById(final UUID id) {
        String sql = """
            SELECT *
            FROM temperatures
            WHERE id = :id
            """;

        return jdbcClient.sql(sql)
                         .param("id", id)
                         .query(Temperature.class)
                         .optional();
    }

    @Override
    public List<Temperature> getTemperatureByTimestamp(final LocalDateTime timestamp) {
        String sql = """
            SELECT *
            FROM temperatures
            WHERE registered_at = :timestamp
            """;

        return jdbcClient.sql(sql)
                         .param("timestamp", timestamp)
                         .query(Temperature.class)
                         .list();
    }

    @Override
    public List<Temperature> getTemperaturesByDate(final LocalDate date) {
        String sql = """
            SELECT *
            FROM temperatures
            WHERE registered_at >= :start AND registered_at < :end
            """;

        return jdbcClient.sql(sql)
                         .param("start", date.atStartOfDay())
                         .param("end", date.plusDays(1).atStartOfDay())
                         .query(Temperature.class)
                         .list();
    }

    @Override
    public void createTemperature(final Temperature temperature) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
            INSERT INTO temperatures (id, registered_at, temp)
            VALUES (:id, :registered_at, :temp)
            """;

        jdbcClient.sql(sql)
                  .param("id", temperature.getId())
                  .param("registered_at", temperature.getRegisteredAt())
                  .param("temp", temperature.getTemp())
                  .update(keyHolder, "id");
    }

    @Override
    public void deleteTemperatureById(final UUID id) {
        String sql = """
            DELETE FROM temperatures
            WHERE id = :id
            """;

        jdbcClient.sql(sql)
                  .param("id", id)
                  .update();
    }

    @Override
    public void deleteTemperatureByTimestamp(final LocalDateTime timestamp) {
        String sql = """
            DELETE FROM temperatures
            WHERE registered_at = :timestamp
            """;

        jdbcClient.sql(sql)
                  .param("timestamp", timestamp)
                  .update();
    }

    @Override
    public void deleteTemperaturesByDate(final LocalDate date) {
        String sql = """
            SELECT *
            FROM batteries
            WHERE registered_at >= :start AND registered_at < :end
            """;

        jdbcClient.sql(sql)
                  .param("start", date.atStartOfDay())
                  .param("end", date.plusDays(1).atStartOfDay())
                  .update();
    }
}
