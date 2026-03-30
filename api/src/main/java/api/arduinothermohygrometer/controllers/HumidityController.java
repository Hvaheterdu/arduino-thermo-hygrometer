package api.arduinothermohygrometer.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import api.arduinothermohygrometer.dtos.HumidityDto;

public interface HumidityController {
    ResponseEntity<HumidityDto> getHumidityById(UUID id);

    ResponseEntity<HumidityDto> getHumidityByTimestamp(LocalDateTime timestamp);

    ResponseEntity<List<HumidityDto>> getHumiditiesByDate(LocalDate date);

    ResponseEntity<HumidityDto> create(final HumidityDto humidityDto);

    ResponseEntity<Void> deleteHumidityDtoById(UUID id);

    ResponseEntity<Void> deleteHumidityByTimestamp(LocalDateTime timestamp);
}
