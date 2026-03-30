package api.arduinothermohygrometer.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import api.arduinothermohygrometer.dtos.BatteryDto;

public interface BatteryController {
    ResponseEntity<BatteryDto> getBatteryById(UUID id);

    ResponseEntity<BatteryDto> getBatteryByTimestamp(LocalDateTime timestamp);

    ResponseEntity<List<BatteryDto>> getBatteriesByDate(LocalDate date);

    ResponseEntity<BatteryDto> create(final BatteryDto batteryDto);

    ResponseEntity<Void> deleteBatteryDtoById(UUID id);

    ResponseEntity<Void> deleteBatteryByTimestamp(LocalDateTime timestamp);
}
