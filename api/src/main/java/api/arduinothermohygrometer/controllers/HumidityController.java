package api.arduinothermohygrometer.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.exceptions.ResourceMappingFailedException;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;

public interface HumidityController {
    ResponseEntity<HumidityDto> getHumidityById(UUID id) throws ResourceNotFoundException;

    ResponseEntity<HumidityDto> getHumidityByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;

    ResponseEntity<List<HumidityDto>> getHumiditiesByDate(LocalDate date);

    ResponseEntity<HumidityDto> create(final HumidityDto humidityDto) throws ResourceNotCreatedException, ResourceMappingFailedException;

    ResponseEntity<Void> deleteHumidityDtoById(UUID id) throws ResourceNotFoundException;

    ResponseEntity<Void> deleteHumidityByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;
}
