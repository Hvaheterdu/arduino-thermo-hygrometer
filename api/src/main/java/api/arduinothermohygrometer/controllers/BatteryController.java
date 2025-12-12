package api.arduinothermohygrometer.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import api.arduinothermohygrometer.dtos.BatteryDto;

public interface BatteryController {
    ResponseEntity<BatteryDto> getBatteryById(@PathVariable UUID id);

    ResponseEntity<BatteryDto> getBatteryByTimestamp(@PathVariable LocalDateTime timestamp);
}
