package api.arduinothermohygrometer.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.exceptions.ResourceMappingFailedException;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;

public interface BatteryController {
    ResponseEntity<BatteryDto> getBatteryById(UUID id) throws ResourceNotFoundException;

    ResponseEntity<BatteryDto> getBatteryByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;

    ResponseEntity<List<BatteryDto>> getBatteriesByDate(LocalDateTime date);

    ResponseEntity<BatteryDto> create(final BatteryDto batteryDto) throws ResourceNotCreatedException, ResourceMappingFailedException;

    ResponseEntity<Void> deleteBatteryDtoById(UUID id) throws ResourceNotFoundException;

    ResponseEntity<Void> deleteBatteryByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;
}
