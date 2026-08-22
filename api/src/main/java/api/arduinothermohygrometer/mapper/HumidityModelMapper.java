package api.arduinothermohygrometer.mapper;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.model.Humidity;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class HumidityModelMapper {
  public static Humidity toModel(final HumidityDto humidityDto) {
    return new Humidity(humidityDto.getRegisteredAt(), humidityDto.getAirHumidity());
  }

  public static HumidityDto toDto(final Humidity humidity) {
    return new HumidityDto(humidity.getRegisteredAt(), humidity.getAirHumidity());
  }
}
