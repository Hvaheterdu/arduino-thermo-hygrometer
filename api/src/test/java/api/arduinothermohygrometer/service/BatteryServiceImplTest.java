package api.arduinothermohygrometer.service;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.BatteryModelMapper;
import api.arduinothermohygrometer.model.Battery;
import api.arduinothermohygrometer.repository.BatteryRepository;
import api.arduinothermohygrometer.service.implementation.BatteryServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class BatteryServiceImplTest {
    @Mock
    private BatteryRepository batteryRepository;

    @InjectMocks
    private BatteryServiceImpl batteryService;

    @Nested
    class GetMethods {
        @Test
        void givenValidId_whenGetBatteryById_thenReturnBattery() {
            UUID id = UUID.randomUUID();
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            BatteryDto batteryDto = createBatteryDto(registeredAt, 90);
            Battery battery = BatteryModelMapper.toModel(batteryDto);
            when(batteryRepository.getBatteryById(id)).thenReturn(Optional.of(battery));

            BatteryDto result = batteryService.getBatteryById(id);

            assertThat(result.getId()).isEqualTo(batteryDto.getId());
            assertThat(result.getRegisteredAt()).isEqualTo(batteryDto.getRegisteredAt());
            assertThat(result.getBatteryStatus()).isEqualTo(batteryDto.getBatteryStatus());
        }

        @Test
        void givenInvalidId_whenGetBatteryById_thenThrowResourceNotFoundException() {
            UUID invalidId = UUID.randomUUID();
            when(batteryRepository.getBatteryById(invalidId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> batteryService.getBatteryById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Battery with id=%s not found.".formatted(invalidId));
        }

        @Test
        void givenValidTimestamp_whenGetBatteriesByDateOrTimestamp_thenReturnBattery() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            BatteryDto batteryDto = createBatteryDto(registeredAt, 90);
            List<Battery> batteries = List.of(BatteryModelMapper.toModel(batteryDto));
            when(batteryRepository.getBatteryByTimestamp(registeredAt)).thenReturn(batteries);

            List<BatteryDto> result = batteryService.getBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository).getBatteryByTimestamp(registeredAt);
            assertThat(result)
                .hasSize(1)
                .first()
                .satisfies(battery -> {
                    assertThat(battery.getId()).isEqualTo(batteryDto.getId());
                    assertThat(battery.getRegisteredAt()).isEqualTo(batteryDto.getRegisteredAt());
                    assertThat(battery.getBatteryStatus()).isEqualTo(batteryDto.getBatteryStatus());
                });
        }

        @Test
        void givenInvalidTimestamp_whenGetBatteriesByDateOrTimestamp_thenReturnEmptyList() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            when(batteryRepository.getBatteryByTimestamp(registeredAt)).thenReturn(emptyList());

            List<BatteryDto> result = batteryService.getBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository).getBatteryByTimestamp(registeredAt);
            assertThat(result)
                .isEmpty();
        }

        @Test
        void givenValidDate_whenGetBatteriesByDateOrTimestamp_thenReturnBatteries() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            List<BatteryDto> batteryDtos = List.of(
                createBatteryDto(registeredAt, 90),
                createBatteryDto(registeredAt.minusHours(1), 85)
            );
            List<Battery> batteries = batteryDtos.stream()
                                                 .map(BatteryModelMapper::toModel)
                                                 .toList();
            when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())).thenReturn(batteries);

            List<BatteryDto> result = batteryService.getBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository).getBatteriesByDate(registeredAt.toLocalDate());
            assertThat(result)
                .hasSize(2)
                .first()
                .satisfies(battery -> {
                    assertThat(battery.getId()).isEqualTo(batteryDtos.getFirst().getId());
                    assertThat(battery.getRegisteredAt()).isEqualTo(batteryDtos.getFirst().getRegisteredAt());
                    assertThat(battery.getBatteryStatus()).isEqualTo(batteryDtos.getFirst().getBatteryStatus());
                });
        }

        @Test
        void givenInvalidDate_whenGetBatteriesByDateOrTimestamp_thenReturnEmptyList() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

            List<BatteryDto> result = batteryService.getBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository).getBatteriesByDate(registeredAt.toLocalDate());
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class CreateMethods {
        @Test
        void givenValidBatteryModel_whenCreateBattery_thenReturnCreatedBattery() {
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            BatteryDto batteryDto = createBatteryDto(registeredAt, 90);
            Battery battery = new Battery(registeredAt, 90);
            ReflectionTestUtils.setField(battery, "id", UUID.randomUUID());
            when(batteryRepository.createBattery(any(Battery.class))).thenReturn(Optional.of(battery));

            BatteryDto result = batteryService.createBattery(batteryDto);

            verify(batteryRepository).createBattery(any(Battery.class));
            assertThat(result.getId()).isNotNull();
            assertThat(result.getRegisteredAt()).isEqualTo(batteryDto.getRegisteredAt());
            assertThat(result.getBatteryStatus()).isEqualTo(batteryDto.getBatteryStatus());
        }

        @Test
        void givenEmptyBatteryModel_whenCreateBattery_thenThrowResourceNotCreatedException() {
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
        void givenValidId_whenDeleteBatteryById_thenDeleteBattery() {
            UUID id = UUID.randomUUID();
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            BatteryDto batteryDto = createBatteryDto(registeredAt, 90);
            Battery battery = BatteryModelMapper.toModel(batteryDto);
            when(batteryRepository.getBatteryById(id)).thenReturn(Optional.of(battery));

            batteryService.deleteBatteryById(id);

            verify(batteryRepository).getBatteryById(id);
            verify(batteryRepository).deleteBatteryById(id);
        }

        @Test
        void givenInvalidId_whenDeleteBatteryById_thenThrowResourceNotFoundException() {
            UUID invalidId = UUID.randomUUID();
            when(batteryRepository.getBatteryById(invalidId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> batteryService.deleteBatteryById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Battery with id=%s not found.".formatted(invalidId));
        }

        @Test
        void givenValidTimestamp_whenDeleteBatteryByDateOrTimestamp_thenDeleteBattery() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            BatteryDto batteryDto = createBatteryDto(registeredAt, 90);
            List<Battery> batteries = List.of(BatteryModelMapper.toModel(batteryDto));
            when(batteryRepository.getBatteryByTimestamp(registeredAt)).thenReturn(batteries);

            batteryService.deleteBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository).getBatteryByTimestamp(registeredAt);
            verify(batteryRepository).deleteBatteryByTimestamp(registeredAt);
        }

        @Test
        void givenInvalidTimestamp_whenDeleteBatteryByDateOrTimestamp_thenReturn() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            when(batteryRepository.getBatteryByTimestamp(registeredAt)).thenReturn(emptyList());

            batteryService.deleteBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository).getBatteryByTimestamp(registeredAt);
            verify(batteryRepository, times(0)).deleteBatteryByTimestamp(registeredAt);
        }

        @Test
        void givenValidDate_whenDeleteBatteryByDateOrTimestamp_thenDeleteBattery() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            List<BatteryDto> batteryDtos = List.of(
                createBatteryDto(registeredAt, 90),
                createBatteryDto(registeredAt.minusHours(1), 85)
            );
            List<Battery> batteries = batteryDtos.stream()
                                                 .map(BatteryModelMapper::toModel)
                                                 .toList();
            when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())).thenReturn(batteries);

            batteryService.deleteBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository).getBatteriesByDate(registeredAt.toLocalDate());
            verify(batteryRepository).deleteBatteriesByDate(registeredAt.toLocalDate());
        }

        @Test
        void givenInvalidDate_whenDeleteBatteryByDateOrTimestamp_thenReturn() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

            batteryService.deleteBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository).getBatteriesByDate(registeredAt.toLocalDate());
            verify(batteryRepository, times(0)).deleteBatteriesByDate(registeredAt.toLocalDate());
        }
    }

    private BatteryDto createBatteryDto(LocalDateTime registeredAt, int batteryStatus) {
        return BatteryDto.builder()
                         .registeredAt(registeredAt)
                         .batteryStatus(batteryStatus)
                         .build();
    }
}
