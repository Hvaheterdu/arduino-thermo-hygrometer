package api.arduinothermohygrometer.mappers;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.models.Battery;

public class BatteryEntityMapper {
    private BatteryEntityMapper() {
    }

    public static Battery toEntity(BatteryDto batteryDto) {
        return new Battery(batteryDto.getRegisteredAt(), batteryDto.getBatteryStatus());
    }

    public static BatteryDto toDto(Battery battery) {
        return BatteryDto.builder()
                         .registeredAt(battery.getRegisteredAt())
                         .batteryStatus(battery.getBatteryStatus())
                         .build();
    }
}
