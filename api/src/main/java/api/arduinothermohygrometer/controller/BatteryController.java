package api.arduinothermohygrometer.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.api.BatteryApi;
import api.arduinothermohygrometer.dto.BatteryDto;
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
    public ResponseEntity<List<BatteryDto>> getBatteriesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) {
        List<BatteryDto> batteryDtos = batteryService.getBatteriesByDateOrTimestamp(dateTime, checkOnlyDate);
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
    public ResponseEntity<Void> deleteBatteriesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) {
        batteryService.deleteBatteriesByDateOrTimestamp(dateTime, checkOnlyDate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
