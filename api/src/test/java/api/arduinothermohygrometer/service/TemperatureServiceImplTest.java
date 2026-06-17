package api.arduinothermohygrometer.service;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.TemperatureModelMapper;
import api.arduinothermohygrometer.model.Temperature;
import api.arduinothermohygrometer.repository.TemperatureRepository;
import api.arduinothermohygrometer.service.implementation.TemperatureServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.OutputCaptureExtension;
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

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
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
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            TemperatureDto temperatureDto = createTemperatureDto(registeredAt, 70.00);
            Temperature temperature = TemperatureModelMapper.toModel(temperatureDto);
            when(temperatureRepository.getTemperatureById(id)).thenReturn(Optional.of(temperature));

            TemperatureDto result = temperatureService.getTemperatureById(id);

            assertThat(result.getId()).isEqualTo(temperatureDto.getId());
            assertThat(result.getRegisteredAt()).isEqualTo(temperatureDto.getRegisteredAt());
            assertThat(result.getTemp()).isEqualTo(temperatureDto.getTemp());
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
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            TemperatureDto temperatureDto = createTemperatureDto(registeredAt, 70.00);
            List<Temperature> temperatures = List.of(TemperatureModelMapper.toModel(temperatureDto));
            when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(temperatures);

            List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperatureByTimestamp(registeredAt);
            assertThat(result)
                .hasSize(1)
                .first()
                .satisfies(temperature -> {
                    assertThat(temperature.getId()).isEqualTo(temperatureDto.getId());
                    assertThat(temperature.getRegisteredAt()).isEqualTo(temperatureDto.getRegisteredAt());
                    assertThat(temperature.getTemp()).isEqualTo(temperatureDto.getTemp());
                });
        }

        @Test
        void givenInvalidTimestamp_whenGetTemperaturesByDateOrTimestamp_thenReturnEmptyList() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(emptyList());

            List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperatureByTimestamp(registeredAt);
            assertThat(result)
                .isEmpty();
        }

        @Test
        void givenValidDate_whenGetTemperaturesByDateOrTimestamp_thenReturnTemperatures() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            List<TemperatureDto> temperatureDtos = List.of(
                createTemperatureDto(registeredAt, 70.00),
                createTemperatureDto(registeredAt, 65.00)
            );
            List<Temperature> temperatures = temperatureDtos.stream()
                                                            .map(TemperatureModelMapper::toModel)
                                                            .toList();
            when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())).thenReturn(temperatures);

            List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperaturesByDate(registeredAt.toLocalDate());
            assertThat(result)
                .hasSize(2)
                .first()
                .satisfies(temperature -> {
                    assertThat(temperature.getId()).isEqualTo(temperatureDtos.getFirst().getId());
                    assertThat(temperature.getRegisteredAt()).isEqualTo(temperatureDtos.getFirst().getRegisteredAt());
                    assertThat(temperature.getTemp()).isEqualTo(temperatureDtos.getFirst().getTemp());
                });
        }

        @Test
        void givenInvalidDate_whenGetTemperaturesByDate_thenReturnEmptyList() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
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
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            TemperatureDto temperatureDto = createTemperatureDto(registeredAt, 70.00);
            Temperature temperature = new Temperature(registeredAt, 70.00);
            ReflectionTestUtils.setField(temperature, "id", UUID.randomUUID());
            when(temperatureRepository.createTemperature(any(Temperature.class))).thenReturn(Optional.of(temperature));

            TemperatureDto result = temperatureService.createTemperature(temperatureDto);

            verify(temperatureRepository).createTemperature(any(Temperature.class));
            assertThat(result.getId()).isNotNull();
            assertThat(result.getRegisteredAt()).isEqualTo(temperatureDto.getRegisteredAt());
            assertThat(result.getTemp()).isEqualTo(temperatureDto.getTemp());
        }

        @Test
        void givenInvalidTemperatureModel_whenCreateTemperature_thenThrowResourceNotCreatedException() {
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            TemperatureDto temperatureDto = createTemperatureDto(registeredAt, 70.00);
            when(temperatureRepository.createTemperature(any(Temperature.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> temperatureService.createTemperature(temperatureDto))
                .isInstanceOf(ResourceNotCreatedException.class)
                .hasMessage("Temperature cannot be created.");
        }
    }

    @Nested
    class DeleteMethods {
        @Test
        void givenValidId_whenDeleteTemperatureById_thenDeleteTemperature() {
            UUID id = UUID.randomUUID();
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            TemperatureDto temperatureDto = createTemperatureDto(registeredAt, 70.00);
            Temperature temperature = TemperatureModelMapper.toModel(temperatureDto);
            when(temperatureRepository.getTemperatureById(id)).thenReturn(Optional.of(temperature));

            temperatureService.deleteTemperatureById(id);

            verify(temperatureRepository).getTemperatureById(id);
            verify(temperatureRepository).deleteTemperatureById(id);
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
        void givenValidTimestamp_whenDeleteTemperaturesByDateOrTimestamp_thenDeleteTemperature() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            TemperatureDto temperatureDto = createTemperatureDto(registeredAt, 70.00);
            List<Temperature> temperatures = List.of(TemperatureModelMapper.toModel(temperatureDto));
            when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(temperatures);

            temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperatureByTimestamp(registeredAt);
            verify(temperatureRepository).deleteTemperatureByTimestamp(registeredAt);
        }

        @Test
        void givenInvalidTimestamp_whenDeleteTemperaturesByDateOrTimestamp_thenReturn() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
            when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(emptyList());

            temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperatureByTimestamp(registeredAt);
            verify(temperatureRepository, times(0)).deleteTemperatureByTimestamp(registeredAt);
        }

        @Test
        void givenValidDate_whenDeleteTemperaturesByDateOrTimestamp_thenDeleteTemperature() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            List<TemperatureDto> temperatureDtos = List.of(
                createTemperatureDto(registeredAt, 70.00),
                createTemperatureDto(registeredAt, 65.00)
            );
            List<Temperature> temperatures = temperatureDtos.stream()
                                                            .map(TemperatureModelMapper::toModel)
                                                            .toList();
            when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())).thenReturn(temperatures);

            temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperaturesByDate(registeredAt.toLocalDate());
            verify(temperatureRepository).deleteTemperaturesByDate(registeredAt.toLocalDate());
        }

        @Test
        void givenInvalidDate_whenDeleteTemperaturesByDateOrTimestamp_thenReturn() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

            temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            verify(temperatureRepository).getTemperaturesByDate(registeredAt.toLocalDate());
            verify(temperatureRepository, times(0)).deleteTemperaturesByDate(registeredAt.toLocalDate());
        }
    }

    private TemperatureDto createTemperatureDto(LocalDateTime registeredAt, Double temp) {
        return TemperatureDto.builder()
                             .registeredAt(registeredAt)
                             .temp(temp)
                             .build();
    }
}
