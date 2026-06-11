package api.arduinothermohygrometer.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.model.Humidity;

class HumidityModelMapperTest {
    @Test
    void givenValidHumidityDto_whenToModel_thenReturnHumidityModel() {
        HumidityDto humidityDto = HumidityDto.builder()
                .registeredAt(LocalDateTime.now())
                .airHumidity(86.123)
                .build();

        Humidity result = HumidityModelMapper.toModel(humidityDto);

        assertThat(result.getRegisteredAt()).isEqualTo(humidityDto.getRegisteredAt());
        assertThat(result.getAirHumidity()).isEqualTo(humidityDto.getAirHumidity());
    }

    @Test
    void givenValidHumidityModel_whenToDto_thenReturnHumidityDto() {
        Humidity humidity = new Humidity(LocalDateTime.now(), 86.425);

        HumidityDto result = HumidityModelMapper.toDto(humidity);

        assertThat(result.getId()).isNull();
        assertThat(result.getRegisteredAt()).isEqualTo(humidity.getRegisteredAt());
        assertThat(result.getAirHumidity()).isEqualTo(humidity.getAirHumidity());
    }
}
