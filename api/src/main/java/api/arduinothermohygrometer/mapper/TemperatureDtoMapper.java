package api.arduinothermohygrometer.mapper;

import org.springframework.stereotype.Component;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.model.Temperature;

@Component
public class TemperatureDtoMapper {
  public Temperature toModel(final TemperatureDto temperatureDto) {
    return new Temperature(temperatureDto.getRegisteredAt(), temperatureDto.getTemp());
  }

  public TemperatureDto toDto(final Temperature temperature) {
    return new TemperatureDto(temperature.getRegisteredAt(), temperature.getTemp());
  }
}
