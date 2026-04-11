package api.arduinothermohygrometer.mappers;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.models.Humidity;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit tests for HumidityEntityMapper.")
class HumidityEntityMapperTest {
    @Test
    @DisplayName("toEntity returns humidity entity from valid humidityDto entity.")
    void givenValidHumidityDtoEntity_whenToEntity_thenReturnHumidityEntity() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double airHumidity = 86.123;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(registeredAt)
                                             .airHumidity(airHumidity)
                                             .build();

        Humidity result = HumidityEntityMapper.toEntity(humidityDto);

        assertThat(result.getRegisteredAt()).isEqualTo(humidityDto.getRegisteredAt());
        assertThat(result.getAirHumidity()).isEqualTo(humidityDto.getAirHumidity());
    }

    @Test
    @DisplayName("toDto returns humidityDto entity from valid humidity entity.")
    void givenValidHumidityEntity_whenToDto_thenReturnHumidityDtoEntity() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double airHumidity = 86.425;
        Humidity humidity = new Humidity(registeredAt, airHumidity);

        HumidityDto result = HumidityEntityMapper.toDto(humidity);

        assertThat(result.getRegisteredAt()).isEqualTo(humidity.getRegisteredAt());
        assertThat(result.getAirHumidity()).isEqualTo(humidity.getAirHumidity());
    }
}
