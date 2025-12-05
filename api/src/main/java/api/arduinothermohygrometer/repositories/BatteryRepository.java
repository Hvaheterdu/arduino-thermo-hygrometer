package api.arduinothermohygrometer.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.arduinothermohygrometer.entities.Battery;

public interface BatteryRepository {
    Optional<Battery> getBatteryById(UUID id);

    Optional<Battery> getBatteryByTimestamp(LocalDateTime timestamp);

    List<Battery> getBatteriesByDate(LocalDateTime localDateTime);

    void createBattery(Battery battery);

    void deleteBatteryById(UUID id);

    void deleteBatteryByTimestamp(LocalDateTime timestamp);
}
