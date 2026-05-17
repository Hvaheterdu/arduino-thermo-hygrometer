package api.arduinothermohygrometer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;

public interface HumidityService {
    HumidityDto getHumidityById(final UUID id) throws ResourceNotFoundException;

    List<HumidityDto> getHumiditiesByDateOrTimestamp(final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException;

    HumidityDto createHumidity(final HumidityDto humidityDto) throws ResourceNotCreatedException;

    void deleteHumidityById(final UUID id) throws ResourceNotFoundException;

    void deleteHumiditiesByDateOrTimestamp(final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException;
}
