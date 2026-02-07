package api.arduinothermohygrometer.mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.entities.Humidity;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit tests for HumidityEntityMapper.")
class HumidityEntityMapperTest {
    @Test
    @DisplayName("toEntity returns humidity entity from valid humidityDto entity.")
    void givenValidHumidityDtoEntity_whenToEntity_thenReturnHumidityEntity() {
        UUID id = UUID.randomUUID();
        LocalDateTime registeredAt = LocalDateTime.now();
        Double airHumidity = 86.123;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .id(id)
                                             .registeredAt(registeredAt)
                                             .airHumidity(airHumidity)
                                             .build();

        Humidity result = HumidityEntityMapper.toEntity(humidityDto);

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(humidityDto);
    }

    @Test
    @DisplayName("toDto returns humidityDto entity from valid humidity entity.")
    void givenValidHumidityEntity_whenToDto_thenReturnHumidityDtoEntity() {
        Double airHumidity = 86.425;
        Humidity humidity = new Humidity(airHumidity);

        HumidityDto result = HumidityEntityMapper.toDto(humidity);

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(humidity);
    }
}
