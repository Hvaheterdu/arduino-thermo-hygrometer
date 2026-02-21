package api.arduinothermohygrometer.mappers;

import java.time.LocalDateTime;
import java.util.UUID;

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
        UUID id = UUID.randomUUID();
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 95;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .id(id)
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();

        Battery result = BatteryEntityMapper.toEntity(batteryDto);

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(batteryDto);
    }

    @Test
    @DisplayName("toDto returns batteryDto entity from valid battery entity.")
    void givenValidBatteryEntity_whenToDto_thenReturnBatteryDtoEntity() {
        int batteryStatus = 95;
        Battery battery = new Battery(batteryStatus);

        BatteryDto result = BatteryEntityMapper.toDto(battery);

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(battery);
    }
}
