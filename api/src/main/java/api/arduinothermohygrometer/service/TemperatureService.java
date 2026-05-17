package api.arduinothermohygrometer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;

public interface TemperatureService {
    TemperatureDto getTemperatureById(final UUID id) throws ResourceNotFoundException;

    List<TemperatureDto> getTemperaturesByDateOrTimestamp(final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException;

    TemperatureDto createTemperature(final TemperatureDto temperatureDto) throws ResourceNotCreatedException;

    void deleteTemperatureById(final UUID id) throws ResourceNotFoundException;

    void deleteTemperaturesByDateOrTimestamp(final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException;
}
