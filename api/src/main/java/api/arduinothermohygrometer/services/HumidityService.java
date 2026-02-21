package api.arduinothermohygrometer.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;

public interface HumidityService {
    HumidityDto getHumidityById(UUID id) throws ResourceNotFoundException;

    HumidityDto getHumidityByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;

    List<HumidityDto> getHumiditiesByDate(LocalDate date);

    HumidityDto createHumidity(HumidityDto humidityDto) throws ResourceNotCreatedException;

    void deleteHumidityById(UUID id) throws ResourceNotFoundException;

    void deleteHumidityByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;
}
