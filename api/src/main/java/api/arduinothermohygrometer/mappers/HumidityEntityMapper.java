package api.arduinothermohygrometer.mappers;

import org.springframework.stereotype.Component;

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.entities.Humidity;

@Component
public class HumidityEntityMapper {
    private HumidityEntityMapper() {
    }

    public static Humidity toModel(HumidityDto humidityDto) {
        return Humidity.builder()
                       .airHumidity(humidityDto.airHumidity())
                       .build();
    }

    public static HumidityDto toDto(Humidity humidity) {
        return HumidityDto.builder()
                          .id(humidity.getId())
                          .registeredAt(humidity.getRegisteredAt())
                          .airHumidity(humidity.getAirHumidity())
                          .build();
    }
}
