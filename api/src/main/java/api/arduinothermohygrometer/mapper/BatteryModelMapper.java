package api.arduinothermohygrometer.mapper;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.model.Battery;

public class BatteryModelMapper {
    private BatteryModelMapper() {
    }

    public static Battery toModel(BatteryDto batteryDto) {
        return new Battery(batteryDto.getRegisteredAt(), batteryDto.getBatteryStatus());
    }

    public static BatteryDto toDto(Battery battery) {
        return BatteryDto.builder()
                         .registeredAt(battery.getRegisteredAt())
                         .batteryStatus(battery.getBatteryStatus())
                         .build();
    }
}
