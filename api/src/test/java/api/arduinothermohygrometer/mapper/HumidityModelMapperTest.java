package api.arduinothermohygrometer.mapper;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.model.Humidity;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HumidityModelMapper unit tests.")
class HumidityModelMapperTest {
    @Test
    @DisplayName("toModel returns humidity model from valid humidityDto model.")
    void givenValidHumidityDtoModel_whenToModel_thenReturnHumidityModel() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double airHumidity = 86.123;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(registeredAt)
                                             .airHumidity(airHumidity)
                                             .build();

        Humidity result = HumidityModelMapper.toModel(humidityDto);

        assertThat(result.getRegisteredAt()).isEqualTo(humidityDto.getRegisteredAt());
        assertThat(result.getAirHumidity()).isEqualTo(humidityDto.getAirHumidity());
    }

    @Test
    @DisplayName("toDto returns humidityDto model from valid humidity model.")
    void givenValidHumidityModel_whenToDto_thenReturnHumidityDtoModel() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double airHumidity = 86.425;
        Humidity humidity = new Humidity(registeredAt, airHumidity);

        HumidityDto result = HumidityModelMapper.toDto(humidity);

        assertThat(result.getRegisteredAt()).isEqualTo(humidity.getRegisteredAt());
        assertThat(result.getAirHumidity()).isEqualTo(humidity.getAirHumidity());
    }
}
