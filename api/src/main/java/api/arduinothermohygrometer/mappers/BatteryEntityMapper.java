package api.arduinothermohygrometer.mappers;

import java.util.Optional;

import org.springframework.stereotype.Component;

import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.entities.Battery;

@Component
public class BatteryEntityMapper {
    private BatteryEntityMapper() {
    }

    public static Optional<Battery> toModel(BatteryDto batteryDto) {
        return Optional.ofNullable(batteryDto)
                .map(dto -> Battery.builder()
                        .batteryStatus(dto.batteryStatus())
                        .build());
    }

    public static Optional<BatteryDto> toDto(Battery battery) {
        return Optional.ofNullable(battery)
                .map(entity -> BatteryDto.builder().id(entity.getId())
                        .registeredAt(entity.getRegisteredAt())
                        .batteryStatus(entity.getBatteryStatus()).build());
    }
}
