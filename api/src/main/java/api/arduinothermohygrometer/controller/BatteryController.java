package api.arduinothermohygrometer.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.api.BatteryApi;
import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.mapper.BatteryDtoMapper;
import api.arduinothermohygrometer.model.Battery;
import api.arduinothermohygrometer.service.BatteryService;

@RestController
public class BatteryController implements BatteryApi {
  private final BatteryService batteryService;
  private final BatteryDtoMapper batteryDtoMapper;

  public BatteryController(
      final BatteryService batteryService, final BatteryDtoMapper batteryDtoMapper) {
    this.batteryService = batteryService;
    this.batteryDtoMapper = batteryDtoMapper;
  }

  @Override
  public ResponseEntity<List<BatteryDto>> getBatteriesByDateOrTimestamp(
      final LocalDateTime registeredAt, final boolean dateOnly) {
    List<Battery> batteries = batteryService.getBatteriesByDateOrTimestamp(registeredAt, dateOnly);
    List<BatteryDto> batteryDtos = batteries.stream().map(batteryDtoMapper::toDto).toList();
    return ResponseEntity.ok(batteryDtos);
  }

  @Override
  public ResponseEntity<BatteryDto> createBattery(final BatteryDto batteryDto) {
    Battery battery = batteryDtoMapper.toModel(batteryDto);
    Battery createdBattery = batteryService.createBattery(battery);
    return ResponseEntity.status(HttpStatus.CREATED).body(batteryDtoMapper.toDto(createdBattery));
  }

  @Override
  public ResponseEntity<Void> deleteBatteriesByDateOrTimestamp(
      final LocalDateTime registeredAt, final boolean dateOnly) {
    batteryService.deleteBatteriesByDateOrTimestamp(registeredAt, dateOnly);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
