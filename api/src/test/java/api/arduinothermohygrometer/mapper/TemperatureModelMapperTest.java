package api.arduinothermohygrometer.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.model.Temperature;

class TemperatureModelMapperTest {
    @Test
    void givenValidTemperatureDto_whenToModel_thenReturnTemperatureModel() {
        TemperatureDto temperatureDto = TemperatureDto.builder()
                .registeredAt(LocalDateTime.now())
                .temp(86.123)
                .build();

        Temperature result = TemperatureModelMapper.toModel(temperatureDto);

        assertThat(result.getId()).isNull();
        assertThat(result.getRegisteredAt()).isEqualTo(temperatureDto.getRegisteredAt());
        assertThat(result.getTemp()).isEqualTo(temperatureDto.getTemp());
    }

    @Test
    void givenValidTemperatureModel_whenToDto_thenReturnTemperatureDto() {
        Temperature temperature = new Temperature(LocalDateTime.now(), 86.425);

        TemperatureDto result = TemperatureModelMapper.toDto(temperature);

        assertThat(result.getId()).isNull();
        assertThat(result.getRegisteredAt()).isEqualTo(temperature.getRegisteredAt());
        assertThat(result.getTemp()).isEqualTo(temperature.getTemp());
    }
}
