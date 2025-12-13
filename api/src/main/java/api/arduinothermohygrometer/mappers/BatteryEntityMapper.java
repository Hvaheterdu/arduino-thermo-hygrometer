package api.arduinothermohygrometer.mappers;

import org.springframework.stereotype.Component;

import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.entities.Battery;

@Component
public class BatteryEntityMapper {
    private BatteryEntityMapper() {
    }

    public static Battery toModel(BatteryDto batteryDto) {
        return Battery.builder()
                .batteryStatus(batteryDto.batteryStatus())
                .build();
    }

    public static BatteryDto toDto(Battery battery) {
        return BatteryDto.builder().id(battery.getId())
                .registeredAt(battery.getRegisteredAt())
                .batteryStatus(battery.getBatteryStatus())
                .build();
    }
}
