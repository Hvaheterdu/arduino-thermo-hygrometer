package api.arduinothermohygrometer.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import api.arduinothermohygrometer.dtos.BatteryDto;

public interface BatteryService {
    Optional<BatteryDto> getBatteryDtoById(UUID id);

    Optional<BatteryDto> getBatteryDtoByTimestamp(LocalDateTime timestamp);

    List<BatteryDto> getBatteryDtosByDate(LocalDateTime localDateTime);

    void createBatteryDto(BatteryDto batteryDto);

    void deleteBatteryById(UUID id);

    void deleteBatteryByTimestamp(LocalDateTime timestamp);

}
