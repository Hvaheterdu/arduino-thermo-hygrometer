package api.arduinothermohygrometer.mappers;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.models.Battery;

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

        assertThat(result.getRegisteredAt()).isEqualTo(batteryDto.getRegisteredAt());
        assertThat(result.getBatteryStatus()).isEqualTo(batteryDto.getBatteryStatus());
    }

    @Test
    @DisplayName("toDto returns batteryDto entity from valid battery entity.")
    void givenValidBatteryEntity_whenToDto_thenReturnBatteryDtoEntity() {
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 95;
        Battery battery = new Battery(registeredAt, batteryStatus);

        BatteryDto result = BatteryEntityMapper.toDto(battery);

        assertThat(result.getRegisteredAt()).isEqualTo(battery.getRegisteredAt());
        assertThat(result.getBatteryStatus()).isEqualTo(battery.getBatteryStatus());
    }
}
