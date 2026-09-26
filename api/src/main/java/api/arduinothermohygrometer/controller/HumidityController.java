package api.arduinothermohygrometer.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.api.HumidityApi;
import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.mapper.HumidityDtoMapper;
import api.arduinothermohygrometer.model.Humidity;
import api.arduinothermohygrometer.service.HumidityService;

@RestController
public class HumidityController implements HumidityApi {
  private final HumidityService humidityService;
  private final HumidityDtoMapper humidityDtoMapper;

  public HumidityController(
      final HumidityService humidityService, final HumidityDtoMapper humidityDtoMapper) {
    this.humidityService = humidityService;
    this.humidityDtoMapper = humidityDtoMapper;
  }

  @Override
  public ResponseEntity<List<HumidityDto>> getHumiditiesByDateOrTimestamp(
      final LocalDateTime registeredAt, final boolean dateOnly) {
    List<Humidity> humidities =
        humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);
    List<HumidityDto> humidityDtos = humidities.stream().map(humidityDtoMapper::toDto).toList();
    return ResponseEntity.ok(humidityDtos);
  }

  @Override
  public ResponseEntity<HumidityDto> createHumidity(final HumidityDto humidityDto) {
    Humidity humidity = humidityDtoMapper.toModel(humidityDto);
    Humidity createdHumidity = humidityService.createHumidity(humidity);
    return ResponseEntity.status(HttpStatus.CREATED).body(humidityDtoMapper.toDto(createdHumidity));
  }

  @Override
  public ResponseEntity<Void> deleteHumiditiesByDateOrTimestamp(
      final LocalDateTime registeredAt, final boolean dateOnly) {
    humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
