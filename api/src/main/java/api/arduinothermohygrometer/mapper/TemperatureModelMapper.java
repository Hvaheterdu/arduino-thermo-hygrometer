package api.arduinothermohygrometer.mapper;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.model.Temperature;

public class TemperatureModelMapper {
    private TemperatureModelMapper() {
    }

    public static Temperature toModel(TemperatureDto temperatureDto) {
        return new Temperature(temperatureDto.getRegisteredAt(), temperatureDto.getTemp());
    }

    public static TemperatureDto toDto(Temperature temperature) {
        return TemperatureDto.builder()
                             .id(temperature.getId())
                             .registeredAt(temperature.getRegisteredAt())
                             .temp(temperature.getTemp())
                             .build();
    }
}
