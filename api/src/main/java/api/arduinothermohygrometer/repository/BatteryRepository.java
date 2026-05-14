package api.arduinothermohygrometer.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.arduinothermohygrometer.model.Battery;

public interface BatteryRepository {
    Optional<Battery> getBatteryById(final UUID id);

    List<Battery> getBatteryByTimestamp(final LocalDateTime timestamp);

    List<Battery> getBatteriesByDate(final LocalDate date);

    Optional<Battery> createBattery(final Battery battery);

    void deleteBatteryById(final UUID id);

    void deleteBatteryByTimestamp(final LocalDateTime timestamp);

    void deleteBatteriesByDate(final LocalDate date);
}
