package api.arduinothermohygrometer.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.api.HumidityApi;
import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.service.HumidityService;

@RestController
public class HumidityController implements HumidityApi {
    private final HumidityService humidityService;

    public HumidityController(final HumidityService humidityService) {
        this.humidityService = humidityService;
    }

    @Override
    public ResponseEntity<HumidityDto> createHumidity(final HumidityDto humidityDto) {
        HumidityDto createdHumidityDto = humidityService.createHumidity(humidityDto);
        return new ResponseEntity<>(createdHumidityDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteHumiditiesByDateOrTimestamp(final LocalDateTime registeredAt, final boolean dateOnly) {
        humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteHumidityById(final UUID id) {
        humidityService.deleteHumidityById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<HumidityDto>> getHumiditiesByDateOrTimestamp(final LocalDateTime registeredAt, final boolean dateOnly) {
        List<HumidityDto> humidityDtos = humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);
        return new ResponseEntity<>(humidityDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HumidityDto> getHumidityById(final UUID id) {
        HumidityDto humidityDto = humidityService.getHumidityById(id);
        return new ResponseEntity<>(humidityDto, HttpStatus.OK);
    }
}
