package api.arduinothermohygrometer.mappers;

import api.arduinothermohygrometer.dtos.TemperatureDto;
import api.arduinothermohygrometer.entities.Temperature;

public class TemperatureEntityMapper {
    private TemperatureEntityMapper() {
    }

    public static Temperature toEntity(TemperatureDto temperatureDto) {
        Temperature temperature = new Temperature(temperatureDto.temp());
        temperature.setId(temperatureDto.id());
        temperature.setRegisteredAt(temperatureDto.registeredAt());

        return temperature;
    }

    public static TemperatureDto toDto(Temperature temperature) {
        return TemperatureDto.builder()
                             .id(temperature.getId())
                             .registeredAt(temperature.getRegisteredAt())
                             .temp(temperature.getTemp())
                             .build();
    }
}
