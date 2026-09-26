package api.arduinothermohygrometer.mapper;

import org.springframework.stereotype.Component;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.model.Humidity;

@Component
public class HumidityDtoMapper {
  public Humidity toModel(final HumidityDto humidityDto) {
    return new Humidity(humidityDto.getRegisteredAt(), humidityDto.getAirHumidity());
  }

  public HumidityDto toDto(final Humidity humidity) {
    return new HumidityDto(humidity.getRegisteredAt(), humidity.getAirHumidity());
  }
}
