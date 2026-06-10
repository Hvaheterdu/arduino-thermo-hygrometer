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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.util.ReflectionTestUtils;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.TemperatureModelMapper;
import api.arduinothermohygrometer.model.Temperature;
import api.arduinothermohygrometer.repository.TemperatureRepository;
import api.arduinothermohygrometer.service.implementation.TemperatureServiceImpl;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
class TemperatureServiceImplTest {
    @Mock
    private TemperatureRepository temperatureRepository;

    @InjectMocks
    private TemperatureServiceImpl temperatureService;

    @Nested
    class GetMethods {
        @Test
        void givenValidId_whenGetTemperatureById_thenReturnTemperature() {
            UUID id = UUID.randomUUID();
            LocalDateTime registeredAt = LocalDateTime.now();
            Double temp = 70.00;
            TemperatureDto temperatureDto = TemperatureDto.builder()
                    .registeredAt(registeredAt)
                    .temp(temp)
                    .build();
            Temperature temperature = TemperatureModelMapper.toModel(temperatureDto);
            when(temperatureRepository.getTemperatureById(id)).thenReturn(Optional.of(temperature));

            TemperatureDto result = temperatureService.getTemperatureById(id);

            assertThat(result)
                    .isEqualTo(temperatureDto);
        }

        @Test
        void givenInvalidId_whenGetTemperatureById_thenThrowResourceNotFoundException() {
            UUID invalidId = UUID.randomUUID();
            when(temperatureRepository.getTemperatureById(invalidId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> temperatureService.getTemperatureById(invalidId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Temperature with id=%s not found.".formatted(invalidId));
        }

        @Test
        void givenValidTimestamp_whenGetTemperaturesByDateOrTimestamp_thenReturnTemperature() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            Double temp = 75.00;
            TemperatureDto temperatureDto = TemperatureDto.builder()
                    .registeredAt(registeredAt)
                    .temp(temp)
                    .build();
            List<TemperatureDto> temperatureDtos = List.of(temperatureDto);
            List<Temperature> temperatures = List.of(TemperatureModelMapper.toModel(temperatureDto));
            when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(temperatures);

            List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperatureByTimestamp(registeredAt);
            assertThat(result)
                    .containsExactlyElementsOf(temperatureDtos);
        }

        @Test
        void givenInvalidTimestamp_whenGetTemperaturesByDateOrTimestamp_thenReturnEmptyList() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(emptyList());

            List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperatureByTimestamp(registeredAt);
            assertThat(result)
                    .isEmpty();
        }

        @Test
        void givenValidDate_whenGetTemperaturesByDateOrTimestamp_thenReturnTemperatures() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            Double temp = 75.00;
            Double temp2 = 80.00;
            List<TemperatureDto> temperatureDtos = List.of(
                    TemperatureDto.builder().registeredAt(registeredAt).temp(temp).build(),
                    TemperatureDto.builder().registeredAt(registeredAt.minusHours(1)).temp(temp2).build());
            List<Temperature> temperatures = temperatureDtos.stream()
                    .map(TemperatureModelMapper::toModel)
                    .toList();
            when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())).thenReturn(temperatures);

