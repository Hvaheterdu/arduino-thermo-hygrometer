package api.arduinothermohygrometer.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.api.HumidityApi;
import api.arduinothermohygrometer.dto.DateTimeRequest;
import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.service.HumidityService;

@RestController
public class HumidityController implements HumidityApi {
    private final HumidityService humidityService;

    public HumidityController(HumidityService humidityService) {
        this.humidityService = humidityService;
    }

    @Override
    public ResponseEntity<HumidityDto> getHumidityById(final UUID id) {
        HumidityDto humidityDto = humidityService.getHumidityById(id);
        return new ResponseEntity<>(humidityDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<HumidityDto>> getHumiditiesByDateOrTimestamp(final DateTimeRequest dateTimeRequest) {
        List<HumidityDto> humidityDtos = humidityService.getHumiditiesByDateOrTimestamp(dateTimeRequest);
        return new ResponseEntity<>(humidityDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HumidityDto> createHumidity(final HumidityDto humidityDto) {
        HumidityDto createdHumidityDto = humidityService.createHumidity(humidityDto);
        return new ResponseEntity<>(createdHumidityDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteHumidityDtoById(final UUID id) {
        humidityService.deleteHumidityById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteHumiditiesByDateOrTimestamp(final DateTimeRequest dateTimeRequest) {
        humidityService.deleteHumiditiesByDateOrTimestamp(dateTimeRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
