package api.arduinothermohygrometer.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.api.HumidityApi;
import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.services.HumidityService;

@RestController
public class HumidityController implements HumidityApi {
    private final HumidityService humidityService;

    public HumidityController(HumidityService humidityService) {
        this.humidityService = humidityService;
    }

    @Override
    public ResponseEntity<HumidityDto> getHumidityById(UUID id) {
        HumidityDto humidityDto = humidityService.getHumidityById(id);

        return new ResponseEntity<>(humidityDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HumidityDto> getHumidityByTimestamp(LocalDateTime timestamp) {
        HumidityDto humidityDto = humidityService.getHumidityByTimestamp(timestamp);

        return new ResponseEntity<>(humidityDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<HumidityDto>> getHumiditiesByDate(LocalDate date) {
        List<HumidityDto> humidities = humidityService.getHumiditiesByDate(date);

        return new ResponseEntity<>(humidities, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HumidityDto> createHumidity(HumidityDto humidityDto) {
        HumidityDto createdHumidityDto = humidityService.createHumidity(humidityDto);

        return new ResponseEntity<>(createdHumidityDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteHumidityDtoById(UUID id) {
        humidityService.deleteHumidityById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteHumidityByTimestamp(LocalDateTime timestamp) {
        humidityService.deleteHumidityByTimestamp(timestamp);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
