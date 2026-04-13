package api.arduinothermohygrometer.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;

public interface TemperatureService {
    TemperatureDto getTemperatureById(UUID id) throws ResourceNotFoundException;

    TemperatureDto getTemperatureByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;

    List<TemperatureDto> getTemperaturesByDate(LocalDate date);

    TemperatureDto createTemperature(TemperatureDto temperatureDto) throws ResourceNotCreatedException;

    void deleteTemperatureById(UUID id) throws ResourceNotFoundException;

    void deleteTemperatureByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;
}
