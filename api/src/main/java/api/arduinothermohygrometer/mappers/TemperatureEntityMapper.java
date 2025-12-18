package api.arduinothermohygrometer.mappers;

import org.springframework.stereotype.Component;

import api.arduinothermohygrometer.dtos.TemperatureDto;
import api.arduinothermohygrometer.entities.Temperature;

@Component
public class TemperatureEntityMapper {
    private TemperatureEntityMapper() {
    }

    public static Temperature toModel(TemperatureDto temperatureDto) {
        return Temperature.builder()
                          .temp(temperatureDto.temp())
                          .build();
    }

    public static TemperatureDto toDto(Temperature temperature) {
        return TemperatureDto.builder()
                             .id(temperature.getId())
                             .registeredAt(temperature.getRegisteredAt())
                             .temp(temperature.getTemp())
                             .build();
    }
}
