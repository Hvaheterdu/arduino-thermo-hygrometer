package api.arduinothermohygrometer.mappers;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.entities.Battery;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit tests for BatteryEntityMapper")
class BatteryEntityMapperTest {
    @Test
    @DisplayName("toEntity returns battery entity from valid batteryDto entity.")
    void givenValidBatteryDtoEntity_whenToEntity_thenReturnBatteryEntity() {
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 95;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();

        Battery result = BatteryEntityMapper.toEntity(batteryDto);

        assertThat(result.getRegisteredAt()).isEqualTo(batteryDto.registeredAt());
        assertThat(result.getBatteryStatus()).isEqualTo(batteryDto.batteryStatus());
    }

    @Test
    @DisplayName("toDto returns batteryDto entity from valid battery entity.")
    void givenValidBatteryEntity_whenToDto_thenReturnBatteryDtoEntity() {
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 95;
        Battery battery = Battery.builder()
                                 .registeredAt(registeredAt)
                                 .batteryStatus(batteryStatus)
                                 .build();

        BatteryDto result = BatteryEntityMapper.toDto(battery);

        assertThat(result.registeredAt()).isEqualTo(battery.getRegisteredAt());
        assertThat(result.batteryStatus()).isEqualTo(battery.getBatteryStatus());
    }
}
