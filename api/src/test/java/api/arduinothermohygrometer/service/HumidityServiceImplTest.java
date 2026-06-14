package api.arduinothermohygrometer.service;

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

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.HumidityModelMapper;
import api.arduinothermohygrometer.model.Humidity;
import api.arduinothermohygrometer.repository.HumidityRepository;
import api.arduinothermohygrometer.service.implementation.HumidityServiceImpl;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class HumidityServiceImplTest {
    @Mock
    private HumidityRepository humidityRepository;

    @InjectMocks
    private HumidityServiceImpl humidityService;

    @Nested
    class GetMethods {
        @Test
        void givenValidId_whenGetHumidityById_thenReturnHumidity() {
            UUID id = UUID.randomUUID();
            HumidityDto humidityDto = HumidityDto.builder()
                                                 .registeredAt(LocalDateTime.now())
                                                 .airHumidity(70.00)
                                                 .build();
            Humidity humidity = HumidityModelMapper.toModel(humidityDto);
            when(humidityRepository.getHumidityById(id)).thenReturn(Optional.of(humidity));

            HumidityDto result = humidityService.getHumidityById(id);

            assertThat(result.getId()).isEqualTo(humidityDto.getId());
            assertThat(result.getRegisteredAt()).isEqualTo(humidityDto.getRegisteredAt());
            assertThat(result.getAirHumidity()).isEqualTo(humidityDto.getAirHumidity());
        }

        @Test
        void givenInvalidId_whenGetHumidityById_thenThrowResourceNotFoundException() {
            UUID invalidId = UUID.randomUUID();
            when(humidityRepository.getHumidityById(invalidId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> humidityService.getHumidityById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Humidity with id=%s not found.".formatted(invalidId));
        }

        @Test
        void givenValidTimestamp_whenGetHumiditiesByDateOrTimestamp_thenReturnHumidity() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            HumidityDto humidityDto = HumidityDto.builder()
                                                 .registeredAt(registeredAt)
                                                 .airHumidity(75.00)
                                                 .build();
            List<Humidity> humidities = List.of(HumidityModelMapper.toModel(humidityDto));
            when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(humidities);

            List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository).getHumidityByTimestamp(registeredAt);
            assertThat(result)
                .hasSize(1)
                .first()
                .satisfies(humidity -> {
                    assertThat(humidity.getId()).isEqualTo(humidityDto.getId());
                    assertThat(humidity.getRegisteredAt()).isEqualTo(humidityDto.getRegisteredAt());
                    assertThat(humidity.getAirHumidity()).isEqualTo(humidityDto.getAirHumidity());
                });
        }

        @Test
        void givenInvalidTimestamp_whenGetHumiditiesByDateOrTimestamp_thenReturnEmptyList() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(emptyList());

            List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository).getHumidityByTimestamp(registeredAt);
            assertThat(result)
                .isEmpty();
        }

        @Test
        void givenValidDate_whenGetHumiditiesByDateOrTimestamp_thenReturnHumidities() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            List<HumidityDto> humidityDtos = List.of(
                HumidityDto.builder().registeredAt(registeredAt).airHumidity(75.00).build(),
                HumidityDto.builder().registeredAt(registeredAt.minusHours(1)).airHumidity(80.00).build()
            );
            List<Humidity> humidities = humidityDtos.stream()
                                                    .map(HumidityModelMapper::toModel)
                                                    .toList();
            when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate())).thenReturn(humidities);

            List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository).getHumiditiesByDate(registeredAt.toLocalDate());
            assertThat(result)
                .hasSize(2)
                .first()
                .satisfies(humidity -> {
                    assertThat(humidity.getId()).isEqualTo(humidityDtos.getFirst().getId());
                    assertThat(humidity.getRegisteredAt()).isEqualTo(humidityDtos.getFirst().getRegisteredAt());
                    assertThat(humidity.getAirHumidity()).isEqualTo(humidityDtos.getFirst().getAirHumidity());
                });
        }

        @Test
        void givenInvalidDate_whenGetHumiditiesByDate_thenReturnEmptyList() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

            List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository).getHumiditiesByDate(registeredAt.toLocalDate());
            assertThat(result)
                .isEmpty();
        }
    }

    @Nested
    class CreateMethods {
        @Test
        void givenValidHumidityModel_whenCreateHumidity_thenReturnCreatedHumidity() {
            HumidityDto humidityDto = HumidityDto.builder()
                                                 .registeredAt(LocalDateTime.now())
                                                 .airHumidity(75.00)
                                                 .build();
            Humidity humidity = new Humidity(LocalDateTime.now(), 75.00);
            ReflectionTestUtils.setField(humidity, "id", UUID.randomUUID());
            when(humidityRepository.createHumidity(any(Humidity.class))).thenReturn(Optional.of(humidity));

            HumidityDto result = humidityService.createHumidity(humidityDto);

            verify(humidityRepository).createHumidity(any(Humidity.class));
            assertThat(result.getId()).isNotNull();
            assertThat(result.getRegisteredAt()).isEqualTo(humidityDto.getRegisteredAt());
            assertThat(result.getAirHumidity()).isEqualTo(humidityDto.getAirHumidity());
        }

        @Test
        void givenInvalidHumidityModel_whenCreateHumidity_thenThrowResourceNotCreatedException() {
            HumidityDto humidityDto = HumidityDto.builder()
                                                 .registeredAt(LocalDateTime.now())
                                                 .airHumidity(105.00)
                                                 .build();
            when(humidityRepository.createHumidity(any(Humidity.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> humidityService.createHumidity(humidityDto))
                .isInstanceOf(ResourceNotCreatedException.class)
                .hasMessage("Humidity cannot be created.");
        }
    }

    @Nested
    class DeleteMethods {
        @Test
        void givenValidId_whenDeleteHumidityById_thenDeleteHumidity(final CapturedOutput capturedOutput) {
            UUID id = UUID.randomUUID();
            HumidityDto humidityDto = HumidityDto.builder()
                                                 .registeredAt(LocalDateTime.now())
                                                 .airHumidity(75.00)
                                                 .build();
            Humidity humidity = HumidityModelMapper.toModel(humidityDto);
            when(humidityRepository.getHumidityById(id)).thenReturn(Optional.of(humidity));
            doNothing().when(humidityRepository).deleteHumidityById(id);

            humidityService.deleteHumidityById(id);

            verify(humidityRepository).getHumidityById(id);
            verify(humidityRepository).deleteHumidityById(id);
            assertThat(capturedOutput)
                .contains("Humidity with id=%s deleted.".formatted(id));
        }

        @Test
        void givenInvalidId_whenDeleteHumidityById_thenThrowResourceNotFoundException() {
            UUID invalidId = UUID.randomUUID();
            when(humidityRepository.getHumidityById(invalidId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> humidityService.deleteHumidityById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Humidity with id=%s not found.".formatted(invalidId));
        }

        @Test
        void givenValidTimestamp_whenDeleteHumiditiesByTimestamp_thenDeleteHumidity(final CapturedOutput capturedOutput) {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            HumidityDto humidityDto = HumidityDto.builder()
                                                 .registeredAt(registeredAt)
                                                 .airHumidity(75.00)
                                                 .build();
            List<Humidity> humidities = List.of(HumidityModelMapper.toModel(humidityDto));
            when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(humidities);
            doNothing().when(humidityRepository).deleteHumidityByTimestamp(registeredAt);

            humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository).getHumidityByTimestamp(registeredAt);
            verify(humidityRepository).deleteHumidityByTimestamp(registeredAt);
            assertThat(capturedOutput)
                .contains("Deleted humidity with timestamp=%s.".formatted(humidities.getFirst().getRegisteredAt()));
        }

        @Test
        void givenInvalidTimestamp_whenDeleteHumiditiesByDateOrTimestamp_thenReturn(final CapturedOutput capturedOutput) {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(emptyList());

            humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository).getHumidityByTimestamp(registeredAt);
            verify(humidityRepository, times(0)).deleteHumidityByTimestamp(registeredAt);
            assertThat(capturedOutput)
                .contains("Humidities registeredAt=%s not found.".formatted(registeredAt));
        }

        @Test
        void givenValidDate_whenDeleteHumiditiesByDateOrTimestamp_thenDeleteHumidity(final CapturedOutput capturedOutput) {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            List<HumidityDto> humidityDtos = List.of(
                HumidityDto.builder().registeredAt(registeredAt).airHumidity(75.00).build(),
                HumidityDto.builder().registeredAt(registeredAt).airHumidity(70.00).build()
            );
            List<Humidity> humidities = humidityDtos.stream()
                                                    .map(HumidityModelMapper::toModel)
                                                    .toList();
            when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate())).thenReturn(humidities);
            doNothing().when(humidityRepository).deleteHumiditiesByDate(registeredAt.toLocalDate());

            humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository).getHumiditiesByDate(registeredAt.toLocalDate());
            verify(humidityRepository).deleteHumiditiesByDate(registeredAt.toLocalDate());
            assertThat(capturedOutput)
                .contains("Deleted humidities with date=%s.".formatted(humidities.getFirst().getRegisteredAt().toLocalDate()));
        }

        @Test
        void givenInvalidDate_whenDeleteHumiditiesByDateOrTimestamp_thenReturn(final CapturedOutput capturedOutput) {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

            humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository).getHumiditiesByDate(registeredAt.toLocalDate());
            verify(humidityRepository, times(0)).deleteHumiditiesByDate(registeredAt.toLocalDate());
            assertThat(capturedOutput)
                .contains("Humidities registeredAt=%s not found.".formatted(registeredAt));
        }
    }
}
