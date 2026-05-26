package api.arduinothermohygrometer.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.arduinothermohygrometer.model.Battery;

public interface BatteryRepository {
    Optional<Battery> getBatteryById(UUID id);

    List<Battery> getBatteryByTimestamp(LocalDateTime timestamp);

    List<Battery> getBatteriesByDate(LocalDate date);

    Optional<Battery> createBattery(Battery battery);

    void deleteBatteryById(UUID id);

    void deleteBatteryByTimestamp(LocalDateTime timestamp);

    void deleteBatteriesByDate(LocalDate date);
}
