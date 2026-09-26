package api.arduinothermohygrometer.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.api.TemperatureApi;
import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.mapper.TemperatureDtoMapper;
import api.arduinothermohygrometer.model.Temperature;
import api.arduinothermohygrometer.service.TemperatureService;

@RestController
public class TemperatureController implements TemperatureApi {
  private final TemperatureService temperatureService;
  private final TemperatureDtoMapper temperatureDtoMapper;

  public TemperatureController(
      final TemperatureService temperatureService,
      final TemperatureDtoMapper temperatureDtoMapper) {
    this.temperatureService = temperatureService;
    this.temperatureDtoMapper = temperatureDtoMapper;
  }

  @Override
  public ResponseEntity<List<TemperatureDto>> getTemperaturesByDateOrTimestamp(
      final LocalDateTime registeredAt, final boolean dateOnly) {
    List<Temperature> temperatures =
        temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);
    List<TemperatureDto> temperatureDtos =
        temperatures.stream().map(temperatureDtoMapper::toDto).toList();
    return ResponseEntity.ok(temperatureDtos);
  }

  @Override
  public ResponseEntity<TemperatureDto> createTemperature(final TemperatureDto temperatureDto) {
    Temperature temperature = temperatureDtoMapper.toModel(temperatureDto);
    Temperature createdTemperature = temperatureService.createTemperature(temperature);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(temperatureDtoMapper.toDto(createdTemperature));
  }

  @Override
  public ResponseEntity<Void> deleteTemperaturesByDateOrTimestamp(
      final LocalDateTime registeredAt, final boolean dateOnly) {
    temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
