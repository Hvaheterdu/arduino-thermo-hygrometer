package api.arduinothermohygrometer.service;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.BatteryModelMapper;
import api.arduinothermohygrometer.model.Battery;
import api.arduinothermohygrometer.repository.BatteryRepository;
import api.arduinothermohygrometer.service.implementation.BatteryServiceImpl;

@DisplayName("BatteryServiceImpl unit tests.")
@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
class BatteryServiceImplTest {
    @Mock
    private BatteryRepository batteryRepository;

    @Captor
    private ArgumentCaptor<Battery> batteryArgumentCaptor = ArgumentCaptor.forClass(Battery.class);

    @InjectMocks
    private BatteryServiceImpl batteryService;

    @DisplayName("Get methods for BatteryServiceImpl.")
    @Nested
    class GetMethods {
        @Test
        void givenValidId_whenGetBatteryById_thenReturnBattery() {
            UUID id = UUID.randomUUID();
            LocalDateTime registeredAt = LocalDateTime.now();
            int batteryStatus = 90;
            BatteryDto batteryDto = BatteryDto.builder()
                    .registeredAt(registeredAt)
                    .batteryStatus(batteryStatus)
                    .build();
            Battery battery = BatteryModelMapper.toModel(batteryDto);
            when(batteryRepository.getBatteryById(id)).thenReturn(Optional.of(battery));

            BatteryDto result = batteryService.getBatteryById(id);

            assertThat(result)
                    .isEqualTo(batteryDto);
        }

        @Test
        void givenInvalidId_whenGetBatteryById_thenThrowResourceNotFoundException() {
            UUID invalidId = UUID.randomUUID();
            when(batteryRepository.getBatteryById(invalidId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> batteryService.getBatteryById(invalidId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage(String.format("Battery with id=%s not found.", invalidId));
        }

        @Test
        void givenValidTimestamp_whenGetBatteriesByDateOrTimestamp_thenReturnBattery() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            int batteryStatus = 90;
            BatteryDto batteryDto = BatteryDto.builder()
                    .registeredAt(registeredAt)
                    .batteryStatus(batteryStatus)
                    .build();
            List<BatteryDto> batteryDtos = List.of(batteryDto);
            List<Battery> batteries = List.of(BatteryModelMapper.toModel(batteryDto));
            when(batteryRepository.getBatteryByTimestamp(registeredAt)).thenReturn(batteries);

            List<BatteryDto> result = batteryService.getBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository, times(1)).getBatteryByTimestamp(registeredAt);
            assertThat(result)
                    .containsExactlyElementsOf(batteryDtos);
        }

        @Test
        void givenInvalidTimestamp_whenGetBatteriesByDateOrTimestamp_thenReturnEmptyList() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(batteryRepository.getBatteryByTimestamp(registeredAt)).thenReturn(emptyList());

            List<BatteryDto> result = batteryService.getBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository, times(1)).getBatteryByTimestamp(registeredAt);
            assertThat(result)
                    .isEmpty();
        }

