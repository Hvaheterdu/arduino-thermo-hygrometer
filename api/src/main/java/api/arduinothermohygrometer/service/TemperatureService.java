package api.arduinothermohygrometer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;

public interface TemperatureService {
    TemperatureDto getTemperatureById(UUID id) throws ResourceNotFoundException;

    List<TemperatureDto> getTemperaturesByDateOrTimestamp(LocalDateTime registeredAt, boolean dateOnly) throws ResourceNotFoundException;

    TemperatureDto createTemperature(TemperatureDto temperatureDto) throws ResourceNotCreatedException;

    void deleteTemperatureById(UUID id) throws ResourceNotFoundException;

    void deleteTemperaturesByDateOrTimestamp(LocalDateTime registeredAt, boolean dateOnly) throws ResourceNotFoundException;
}
