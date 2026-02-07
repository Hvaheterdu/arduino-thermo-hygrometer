package api.arduinothermohygrometer.mappers;

import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.entities.Battery;

public class BatteryEntityMapper {
    private BatteryEntityMapper() {
    }

    public static Battery toEntity(BatteryDto batteryDto) {
        Battery battery = new Battery(batteryDto.batteryStatus());
        battery.setId(batteryDto.id());
        battery.setRegisteredAt(batteryDto.registeredAt());

        return battery;
    }

    public static BatteryDto toDto(Battery battery) {
        return BatteryDto.builder()
                         .id(battery.getId())
                         .registeredAt(battery.getRegisteredAt())
                         .batteryStatus(battery.getBatteryStatus())
                         .build();
    }
}
