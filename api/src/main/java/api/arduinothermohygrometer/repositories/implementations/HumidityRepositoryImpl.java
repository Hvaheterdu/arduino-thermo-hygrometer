package api.arduinothermohygrometer.repositories.implementations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import api.arduinothermohygrometer.entities.Humidity;
import api.arduinothermohygrometer.repositories.HumidityRepository;

@Repository
public class HumidityRepositoryImpl implements HumidityRepository {
    private final JdbcClient jdbcClient;

    public HumidityRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Humidity> getHumidityById(UUID id) {
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
    public Optional<Humidity> getHumidityByTimestamp(LocalDateTime timestamp) {
        String sql = """
            SELECT *
            FROM humidities
            WHERE registered_at = :timestamp
            """;

        return jdbcClient.sql(sql)
                         .param("timestamp", timestamp)
                         .query(Humidity.class)
                         .optional();
    }

    @Override
    public List<Humidity> getHumiditiesByDate(LocalDateTime localDateTime) {
        String sql = """
            SELECT *
            FROM humidities
            WHERE registered_at >= :start AND registered_at < :end
            """;

        LocalDate localDate = localDateTime.toLocalDate();
        return jdbcClient.sql(sql)
                         .param("start", localDate.atStartOfDay())
                         .param("end", localDate.plusDays(1)
                                                .atStartOfDay())
                         .query(Humidity.class)
                         .list();
    }

    @Override
    public void createHumidity(Humidity humidity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
            INSERT INTO humidities (id, registered_at, air_humidity)
            VALUES (:id, :registered_at, :air_humidity)
            """;

        jdbcClient.sql(sql)
                  .param("id", humidity.getId())
                  .param("registered_at", humidity.getRegisteredAt())
                  .update(keyHolder, "id");
    }

    @Override
    public void deleteHumidityById(UUID id) {
        String sql = """
            DELETE FROM humidities
            WHERE id = :id
            """;

        jdbcClient.sql(sql)
                  .param("id", id)
                  .update();
    }

    @Override
    public void deleteHumidityByTimestamp(LocalDateTime timestamp) {
        String sql = """
            DELETE FROM humidities
            WHERE registered_at = :timestamp
            """;

        jdbcClient.sql(sql)
                  .param("timestamp", timestamp)
                  .update();
    }
}
