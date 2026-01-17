package api.arduinothermohygrometer.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import api.arduinothermohygrometer.dtos.TemperatureDto;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;

public interface TemperatureController {
    ResponseEntity<TemperatureDto> getTemperatureById(UUID id) throws ResourceNotFoundException;

    ResponseEntity<TemperatureDto> getTemperatureByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;

    ResponseEntity<List<TemperatureDto>> getTemperaturesByDate(LocalDate date);

    ResponseEntity<TemperatureDto> create(final TemperatureDto temperatureDto) throws ResourceNotCreatedException;

    ResponseEntity<Void> deleteTemperatureDtoById(UUID id) throws ResourceNotFoundException;

    ResponseEntity<Void> deleteTemperatureByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;
}
