package api.arduinothermohygrometer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;

public interface HumidityService {
    HumidityDto getHumidityById(UUID id) throws ResourceNotFoundException;

    List<HumidityDto> getHumiditiesByDateOrTimestamp(LocalDateTime registeredAt, boolean dateOnly) throws ResourceNotFoundException;

    HumidityDto createHumidity(HumidityDto humidityDto) throws ResourceNotCreatedException;

    void deleteHumidityById(UUID id) throws ResourceNotFoundException;

    void deleteHumiditiesByDateOrTimestamp(LocalDateTime registeredAt, boolean dateOnly) throws ResourceNotFoundException;
}
