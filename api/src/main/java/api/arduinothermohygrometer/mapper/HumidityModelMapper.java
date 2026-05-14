package api.arduinothermohygrometer.mapper;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.model.Humidity;

public class HumidityModelMapper {
    private HumidityModelMapper() {
    }

    public static Humidity toModel(HumidityDto humidityDto) {
        return new Humidity(humidityDto.getRegisteredAt(), humidityDto.getAirHumidity());
    }

    public static HumidityDto toDto(Humidity humidity) {
        return HumidityDto.builder()
                          .id(humidity.getId())
                          .registeredAt(humidity.getRegisteredAt())
                          .airHumidity(humidity.getAirHumidity())
                          .build();
    }
}
