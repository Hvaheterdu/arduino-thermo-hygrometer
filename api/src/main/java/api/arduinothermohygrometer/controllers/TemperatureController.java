package api.arduinothermohygrometer.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import api.arduinothermohygrometer.dtos.TemperatureDto;

public interface TemperatureController {
    ResponseEntity<TemperatureDto> getTemperatureById(UUID id);

    ResponseEntity<TemperatureDto> getTemperatureByTimestamp(LocalDateTime timestamp);

    ResponseEntity<List<TemperatureDto>> getTemperaturesByDate(LocalDate date);

    ResponseEntity<TemperatureDto> create(final TemperatureDto temperatureDto);

    ResponseEntity<Void> deleteTemperatureDtoById(UUID id);

    ResponseEntity<Void> deleteTemperatureByTimestamp(LocalDateTime timestamp);
}
