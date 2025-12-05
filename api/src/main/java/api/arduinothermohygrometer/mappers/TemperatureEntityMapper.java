package api.arduinothermohygrometer.mappers;

import java.util.Optional;

import org.springframework.stereotype.Component;

import api.arduinothermohygrometer.dtos.TemperatureDto;
import api.arduinothermohygrometer.entities.Temperature;

@Component
public class TemperatureEntityMapper {
    private TemperatureEntityMapper() {
    }

    public static Optional<Temperature> toModel(TemperatureDto temperatureDto) {
        return Optional.ofNullable(temperatureDto)
                .map(dto -> Temperature.builder()
                        .temp(dto.temp())
                        .build());
    }

    public static Optional<TemperatureDto> toDto(Temperature temperature) {
        return Optional.ofNullable(temperature)
                .map(entity -> TemperatureDto.builder().id(entity.getId())
                        .registeredAt(entity.getRegisteredAt())
                        .temp(entity.getTemp()).build());
    }
}
