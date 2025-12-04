package api.arduinothermohygrometer.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import api.arduinothermohygrometer.dtos.TemperatureDto;

public interface TemperatureService {
    Optional<TemperatureDto> getTemperatureDtoById(UUID id);

    Optional<TemperatureDto> getTemperatureDtoByTimestamp(LocalDateTime timestamp);

    List<TemperatureDto> getTemperatureDtosByDate(LocalDateTime localDateTime);

    Optional<TemperatureDto> createTemperatureDto(TemperatureDto temperatureDto);

    Optional<TemperatureDto> deleteBatteryById(UUID id);

    Optional<TemperatureDto> deleteBatteryByTimestamp(LocalDateTime timestamp);
}
