package api.arduinothermohygrometer.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;

public interface HumidityService {
    HumidityDto getHumidityDtoById(UUID id) throws ResourceNotFoundException;

    HumidityDto getHumidityDtoByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;

    List<HumidityDto> getHumidityDtosByDate(LocalDate date);

    HumidityDto createHumidityDto(HumidityDto humidityDto) throws ResourceNotCreatedException;

    void deleteHumidityById(UUID id) throws ResourceNotFoundException;

    void deleteHumidityByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;
}
