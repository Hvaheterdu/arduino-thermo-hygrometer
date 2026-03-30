package api.arduinothermohygrometer.mappers;

import api.arduinothermohygrometer.dtos.TemperatureDto;
import api.arduinothermohygrometer.entities.Temperature;

public class TemperatureEntityMapper {
    private TemperatureEntityMapper() {
    }

    public static Temperature toEntity(TemperatureDto temperatureDto) {
        return Temperature.builder()
                          .registeredAt(temperatureDto.registeredAt())
                          .temp(temperatureDto.temp())
                          .build();
    }

    public static TemperatureDto toDto(Temperature temperature) {
        return TemperatureDto.builder()
                             .registeredAt(temperature.getRegisteredAt())
                             .temp(temperature.getTemp())
                             .build();
    }
}
