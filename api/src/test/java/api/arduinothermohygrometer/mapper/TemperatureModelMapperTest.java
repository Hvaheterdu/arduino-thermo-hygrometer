package api.arduinothermohygrometer.mapper;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.model.Temperature;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TemperatureModelMapper unit tests.")
class TemperatureModelMapperTest {
    @Test
    void givenValidTemperatureDto_whenToModel_thenReturnTemperatureModel() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double temp = 86.123;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(registeredAt)
                                                      .temp(temp)
                                                      .build();

        Temperature result = TemperatureModelMapper.toModel(temperatureDto);

        assertThat(result.getRegisteredAt()).isEqualTo(temperatureDto.getRegisteredAt());
        assertThat(result.getTemp()).isEqualTo(temperatureDto.getTemp());
    }

    @Test
    void givenValidTemperatureModel_whenToDto_thenReturnTemperatureDto() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double temp = 86.425;
        Temperature temperature = new Temperature(registeredAt, temp);

        TemperatureDto result = TemperatureModelMapper.toDto(temperature);

        assertThat(result.getId()).isNull();
        assertThat(result.getRegisteredAt()).isEqualTo(temperature.getRegisteredAt());
        assertThat(result.getTemp()).isEqualTo(temperature.getTemp());
    }
}
