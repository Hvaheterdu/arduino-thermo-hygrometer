package api.arduinothermohygrometer.mappers;

import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.entities.Battery;

public class BatteryEntityMapper {
    private BatteryEntityMapper() {
    }

    public static Battery toEntity(BatteryDto batteryDto) {
        return new Battery(batteryDto.registeredAt(), batteryDto.batteryStatus());
    }

    public static BatteryDto toDto(Battery battery) {
        return BatteryDto.builder()
                         .registeredAt(battery.getRegisteredAt())
                         .batteryStatus(battery.getBatteryStatus())
                         .build();
    }
}
