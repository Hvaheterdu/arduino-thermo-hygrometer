package api.arduinothermohygrometer.mapper;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.model.Battery;

import static org.assertj.core.api.Assertions.assertThat;

class BatteryDtoMapperTest {
  private final BatteryDtoMapper batteryDtoMapper = new BatteryDtoMapper();

  @Test
  void givenValidBatteryDto_thenReturnBattery() {
    BatteryDto batteryDto =
        BatteryDto.builder().registeredAt(LocalDateTime.now()).batteryStatus(95).build();

    Battery result = batteryDtoMapper.toModel(batteryDto);

    assertThat(result.getId()).isNull();
    assertThat(result.getRegisteredAt()).isEqualTo(batteryDto.getRegisteredAt());
    assertThat(result.getBatteryStatus()).isEqualTo(batteryDto.getBatteryStatus());
  }

  @Test
  void givenValidBattery_thenReturnBatteryDto() {
    Battery battery = new Battery(LocalDateTime.now(), 95);

    BatteryDto result = batteryDtoMapper.toDto(battery);

    assertThat(result.getRegisteredAt()).isEqualTo(battery.getRegisteredAt());
    assertThat(result.getBatteryStatus()).isEqualTo(battery.getBatteryStatus());
  }
}
