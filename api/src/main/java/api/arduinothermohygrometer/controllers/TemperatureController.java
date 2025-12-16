package api.arduinothermohygrometer.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import api.arduinothermohygrometer.dtos.TemperatureDto;
import api.arduinothermohygrometer.exceptions.ResourceMappingFailedException;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

public interface TemperatureController {
    ResponseEntity<TemperatureDto> getTemperatureById(@PathVariable UUID id) throws ResourceNotFoundException;

    ResponseEntity<TemperatureDto> getTemperatureByTimestamp(@PathVariable LocalDateTime timestamp)
            throws ResourceNotFoundException;

    ResponseEntity<List<TemperatureDto>> getTemperaturessByDate(@PathVariable LocalDateTime date);

    ResponseEntity<TemperatureDto> create(@Valid @RequestBody final TemperatureDto temperatureDto)
            throws ResourceNotCreatedException, ResourceMappingFailedException;

    ResponseEntity<Void> deleteTemperatureDtoById(@PathVariable UUID id) throws ResourceNotFoundException;

    ResponseEntity<Void> deleteTemperatureByTimestamp(@PathVariable LocalDateTime timestamp)
            throws ResourceNotFoundException;
}
