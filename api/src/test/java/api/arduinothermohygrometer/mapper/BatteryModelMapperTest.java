package api.arduinothermohygrometer.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.model.Battery;

class BatteryModelMapperTest {
    @Test
    void givenValidBatteryDto_whenToModel_thenReturnBatteryModel() {
        BatteryDto batteryDto = BatteryDto.builder()
                .registeredAt(LocalDateTime.now())
                .batteryStatus(95)
                .build();

        Battery result = BatteryModelMapper.toModel(batteryDto);

        assertThat(result.getRegisteredAt()).isEqualTo(batteryDto.getRegisteredAt());
        assertThat(result.getBatteryStatus()).isEqualTo(batteryDto.getBatteryStatus());
    }

    @Test
    void givenValidBatteryModel_whenToDto_thenReturnBatteryDto() {
        Battery battery = new Battery(LocalDateTime.now(), 95);

        BatteryDto result = BatteryModelMapper.toDto(battery);

        assertThat(result.getId()).isNull();
        assertThat(result.getRegisteredAt()).isEqualTo(battery.getRegisteredAt());
        assertThat(result.getBatteryStatus()).isEqualTo(battery.getBatteryStatus());
    }
}