        @Test
        void givenValidDate_whenGetBatteriesByDateOrTimestamp_thenReturnBatteries() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            int batteryStatus = 90;
            int batteryStatus2 = 95;
            BatteryDto batteryDto = BatteryDto.builder()
                    .registeredAt(registeredAt)
                    .batteryStatus(batteryStatus)
                    .build();
            BatteryDto batteryDto2 = BatteryDto.builder()
                    .registeredAt(registeredAt.minusHours(1))
                    .batteryStatus(batteryStatus2)
                    .build();
            List<BatteryDto> batteryDtos = List.of(batteryDto, batteryDto2);
            List<Battery> batteries = batteryDtos.stream()
                    .map(BatteryModelMapper::toModel)
                    .toList();
            when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())).thenReturn(batteries);

            List<BatteryDto> result = batteryService.getBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository, times(1)).getBatteriesByDate(registeredAt.toLocalDate());
            assertThat(result)
                    .containsExactlyElementsOf(batteryDtos);
        }

        @Test
        void givenInvalidDate_whenGetBatteriesByDateOrTimestamp_thenReturnEmptyList() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

            List<BatteryDto> result = batteryService.getBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository, times(1)).getBatteriesByDate(registeredAt.toLocalDate());
            assertThat(result)
                    .isEmpty();
        }
    }

    @DisplayName("Create methods for BatteryServiceImpl.")
    @Nested
    class CreateMethods {
        @Test
        void givenValidBatteryModel_whenCreateBattery_thenReturnCreatedBattery() {
            LocalDateTime registeredAt = LocalDateTime.now();
            int batteryStatus = 90;
            BatteryDto batteryDto = BatteryDto.builder()
                    .registeredAt(registeredAt)
                    .batteryStatus(batteryStatus)
                    .build();
            Optional<Battery> battery = Optional.of(new Battery(registeredAt, batteryStatus));
            when(batteryRepository.createBattery(any())).thenReturn(battery);

            BatteryDto result = batteryService.createBattery(batteryDto);

            verify(batteryRepository, times(1)).createBattery(batteryArgumentCaptor.capture());
            assertThat(result)
                    .isEqualTo(batteryDto);
        }

        @Test
        void givenInvalidBatteryModel_whenCreateBattery_thenThrowResourceNotCreatedException() {
            assertThatThrownBy(() -> batteryService.createBattery(null))
                    .isInstanceOf(ResourceNotCreatedException.class)
                    .hasMessage("Battery cannot be created.");
        }
    }

    @DisplayName("Delete methods for BatteryServiceImpl.")
    @Nested
    class DeleteMethods {
        @Test
        void givenValidId_whenDeleteBatteryById_thenDeleteBattery(final CapturedOutput capturedOutput) {
            UUID id = UUID.randomUUID();
            LocalDateTime registeredAt = LocalDateTime.now();
            int batteryStatus = 90;
            BatteryDto batteryDto = BatteryDto.builder()
                    .registeredAt(registeredAt)
                    .batteryStatus(batteryStatus)
                    .build();
            Battery battery = BatteryModelMapper.toModel(batteryDto);
            when(batteryRepository.getBatteryById(id)).thenReturn(Optional.of(battery));
            doNothing().when(batteryRepository).deleteBatteryById(id);

            batteryService.deleteBatteryById(id);

            verify(batteryRepository, times(1)).getBatteryById(id);
            verify(batteryRepository, times(1)).deleteBatteryById(id);
            assertThat(capturedOutput)
                    .contains(String.format("Battery with id=%s deleted.", id));
        }

        @Test
        void givenInvalidId_whenDeleteBatteryById_thenThrowResourceNotFoundException() {
            UUID invalidId = UUID.randomUUID();
            when(batteryRepository.getBatteryById(invalidId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> batteryService.deleteBatteryById(invalidId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage(String.format("Battery with id=%s not found.", invalidId));
        }

        @Test
        void givenValidTimestamp_whenDeleteBatteryByDateOrTimestamp_thenDeleteBattery(final CapturedOutput capturedOutput) {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            int batteryStatus = 90;
            BatteryDto batteryDto = BatteryDto.builder()
                    .registeredAt(registeredAt)
                    .batteryStatus(batteryStatus)
                    .build();
            List<Battery> batteries = List.of(BatteryModelMapper.toModel(batteryDto));
            when(batteryRepository.getBatteryByTimestamp(registeredAt)).thenReturn(batteries);
            doNothing().when(batteryRepository).deleteBatteryByTimestamp(registeredAt);

            batteryService.deleteBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository, times(1)).getBatteryByTimestamp(registeredAt);
            verify(batteryRepository, times(1)).deleteBatteryByTimestamp(registeredAt);
            assertThat(capturedOutput)
                    .contains(String.format("Deleted battery with timestamp=%s.", batteries.getFirst().getRegisteredAt()));
        }

        @Test
        void givenInvalidTimestamp_whenDeleteBatteryByDateOrTimestamp_thenReturn(final CapturedOutput capturedOutput) {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(batteryRepository.getBatteryByTimestamp(registeredAt)).thenReturn(emptyList());

            batteryService.deleteBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository, times(1)).getBatteryByTimestamp(registeredAt);
            verify(batteryRepository, times(0)).deleteBatteryByTimestamp(registeredAt);
            assertThat(capturedOutput)
                    .contains(String.format("Batteries registeredAt=%s not found.", registeredAt));
        }

        @Test
        void givenValidDate_whenDeleteBatteryByDateOrTimestamp_thenDeleteBattery(final CapturedOutput capturedOutput) {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            int batteryStatus = 90;
            BatteryDto batteryDto = BatteryDto.builder()
                    .registeredAt(registeredAt)
                    .batteryStatus(batteryStatus)
                    .build();
            BatteryDto batteryDto2 = BatteryDto.builder()
                    .registeredAt(registeredAt.minusHours(1))
                    .batteryStatus(batteryStatus)
                    .build();
            List<BatteryDto> batteryDtos = List.of(batteryDto, batteryDto2);
            List<Battery> batteries = batteryDtos.stream()
                    .map(BatteryModelMapper::toModel)
                    .toList();
            when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())).thenReturn(batteries);
            doNothing().when(batteryRepository).deleteBatteriesByDate(registeredAt.toLocalDate());

            batteryService.deleteBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository, times(1)).getBatteriesByDate(registeredAt.toLocalDate());
            verify(batteryRepository, times(1)).deleteBatteriesByDate(registeredAt.toLocalDate());
            assertThat(capturedOutput)
                    .contains(String.format("Deleted batteries with date=%s.", batteries.getFirst().getRegisteredAt().toLocalDate()));
        }

        @Test
        void givenInvalidDate_whenDeleteBatteryByDateOrTimestamp_thenReturn(final CapturedOutput capturedOutput) {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

            batteryService.deleteBatteriesByDateOrTimestamp(registeredAt, dateOnly);

            verify(batteryRepository, times(1)).getBatteriesByDate(registeredAt.toLocalDate());
            verify(batteryRepository, times(0)).deleteBatteriesByDate(registeredAt.toLocalDate());
            assertThat(capturedOutput)
                    .contains(String.format("Batteries registeredAt=%s not found.", registeredAt));
        }
    }
}
