package api.arduinothermohygrometer.mappers;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.models.Temperature;

public class TemperatureEntityMapper {
    private TemperatureEntityMapper() {
    }

    public static Temperature toEntity(TemperatureDto temperatureDto) {
        return new Temperature(temperatureDto.getRegisteredAt(), temperatureDto.getTemp());
    }

    public static TemperatureDto toDto(Temperature temperature) {
        return TemperatureDto.builder()
                             .registeredAt(temperature.getRegisteredAt())
                             .temp(temperature.getTemp())
                             .build();
    }
}
