package api.arduinothermohygrometer.mapper;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.model.Battery;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BatteryModelMapper {
    public static Battery toModel(final BatteryDto batteryDto) {
        return new Battery(batteryDto.getRegisteredAt(), batteryDto.getBatteryStatus());
    }

    public static BatteryDto toDto(final Battery battery) {
        return BatteryDto.builder()
                         .id(battery.getId())
                         .registeredAt(battery.getRegisteredAt())
                         .batteryStatus(battery.getBatteryStatus())
                         .build();
    }
}
