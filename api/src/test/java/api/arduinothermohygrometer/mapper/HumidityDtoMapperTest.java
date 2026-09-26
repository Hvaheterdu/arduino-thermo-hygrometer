package api.arduinothermohygrometer.mapper;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.model.Humidity;

import static org.assertj.core.api.Assertions.assertThat;

class HumidityDtoMapperTest {
  private final HumidityDtoMapper humidityDtoMapper = new HumidityDtoMapper();

  @Test
  void givenValidHumidityDto_thenReturnHumidity() {
    HumidityDto humidityDto =
        HumidityDto.builder().registeredAt(LocalDateTime.now()).airHumidity(86.123).build();

    Humidity result = humidityDtoMapper.toModel(humidityDto);

    assertThat(result.getId()).isNull();
    assertThat(result.getRegisteredAt()).isEqualTo(humidityDto.getRegisteredAt());
    assertThat(result.getAirHumidity()).isEqualTo(humidityDto.getAirHumidity());
  }

  @Test
  void givenValidHumidity_thenReturnHumidityDto() {
    Humidity humidity = new Humidity(LocalDateTime.now(), 86.425);

    HumidityDto result = humidityDtoMapper.toDto(humidity);

    assertThat(result.getRegisteredAt()).isEqualTo(humidity.getRegisteredAt());
    assertThat(result.getAirHumidity()).isEqualTo(humidity.getAirHumidity());
  }
}
