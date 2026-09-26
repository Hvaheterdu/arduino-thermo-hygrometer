package api.arduinothermohygrometer.mapper;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.model.Temperature;

import static org.assertj.core.api.Assertions.assertThat;

class TemperatureDtoMapperTest {
  private final TemperatureDtoMapper temperatureDtoMapper = new TemperatureDtoMapper();

  @Test
  void givenValidTemperatureDto_thenReturnTemperature() {
    TemperatureDto temperatureDto =
        TemperatureDto.builder().registeredAt(LocalDateTime.now()).temp(86.123).build();

    Temperature result = temperatureDtoMapper.toModel(temperatureDto);

    assertThat(result.getId()).isNull();
    assertThat(result.getRegisteredAt()).isEqualTo(temperatureDto.getRegisteredAt());
    assertThat(result.getTemp()).isEqualTo(temperatureDto.getTemp());
  }

  @Test
  void givenValidTemperature_thenReturnTemperatureDto() {
    Temperature temperature = new Temperature(LocalDateTime.now(), 86.425);

    TemperatureDto result = temperatureDtoMapper.toDto(temperature);

    assertThat(result.getRegisteredAt()).isEqualTo(temperature.getRegisteredAt());
    assertThat(result.getTemp()).isEqualTo(temperature.getTemp());
  }
}
