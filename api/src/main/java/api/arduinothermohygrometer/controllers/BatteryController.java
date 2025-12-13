package api.arduinothermohygrometer.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.exceptions.ResourceMappingFailedException;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

public interface BatteryController {
    ResponseEntity<BatteryDto> getBatteryById(@PathVariable UUID id) throws ResourceNotFoundException;

    ResponseEntity<BatteryDto> getBatteryByTimestamp(@PathVariable LocalDateTime timestamp)
            throws ResourceNotFoundException;

    ResponseEntity<List<BatteryDto>> getBatteriesByDate(@PathVariable LocalDateTime date);

    ResponseEntity<BatteryDto> create(@Valid @RequestBody BatteryDto batteryDto)
            throws ResourceNotCreatedException, ResourceMappingFailedException;

    ResponseEntity<Void> deleteBatteryDtoById(@PathVariable UUID id) throws ResourceNotFoundException;

    ResponseEntity<Void> deleteBatteryByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;
}
