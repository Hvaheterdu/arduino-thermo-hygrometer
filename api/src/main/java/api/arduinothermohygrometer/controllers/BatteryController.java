package api.arduinothermohygrometer.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.api.BatteryApi;
import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.services.BatteryService;

@RestController
public class BatteryController implements BatteryApi {
    private final BatteryService batteryService;

    public BatteryController(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    @Override
    public ResponseEntity<BatteryDto> getBatteryById(UUID id) {
        BatteryDto batteryDto = batteryService.getBatteryById(id);

        return new ResponseEntity<>(batteryDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BatteryDto> getBatteryByTimestamp(LocalDateTime timestamp) {
        BatteryDto batteryDto = batteryService.getBatteryByTimestamp(timestamp);

        return new ResponseEntity<>(batteryDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BatteryDto>> getBatteriesByDate(LocalDate date) {
        List<BatteryDto> batteries = batteryService.getBatteriesByDate(date);

        return new ResponseEntity<>(batteries, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BatteryDto> createBattery(final BatteryDto batteryDto) {
        BatteryDto createdBatteryDto = batteryService.createBattery(batteryDto);

        return new ResponseEntity<>(createdBatteryDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteBatteryDtoById(UUID id) {
        batteryService.deleteBatteryById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteBatteryByTimestamp(final LocalDateTime timestamp) {
        batteryService.deleteBatteryByTimestamp(timestamp);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
