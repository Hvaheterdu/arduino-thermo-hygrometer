package api.arduinothermohygrometer.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.api.TemperatureApi;
import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.service.TemperatureService;

@RestController
public class TemperatureController implements TemperatureApi {
    private final TemperatureService temperatureService;

    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @Override
    public ResponseEntity<TemperatureDto> getTemperatureById(final UUID id) {
        TemperatureDto temperatureDto = temperatureService.getTemperatureById(id);
        return new ResponseEntity<>(temperatureDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<TemperatureDto>> getTemperaturesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) {
        List<TemperatureDto> temperatureDtos = temperatureService.getTemperaturesByDateOrTimestamp(dateTime, checkOnlyDate);
        return new ResponseEntity<>(temperatureDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TemperatureDto> createTemperature(final TemperatureDto temperatureDto) {
        TemperatureDto createdTemperatureDto = temperatureService.createTemperature(temperatureDto);
        return new ResponseEntity<>(createdTemperatureDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteTemperatureDtoById(final UUID id) {
        temperatureService.deleteTemperatureById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteTemperaturesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) {
        temperatureService.deleteTemperaturesByDateOrTimestamp(dateTime, checkOnlyDate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
