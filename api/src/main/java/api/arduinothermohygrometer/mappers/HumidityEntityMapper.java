package api.arduinothermohygrometer.mappers;

import java.util.Optional;

import org.springframework.stereotype.Component;

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.entities.Humidity;

@Component
public class HumidityEntityMapper {
    private HumidityEntityMapper() {
    }

    public static Optional<Humidity> toModel(HumidityDto humidityDto) {
        return Optional.ofNullable(humidityDto)
                .map(dto -> Humidity.builder()
                        .airHumidity(dto.airHumidity())
                        .build());
    }

    public static Optional<HumidityDto> toDto(Humidity humidity) {
        return Optional.ofNullable(humidity)
                .map(entity -> HumidityDto.builder().id(entity.getId())
                        .registeredAt(entity.getRegisteredAt())
                        .airHumidity(entity.getAirHumidity()).build());
    }
}
