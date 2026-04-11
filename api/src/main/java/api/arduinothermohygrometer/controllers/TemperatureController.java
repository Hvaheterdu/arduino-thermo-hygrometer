package api.arduinothermohygrometer.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.api.TemperatureApi;
import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.services.TemperatureService;

@RestController
public class TemperatureController implements TemperatureApi {
    private final TemperatureService temperatureService;

    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @Override
    public ResponseEntity<TemperatureDto> getTemperatureById(UUID id) {
        TemperatureDto temperatureDto = temperatureService.getTemperatureById(id);

        return new ResponseEntity<>(temperatureDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TemperatureDto> getTemperatureByTimestamp(LocalDateTime timestamp) {
        TemperatureDto temperatureDto = temperatureService.getTemperatureByTimestamp(timestamp);

        return new ResponseEntity<>(temperatureDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TemperatureDto>> getTemperaturesByDate(LocalDate date) {
        List<TemperatureDto> temperatures = temperatureService.getTemperaturesByDate(date);

        return new ResponseEntity<>(temperatures, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TemperatureDto> createTemperature(TemperatureDto temperatureDto) {
        TemperatureDto createdTemperatureDto = temperatureService.createTemperature(temperatureDto);

        return new ResponseEntity<>(createdTemperatureDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteTemperatureDtoById(UUID id) {
        temperatureService.deleteTemperatureById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteTemperatureByTimestamp(LocalDateTime timestamp) {
        temperatureService.deleteTemperatureByTimestamp(timestamp);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
