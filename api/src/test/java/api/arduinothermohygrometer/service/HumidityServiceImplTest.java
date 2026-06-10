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

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.HumidityModelMapper;
import api.arduinothermohygrometer.model.Humidity;
import api.arduinothermohygrometer.repository.HumidityRepository;
import api.arduinothermohygrometer.service.implementation.HumidityServiceImpl;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
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
            LocalDateTime registeredAt = LocalDateTime.now();
            Double airHumidity = 70.00;
            HumidityDto humidityDto = HumidityDto.builder()
                    .registeredAt(registeredAt)
                    .airHumidity(airHumidity)
                    .build();
            Humidity humidity = HumidityModelMapper.toModel(humidityDto);
            when(humidityRepository.getHumidityById(id)).thenReturn(Optional.of(humidity));

            HumidityDto result = humidityService.getHumidityById(id);

            assertThat(result)
                    .isEqualTo(humidityDto);
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
            Double airHumidity = 75.00;
            HumidityDto humidityDto = HumidityDto.builder()
                    .registeredAt(registeredAt)
                    .airHumidity(airHumidity)
                    .build();
            List<HumidityDto> humidityDtos = List.of(humidityDto);
            List<Humidity> humidities = List.of(HumidityModelMapper.toModel(humidityDto));
            when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(humidities);

            List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository).getHumidityByTimestamp(registeredAt);
            assertThat(result)
                    .containsExactlyElementsOf(humidityDtos);
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
            Double airHumidity = 75.00;
            Double airHumidity2 = 80.00;
            List<HumidityDto> humidityDtos = List.of(
                    HumidityDto.builder().registeredAt(registeredAt).airHumidity(airHumidity).build(),
                    HumidityDto.builder().registeredAt(registeredAt.minusHours(1)).airHumidity(airHumidity2).build());
            List<Humidity> humidities = humidityDtos.stream()
                    .map(HumidityModelMapper::toModel)
                    .toList();
            when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate())).thenReturn(humidities);

            List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository).getHumiditiesByDate(registeredAt.toLocalDate());
            assertThat(result)
                    .containsExactlyElementsOf(humidityDtos);
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
            LocalDateTime registeredAt = LocalDateTime.now();
            Double airHumidity = 75.00;
            HumidityDto humidityDto = HumidityDto.builder()
                    .registeredAt(registeredAt)
                    .airHumidity(airHumidity)
                    .build();
            Humidity humidity = new Humidity(registeredAt, airHumidity);
            ReflectionTestUtils.setField(humidity, "id", UUID.randomUUID());
            when(humidityRepository.createHumidity(any(Humidity.class))).thenReturn(Optional.of(humidity));

            HumidityDto result = humidityService.createHumidity(humidityDto);

            verify(humidityRepository).createHumidity(any(Humidity.class));
            assertThat(result.getId()).isEqualTo(humidity.getId());
            assertThat(result.getRegisteredAt()).isEqualTo(humidityDto.getRegisteredAt());
            assertThat(result.getAirHumidity()).isEqualTo(humidityDto.getAirHumidity());
        }

        @Test
        void givenInvalidHumidityModel_whenCreateHumidity_thenThrowResourceNotCreatedException() {
            LocalDateTime registeredAt = LocalDateTime.now();
            Double airHumidity = 105.00;
            HumidityDto humidityDto = HumidityDto.builder()
                    .registeredAt(registeredAt)
                    .airHumidity(airHumidity)
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
            LocalDateTime registeredAt = LocalDateTime.now();
            Double airHumidity = 75.00;
            HumidityDto humidityDto = HumidityDto.builder()
                    .registeredAt(registeredAt)
                    .airHumidity(airHumidity)
                    .build();
            Humidity humidity = HumidityModelMapper.toModel(humidityDto);
            when(humidityRepository.getHumidityById(id)).thenReturn(Optional.of(humidity));
            doNothing().when(humidityRepository).deleteHumidityById(id);

            humidityService.deleteHumidityById(id);

            verify(humidityRepository).getHumidityById(id);
            verify(humidityRepository).deleteHumidityById(id);
            assertThat(capturedOutput)
                    .contains("Humidity with id=%s deleted.".formatted(id));
            ;
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
            Double airHumidity = 75.00;
            HumidityDto humidityDto = HumidityDto.builder()
                    .registeredAt(registeredAt)
                    .airHumidity(airHumidity)
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
            Double airHumidity = 75.00;
            HumidityDto humidityDto = HumidityDto.builder()
                    .registeredAt(registeredAt)
                    .airHumidity(airHumidity)
                    .build();
            List<Humidity> humidities = List.of(HumidityModelMapper.toModel(humidityDto));
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
