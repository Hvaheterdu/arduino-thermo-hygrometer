package api.arduinothermohygrometer.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.entities.Battery;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.mappers.BatteryEntityMapper;
import api.arduinothermohygrometer.repositories.BatteryRepository;
import api.arduinothermohygrometer.services.implementations.BatteryServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for BatteryServiceImpl")
@ExtendWith(MockitoExtension.class)
class BatteryServiceImplTest {
    @Mock
    private BatteryRepository batteryRepository;

    @Captor
    ArgumentCaptor<Battery> batteryArgumentCaptor;

    private BatteryServiceImpl batteryService;

    @BeforeEach
    void setUp() {
        batteryService = new BatteryServiceImpl(batteryRepository);
        batteryArgumentCaptor = ArgumentCaptor.forClass(Battery.class);
    }

    @Test
    @DisplayName("getBatteryById returns battery with valid id.")
    void givenValidId_whenGettingBatteryById_thenReturnBattery() {
        UUID id = UUID.randomUUID();
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .id(id)
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();
        Battery battery = BatteryEntityMapper.toModel(batteryDto);
        when(batteryRepository.getBatteryById(id)).thenReturn(Optional.of(battery));

        BatteryDto result = batteryService.getBatteryDtoById(id);

        assertThat(result)
            .isEqualTo(batteryDto);
    }

    @Test
    @DisplayName("getBatteryById throws ResourceNotFoundException with empty id.")
    void givenEmptyId_whenGettingBatteryById_thenThrowResourceNotFoundException() {
        UUID id = new UUID(0, 0);

        assertThatThrownBy(() -> batteryService.getBatteryDtoById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Battery with id=%s not found.", id));
    }

    @Test
    @DisplayName("getBatteryById throws ResourceNotFoundException with invalid id.")
    void givenInvalidId_whenGettingBatteryById_thenThrowResourceNotFoundException() {
        UUID id = UUID.randomUUID();
        when(batteryRepository.getBatteryById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> batteryService.getBatteryDtoById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Battery with id=%s not found.", id));
    }
}
