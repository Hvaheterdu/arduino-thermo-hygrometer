package api.arduinothermohygrometer.repository.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import api.arduinothermohygrometer.model.Humidity;
import api.arduinothermohygrometer.repository.HumidityRepository;

@Repository
public class HumidityRepositoryImpl implements HumidityRepository {
    private final JdbcClient jdbcClient;

    public HumidityRepositoryImpl(final JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Humidity> getHumidityById(final UUID id) {
        String sql = """
                SELECT *
                FROM humidities
                WHERE id = :id
                """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(Humidity.class)
                .optional();
    }

    @Override
    public List<Humidity> getHumidityByTimestamp(final LocalDateTime timestamp) {
        String sql = """
                SELECT *
                FROM humidities
                WHERE registered_at = :timestamp
                """;

        return jdbcClient.sql(sql)
                .param("timestamp", timestamp)
                .query(Humidity.class)
                .list();
    }

    @Override
    public List<Humidity> getHumiditiesByDate(final LocalDate date) {
        String sql = """
                SELECT *
                FROM humidities
                WHERE registered_at >= :start AND registered_at < :end
                """;

        return jdbcClient.sql(sql)
                .param("start", date.atStartOfDay())
                .param("end", date.plusDays(1).atStartOfDay())
                .query(Humidity.class)
                .list();
    }

    @Override
    public Optional<Humidity> createHumidity(final Humidity humidity) {
        String sql = """
                INSERT INTO humidities (registered_at, air_humidity)
                VALUES (:registered_at, :air_humidity)
                RETURNING id, registered_at, air_humidity
                """;

        return jdbcClient.sql(sql)
                .param("registered_at", humidity.getRegisteredAt())
                .param("air_humidity", humidity.getAirHumidity())
                .query(Humidity.class)
                .optional();
    }

    @Override
    public void deleteHumidityById(final UUID id) {
        String sql = """
                DELETE FROM humidities
                WHERE id = :id
                """;

        jdbcClient.sql(sql)
                .param("id", id)
                .update();
    }

    @Override
    public void deleteHumidityByTimestamp(final LocalDateTime timestamp) {
        String sql = """
                DELETE FROM humidities
                WHERE registered_at = :timestamp
                """;

        jdbcClient.sql(sql)
                .param("timestamp", timestamp)
                .update();
    }

    @Override
    public void deleteHumiditiesByDate(final LocalDate date) {
        String sql = """
                DELETE FROM batteries
                WHERE registered_at >= :start AND registered_at < :end
                """;

        jdbcClient.sql(sql)
                .param("start", date.atStartOfDay())
                .param("end", date.plusDays(1).atStartOfDay())
                .update();
    }
}
