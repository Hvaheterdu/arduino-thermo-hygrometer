package api.arduinothermohygrometer.mappers;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.models.Humidity;

public class HumidityEntityMapper {
    private HumidityEntityMapper() {
    }

    public static Humidity toEntity(HumidityDto humidityDto) {
        return new Humidity(humidityDto.getRegisteredAt(), humidityDto.getAirHumidity());
    }

    public static HumidityDto toDto(Humidity humidity) {
        return HumidityDto.builder()
                          .registeredAt(humidity.getRegisteredAt())
                          .airHumidity(humidity.getAirHumidity())
                          .build();
    }
}
