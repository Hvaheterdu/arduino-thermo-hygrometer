package api.arduinothermohygrometer.mappers;

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.entities.Humidity;

public class HumidityEntityMapper {
    private HumidityEntityMapper() {
    }

    public static Humidity toEntity(HumidityDto humidityDto) {
        Humidity humidity = new Humidity(humidityDto.airHumidity());
        humidity.setId(humidityDto.id());
        humidity.setRegisteredAt(humidityDto.registeredAt());

        return humidity;
    }

    public static HumidityDto toDto(Humidity humidity) {
        return HumidityDto.builder()
                          .id(humidity.getId())
                          .registeredAt(humidity.getRegisteredAt())
                          .airHumidity(humidity.getAirHumidity())
                          .build();
    }
}
