package api.arduinothermohygrometer.mappers;

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.entities.Humidity;

public class HumidityEntityMapper {
    private HumidityEntityMapper() {
    }

    public static Humidity toEntity(HumidityDto humidityDto) {
        return Humidity.builder()
                       .registeredAt(humidityDto.registeredAt())
                       .airHumidity(humidityDto.airHumidity())
                       .build();
    }

    public static HumidityDto toDto(Humidity humidity) {
        return HumidityDto.builder()
                          .registeredAt(humidity.getRegisteredAt())
                          .airHumidity(humidity.getAirHumidity())
                          .build();
    }
}
