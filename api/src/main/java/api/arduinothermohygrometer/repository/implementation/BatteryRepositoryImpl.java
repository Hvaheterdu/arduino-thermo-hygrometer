package api.arduinothermohygrometer.repository.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import api.arduinothermohygrometer.model.Battery;
import api.arduinothermohygrometer.repository.BatteryRepository;

@Repository
public class BatteryRepositoryImpl implements BatteryRepository {
    private final JdbcClient jdbcClient;

    public BatteryRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Battery> getBatteryById(final UUID id) {
        String sql = """
            SELECT *
            FROM batteries
            WHERE id = :id
            """;

        return jdbcClient.sql(sql)
                         .param("id", id)
                         .query(Battery.class)
                         .optional();
    }

    @Override
    public List<Battery> getBatteryByTimestamp(final LocalDateTime timestamp) {
        String sql = """
            SELECT *
            FROM batteries
            WHERE registered_at = :timestamp
            """;

        return jdbcClient.sql(sql)
                         .param("timestamp", timestamp)
                         .query(Battery.class)
                         .list();
    }

    @Override
    public List<Battery> getBatteriesByDate(final LocalDate date) {
        String sql = """
            SELECT *
            FROM batteries
            WHERE registered_at >= :start AND registered_at < :end
            """;

        return jdbcClient.sql(sql)
                         .param("start", date.atStartOfDay())
                         .param("end", date.plusDays(1).atStartOfDay())
                         .query(Battery.class)
                         .list();
    }

    @Override
    public Optional<Battery> createBattery(final Battery battery) {
        String sql = """
            INSERT INTO batteries (registered_at, battery_status)
            VALUES (:registered_at, :battery_status)
            RETURNING id, registered_at, battery_status
            """;

        return jdbcClient.sql(sql)
                         .param("registered_at", battery.getRegisteredAt())
                         .param("battery_status", battery.getBatteryStatus())
                         .query(Battery.class)
                         .optional();
    }

    @Override
    public void deleteBatteryById(final UUID id) {
        String sql = """
            DELETE FROM batteries
            WHERE id = :id
            """;

        jdbcClient.sql(sql)
                  .param("id", id)
                  .update();
    }

    @Override
    public void deleteBatteryByTimestamp(final LocalDateTime timestamp) {
        String sql = """
            DELETE FROM batteries
            WHERE registered_at = :timestamp
            """;

        jdbcClient.sql(sql)
                  .param("timestamp", timestamp)
                  .update();
    }

    @Override
    public void deleteBatteriesByDate(final LocalDate date) {
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
