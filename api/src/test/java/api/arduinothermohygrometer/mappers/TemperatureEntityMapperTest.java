package api.arduinothermohygrometer.mappers;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dtos.TemperatureDto;
import api.arduinothermohygrometer.entities.Temperature;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit tests for TemperatureEntityMapper.")
class TemperatureEntityMapperTest {
    @Test
    @DisplayName("toEntity returns temperature entity from valid temperatureDto entity.")
    void givenValidTemperatureDtoEntity_whenToEntity_thenReturnTemperatureEntity() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double temp = 86.123;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(registeredAt)
                                                      .temp(temp)
                                                      .build();

        Temperature result = TemperatureEntityMapper.toEntity(temperatureDto);

        assertThat(result.getRegisteredAt()).isEqualTo(temperatureDto.registeredAt());
        assertThat(result.getTemp()).isEqualTo(temperatureDto.temp());
    }

    @Test
    @DisplayName("toDto returns temperatureDto entity from valid temperature entity.")
    void givenValidTemperatureEntity_whenToDto_thenReturnTemperatureDtoEntity() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double temp = 86.425;
        Temperature temperature = new Temperature(registeredAt, temp);

        TemperatureDto result = TemperatureEntityMapper.toDto(temperature);

        assertThat(result.registeredAt()).isEqualTo(temperature.getRegisteredAt());
        assertThat(result.temp()).isEqualTo(temperature.getTemp());
    }
}
