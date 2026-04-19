package api.arduinothermohygrometer.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.api.BatteryApi;
import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.dto.DateTimeRequest;
import api.arduinothermohygrometer.service.BatteryService;

@RestController
public class BatteryController implements BatteryApi {
    private final BatteryService batteryService;

    public BatteryController(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    @Override
    public ResponseEntity<BatteryDto> getBatteryById(final UUID id) {
        BatteryDto batteryDto = batteryService.getBatteryById(id);
        return new ResponseEntity<>(batteryDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BatteryDto>> getBatteriesByDateOrTimestamp(final DateTimeRequest dateTimeRequest) {
        List<BatteryDto> batteryDtos = batteryService.getBatteriesByDateOrTimestamp(dateTimeRequest);
        return new ResponseEntity<>(batteryDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BatteryDto> createBattery(final BatteryDto batteryDto) {
        BatteryDto createdBatteryDto = batteryService.createBattery(batteryDto);
        return new ResponseEntity<>(createdBatteryDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteBatteryDtoById(final UUID id) {
        batteryService.deleteBatteryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteBatteriesByDateOrTimestamp(final DateTimeRequest dateTimeRequest) {
        batteryService.deleteBatteriesByDateOrTimestamp(dateTimeRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
