package api.arduinothermohygrometer.mapper;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.model.Battery;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BatteryModelMapper unit tests.")
class BatteryModelMapperTest {
    @Test
    @DisplayName("toModel returns battery model from valid batteryDto model.")
    void givenValidBatteryDtoModel_whenToModel_thenReturnBatteryModel() {
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 95;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();

        Battery result = BatteryModelMapper.toModel(batteryDto);

        assertThat(result.getRegisteredAt()).isEqualTo(batteryDto.getRegisteredAt());
        assertThat(result.getBatteryStatus()).isEqualTo(batteryDto.getBatteryStatus());
    }

    @Test
    @DisplayName("toDto returns batteryDto model from valid battery model.")
    void givenValidBatteryModel_whenToDto_thenReturnBatteryDtoModel() {
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 95;
        Battery battery = new Battery(registeredAt, batteryStatus);

        BatteryDto result = BatteryModelMapper.toDto(battery);

        assertThat(result.getRegisteredAt()).isEqualTo(battery.getRegisteredAt());
        assertThat(result.getBatteryStatus()).isEqualTo(battery.getBatteryStatus());
    }
}
