package api.arduinothermohygrometer.mappers;

import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.entities.Battery;

public class BatteryEntityMapper {
    private BatteryEntityMapper() {
    }

    public static Battery toModel(BatteryDto batteryDto) {
        return Battery.builder()
                      .batteryStatus(batteryDto.batteryStatus())
                      .build();
    }

    public static BatteryDto toDto(Battery battery) {
        return BatteryDto.builder()
                         .id(battery.getId())
                         .registeredAt(battery.getRegisteredAt())
                         .batteryStatus(battery.getBatteryStatus())
                         .build();
    }
}
