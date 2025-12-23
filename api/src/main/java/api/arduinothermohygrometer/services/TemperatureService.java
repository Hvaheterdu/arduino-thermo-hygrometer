package api.arduinothermohygrometer.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.dtos.TemperatureDto;
import api.arduinothermohygrometer.exceptions.ResourceMappingFailedException;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;

public interface TemperatureService {
    TemperatureDto getTemperatureDtoById(UUID id) throws ResourceNotFoundException;

    TemperatureDto getTemperatureDtoByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;

    List<TemperatureDto> getTemperatureDtosByDate(LocalDate date);

    TemperatureDto createTemperatureDto(TemperatureDto temperatureDto) throws ResourceNotCreatedException, ResourceMappingFailedException;

    void deleteTemperatureById(UUID id) throws ResourceNotFoundException;

    void deleteTemperatureByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;
}