            List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperaturesByDate(registeredAt.toLocalDate());
            assertThat(result)
                    .containsExactlyElementsOf(temperatureDtos);
        }

        @Test
        void givenInvalidDate_whenGetTemperaturesByDate_thenReturnEmptyList() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

            List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperaturesByDate(registeredAt.toLocalDate());
            assertThat(result)
                    .isEmpty();
        }
    }

    @Nested
    class CreateMethods {
        @Test
        void givenValidTemperatureModel_whenCreateTemperature_thenReturnCreatedTemperature() {
            LocalDateTime registeredAt = LocalDateTime.now();
            Double temp = 75.00;
            TemperatureDto temperatureDto = TemperatureDto.builder()
                    .registeredAt(registeredAt)
                    .temp(temp)
                    .build();
            Temperature temperature = new Temperature(registeredAt, temp);
            ReflectionTestUtils.setField(temperature, "id", UUID.randomUUID());
            when(temperatureRepository.createTemperature(any(Temperature.class))).thenReturn(Optional.of(temperature));

            TemperatureDto result = temperatureService.createTemperature(temperatureDto);

            verify(temperatureRepository).createTemperature(any(Temperature.class));
            assertThat(result.getId()).isEqualTo(temperature.getId());
            assertThat(result.getRegisteredAt()).isEqualTo(temperatureDto.getRegisteredAt());
            assertThat(result.getTemp()).isEqualTo(temperatureDto.getTemp());
        }

        @Test
        void givenInvalidTemperatureModel_whenCreateTemperature_thenThrowResourceNotCreatedException() {
            LocalDateTime registeredAt = LocalDateTime.now();
            Double temp = 150.00;
            TemperatureDto temperatureDto = TemperatureDto.builder()
                    .registeredAt(registeredAt)
                    .temp(temp)
                    .build();
            when(temperatureRepository.createTemperature(any(Temperature.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> temperatureService.createTemperature(temperatureDto))
                    .isInstanceOf(ResourceNotCreatedException.class)
                    .hasMessage("Temperature cannot be created.");
        }
    }

    @Nested
    class DeleteMethods {
        @Test
        void givenValidId_whenDeleteTemperatureById_thenDeleteTemperature(final CapturedOutput capturedOutput) {
            UUID id = UUID.randomUUID();
            LocalDateTime registeredAt = LocalDateTime.now();
            Double temp = 75.00;
            TemperatureDto temperatureDto = TemperatureDto.builder()
                    .registeredAt(registeredAt)
                    .temp(temp)
                    .build();
            Temperature temperature = TemperatureModelMapper.toModel(temperatureDto);
            when(temperatureRepository.getTemperatureById(id)).thenReturn(Optional.of(temperature));
            doNothing().when(temperatureRepository).deleteTemperatureById(id);

            temperatureService.deleteTemperatureById(id);

            verify(temperatureRepository).getTemperatureById(id);
            verify(temperatureRepository).deleteTemperatureById(id);
            assertThat(capturedOutput)
                    .contains("Temperature with id=%s deleted.".formatted(id));
        }

        @Test
        void givenInvalidId_whenDeleteTemperatureById_thenThrowResourceNotFoundException() {
            UUID invalidId = UUID.randomUUID();
            when(temperatureRepository.getTemperatureById(invalidId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> temperatureService.deleteTemperatureById(invalidId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Temperature with id=%s not found.".formatted(invalidId));
        }

        @Test
        void givenValidTimestamp_whenDeleteTemperaturesByDateOrTimestamp_thenDeleteTemperature(final CapturedOutput capturedOutput) {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            Double temp = 75.00;
            TemperatureDto temperatureDto = TemperatureDto.builder()
                    .registeredAt(registeredAt)
                    .temp(temp)
                    .build();
            List<Temperature> temperatures = List.of(TemperatureModelMapper.toModel(temperatureDto));
            when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(temperatures);
            doNothing().when(temperatureRepository).deleteTemperatureByTimestamp(registeredAt);

            temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperatureByTimestamp(registeredAt);
            verify(temperatureRepository).deleteTemperatureByTimestamp(registeredAt);
            assertThat(capturedOutput)
                    .contains("Deleted temperature with timestamp=%s.".formatted(temperatures.getFirst().getRegisteredAt()));
        }

        @Test
        void givenInvalidTimestamp_whenDeleteTemperaturesByDateOrTimestamp_thenReturn(final CapturedOutput capturedOutput) {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(emptyList());

            temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperatureByTimestamp(registeredAt);
            verify(temperatureRepository, times(0)).deleteTemperatureByTimestamp(registeredAt);
            assertThat(capturedOutput)
                    .contains("Temperatures registeredAt=%s not found.".formatted(registeredAt));
        }

        @Test
        void givenValidDate_whenDeleteTemperaturesByDateOrTimestamp_thenDeleteTemperature(final CapturedOutput capturedOutput) {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            Double temp = 75.00;
            TemperatureDto temperatureDto = TemperatureDto.builder()
                    .registeredAt(registeredAt)
                    .temp(temp)
                    .build();
            List<Temperature> temperatures = List.of(TemperatureModelMapper.toModel(temperatureDto));
            when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())).thenReturn(temperatures);
            doNothing().when(temperatureRepository).deleteTemperaturesByDate(registeredAt.toLocalDate());

            temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperaturesByDate(registeredAt.toLocalDate());
            verify(temperatureRepository).deleteTemperaturesByDate(registeredAt.toLocalDate());
            assertThat(capturedOutput)
                    .contains("Deleted temperatures with date=%s.".formatted(temperatures.getFirst().getRegisteredAt().toLocalDate()));
        }

        @Test
        void givenInvalidDate_whenDeleteTemperaturesByDateOrTimestamp_thenReturn(final CapturedOutput capturedOutput) {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

            temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperaturesByDate(registeredAt.toLocalDate());
            verify(temperatureRepository, times(0)).deleteTemperaturesByDate(registeredAt.toLocalDate());
            assertThat(capturedOutput)
                    .contains("Temperatures registeredAt=%s not found.".formatted(registeredAt));
        }
    }
}
