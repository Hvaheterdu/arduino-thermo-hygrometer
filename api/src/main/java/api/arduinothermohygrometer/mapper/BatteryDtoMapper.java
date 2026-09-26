package api.arduinothermohygrometer.mapper;

import org.springframework.stereotype.Component;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.model.Battery;

@Component
public class BatteryDtoMapper {
  public Battery toModel(final BatteryDto batteryDto) {
    return new Battery(batteryDto.getRegisteredAt(), batteryDto.getBatteryStatus());
  }

  public BatteryDto toDto(final Battery battery) {
    return new BatteryDto(battery.getRegisteredAt(), battery.getBatteryStatus());
  }
}
