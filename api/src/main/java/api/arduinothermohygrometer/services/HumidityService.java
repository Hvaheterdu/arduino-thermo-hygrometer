package api.arduinothermohygrometer.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.arduinothermohygrometer.dtos.HumidityDto;

public interface HumidityService {
    Optional<HumidityDto> getHumidityDtoById(UUID id);

    Optional<HumidityDto> getHumidityDtoByTimestamp(LocalDateTime timestamp);

    List<HumidityDto> getHumidityDtosByDate(LocalDateTime localDateTime);

    Optional<HumidityDto> createHumidityDto(HumidityDto humidityDto);

    void deleteHumidityById(UUID id);

    void deleteHumidityByTimestamp(LocalDateTime timestamp);
}
