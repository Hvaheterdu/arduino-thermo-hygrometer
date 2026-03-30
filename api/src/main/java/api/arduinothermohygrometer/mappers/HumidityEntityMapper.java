package api.arduinothermohygrometer.mappers;

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.entities.Humidity;

public class HumidityEntityMapper {
    private HumidityEntityMapper() {
    }

    public static Humidity toEntity(HumidityDto humidityDto) {
        return new Humidity(humidityDto.registeredAt(), humidityDto.airHumidity());
    }

    public static HumidityDto toDto(Humidity humidity) {
        return HumidityDto.builder()
                          .registeredAt(humidity.getRegisteredAt())
                          .airHumidity(humidity.getAirHumidity())
                          .build();
    }
}
