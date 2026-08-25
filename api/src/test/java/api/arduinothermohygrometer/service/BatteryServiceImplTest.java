package api.arduinothermohygrometer.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.BatteryModelMapper;
import api.arduinothermohygrometer.model.Battery;
import api.arduinothermohygrometer.repository.BatteryRepository;
import api.arduinothermohygrometer.service.implementation.BatteryServiceImpl;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class BatteryServiceImplTest {
  @Mock private BatteryRepository batteryRepository;

  @InjectMocks private BatteryServiceImpl batteryService;

  private BatteryDto createBatteryDto(LocalDateTime registeredAt, int batteryStatus) {
    return BatteryDto.builder().registeredAt(registeredAt).batteryStatus(batteryStatus).build();
  }

  @Nested
  class GetMethods {
    @Test
    void givenValidId_thenReturnBattery() {
      UUID id = UUID.randomUUID();
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      BatteryDto batteryDto = createBatteryDto(registeredAt, 90);
      Battery battery = BatteryModelMapper.toModel(batteryDto);
      when(batteryRepository.getBatteryById(id)).thenReturn(Optional.of(battery));

      BatteryDto result = batteryService.getBatteryById(id);

      assertThat(result.getRegisteredAt()).isEqualTo(batteryDto.getRegisteredAt());
      assertThat(result.getBatteryStatus()).isEqualTo(batteryDto.getBatteryStatus());
    }

    @Test
    void givenInvalidId_thenThrowResourceNotFoundException() {
      UUID invalidId = UUID.randomUUID();
      when(batteryRepository.getBatteryById(invalidId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> batteryService.getBatteryById(invalidId))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Battery not found.");
    }

    @Test
    void givenValidTimestamp_thenReturnBatteries() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      BatteryDto batteryDto = createBatteryDto(registeredAt, 90);
      List<Battery> batteries = List.of(BatteryModelMapper.toModel(batteryDto));
      when(batteryRepository.getBatteryByTimestamp(registeredAt)).thenReturn(batteries);

      List<BatteryDto> result = batteryService.getBatteriesByDateOrTimestamp(registeredAt, false);

      verify(batteryRepository).getBatteryByTimestamp(registeredAt);
      assertThat(result)
          .hasSize(1)
          .first()
          .satisfies(
              battery -> {
                assertThat(battery.getRegisteredAt()).isEqualTo(batteryDto.getRegisteredAt());
                assertThat(battery.getBatteryStatus()).isEqualTo(batteryDto.getBatteryStatus());
              });
    }

    @Test
    void givenInvalidTimestamp_thenThrowResourceNotFoundException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(batteryRepository.getBatteryByTimestamp(registeredAt)).thenReturn(emptyList());

      assertThatThrownBy(() -> batteryService.getBatteriesByDateOrTimestamp(registeredAt, false))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Batteries not found for timestamp " + registeredAt + ".");
    }

    @Test
    void givenValidDate_thenReturnBatteries() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      List<BatteryDto> batteryDtos =
          List.of(
              createBatteryDto(registeredAt, 90), createBatteryDto(registeredAt.minusHours(1), 85));
      List<Battery> batteries = batteryDtos.stream().map(BatteryModelMapper::toModel).toList();
      when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())).thenReturn(batteries);

      List<BatteryDto> result = batteryService.getBatteriesByDateOrTimestamp(registeredAt, true);

      verify(batteryRepository).getBatteriesByDate(registeredAt.toLocalDate());
      assertThat(result)
          .hasSize(2)
          .first()
          .satisfies(
              battery -> {
                assertThat(battery.getRegisteredAt())
                    .isEqualTo(batteryDtos.getFirst().getRegisteredAt());
                assertThat(battery.getBatteryStatus())
                    .isEqualTo(batteryDtos.getFirst().getBatteryStatus());
              });
    }

    @Test
    void givenInvalidDate_thenThrowResourceNotFoundException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate()))
          .thenReturn(emptyList());

      assertThatThrownBy(() -> batteryService.getBatteriesByDateOrTimestamp(registeredAt, true))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Batteries not found for date " + registeredAt.toLocalDate() + ".");
    }
  }

  @Nested
  class CreateMethods {
    @Test
    void givenValidBatteryModel_thenReturnCreatedBattery() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      BatteryDto batteryDto = createBatteryDto(registeredAt, 90);
      Battery battery = new Battery(registeredAt, 90);
      ReflectionTestUtils.setField(battery, "id", UUID.randomUUID());
      when(batteryRepository.createBattery(any(Battery.class))).thenReturn(Optional.of(battery));

      BatteryDto result = batteryService.createBattery(batteryDto);

      verify(batteryRepository).createBattery(any(Battery.class));
      assertThat(result.getRegisteredAt()).isEqualTo(batteryDto.getRegisteredAt());
      assertThat(result.getBatteryStatus()).isEqualTo(batteryDto.getBatteryStatus());
    }

    @Test
    void givenEmptyBatteryModel_thenThrowResourceNotCreatedException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      BatteryDto batteryDto = createBatteryDto(registeredAt, 90);
      when(batteryRepository.createBattery(any(Battery.class))).thenReturn(Optional.empty());

      assertThatThrownBy(() -> batteryService.createBattery(batteryDto))
          .isInstanceOf(ResourceNotCreatedException.class)
          .hasMessage("Battery cannot be created.");
    }
  }

  @Nested
  class DeleteMethods {
    @Test
    void givenValidTimestamp_thenDeleteBattery() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      BatteryDto batteryDto = createBatteryDto(registeredAt, 90);
      List<Battery> batteries = List.of(BatteryModelMapper.toModel(batteryDto));
      when(batteryRepository.getBatteryByTimestamp(registeredAt)).thenReturn(batteries);

      batteryService.deleteBatteriesByDateOrTimestamp(registeredAt, false);

      verify(batteryRepository).getBatteryByTimestamp(registeredAt);
      verify(batteryRepository).deleteBatteryByTimestamp(registeredAt);
    }

    @Test
    void givenInvalidTimestamp_thenThrowResourceNotFoundException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(batteryRepository.getBatteryByTimestamp(registeredAt)).thenReturn(emptyList());

      assertThatThrownBy(() -> batteryService.getBatteriesByDateOrTimestamp(registeredAt, false))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Batteries not found for timestamp " + registeredAt + ".");
    }

    @Test
    void givenValidDate_thenDeleteBattery() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      List<BatteryDto> batteryDtos =
          List.of(
              createBatteryDto(registeredAt, 90), createBatteryDto(registeredAt.minusHours(1), 85));
      List<Battery> batteries = batteryDtos.stream().map(BatteryModelMapper::toModel).toList();
      when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())).thenReturn(batteries);

      batteryService.deleteBatteriesByDateOrTimestamp(registeredAt, true);

      verify(batteryRepository).getBatteriesByDate(registeredAt.toLocalDate());
      verify(batteryRepository).deleteBatteriesByDate(registeredAt.toLocalDate());
    }

    @Test
    void givenInvalidDate_thenThrowResourceNotFoundException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate()))
          .thenReturn(emptyList());

      assertThatThrownBy(() -> batteryService.getBatteriesByDateOrTimestamp(registeredAt, true))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Batteries not found for date " + registeredAt.toLocalDate() + ".");
    }
  }
}
