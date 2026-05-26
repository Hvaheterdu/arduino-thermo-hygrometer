package api.arduinothermohygrometer.mapper;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.model.Temperature;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TemperatureModelMapper {
    public static Temperature toModel(final TemperatureDto temperatureDto) {
        return new Temperature(temperatureDto.getRegisteredAt(), temperatureDto.getTemp());
    }

    public static TemperatureDto toDto(final Temperature temperature) {
        return TemperatureDto.builder()
                .id(temperature.getId())
                .registeredAt(temperature.getRegisteredAt())
                .temp(temperature.getTemp())
                .build();
    }
}
