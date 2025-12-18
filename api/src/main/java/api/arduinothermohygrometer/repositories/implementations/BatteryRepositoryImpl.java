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

import api.arduinothermohygrometer.entities.Battery;
import api.arduinothermohygrometer.repositories.BatteryRepository;

@Repository
public class BatteryRepositoryImpl implements BatteryRepository {
    private final JdbcClient jdbcClient;

    public BatteryRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<Battery> getBatteryById(UUID id) {
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
    public Optional<Battery> getBatteryByTimestamp(LocalDateTime timestamp) {
        String sql = """
            SELECT *
            FROM batteries
            WHERE registered_at = :timestamp
            """;

        return jdbcClient.sql(sql)
                         .param("timestamp", timestamp)
                         .query(Battery.class)
                         .optional();
    }

    @Override
    public List<Battery> getBatteriesByDate(LocalDateTime localDateTime) {
        String sql = """
            SELECT *
            FROM batteries
            WHERE registered_at >= :start AND registered_at < :end
            """;

        LocalDate localDate = localDateTime.toLocalDate();
        return jdbcClient.sql(sql)
                         .param("start", localDate.atStartOfDay())
                         .param("end", localDate.plusDays(1)
                                                .atStartOfDay())
                         .query(Battery.class)
                         .list();
    }

    @Override
    public void createBattery(Battery battery) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
            INSERT INTO batteries (id, registered_at, battery_status)
            VALUES (:id, :registered_at, :battery_status)
            """;

        jdbcClient.sql(sql)
                  .param("id", battery.getId())
                  .param("registered_at", battery.getRegisteredAt())
                  .param("battery_status", battery.getBatteryStatus())
                  .update(keyHolder, "id");
    }

    @Override
    public void deleteBatteryById(UUID id) {
        String sql = """
            DELETE FROM batteries
            WHERE id = :id
            """;

        jdbcClient.sql(sql)
                  .param("id", id)
                  .update();
    }

    @Override
    public void deleteBatteryByTimestamp(LocalDateTime timestamp) {
        String sql = """
            DELETE FROM batteries
            WHERE registered_at = :timestamp
            """;

        jdbcClient.sql(sql)
                  .param("timestamp", timestamp)
                  .update();
    }
}
