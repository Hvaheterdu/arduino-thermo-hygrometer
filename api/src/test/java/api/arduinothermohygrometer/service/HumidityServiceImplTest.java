package api.arduinothermohygrometer.service;

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

@DisplayName("HumidityServiceImpl unit tests.")
@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class HumidityServiceImplTest {
    @Mock
    private HumidityRepository humidityRepository;

    @Captor
    private ArgumentCaptor<Humidity> humidityArgumentCaptor = ArgumentCaptor.forClass(Humidity.class);

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
                .hasMessage(String.format("Humidity with id=%s not found.", invalidId));
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

            verify(humidityRepository, times(1)).getHumidityByTimestamp(registeredAt);
            assertThat(result)
                .containsExactlyElementsOf(humidityDtos);
        }

        @Test
        void givenInvalidTimestamp_whenGetHumiditiesByDateOrTimestamp_thenReturnEmptyList() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(emptyList());

            List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository, times(1)).getHumidityByTimestamp(registeredAt);
            assertThat(result)
                .isEmpty();
        }

        @Test
        void givenValidDate_whenGetHumiditiesByDateOrTimestamp_thenReturnHumidities() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            Double airHumidity = 75.00;
            Double airHumidity2 = 80.00;
            HumidityDto humidityDto = HumidityDto.builder()
                                                 .registeredAt(registeredAt)
                                                 .airHumidity(airHumidity)
                                                 .build();
            HumidityDto humidityDto2 = HumidityDto.builder()
                                                  .registeredAt(registeredAt.minusHours(1))
                                                  .airHumidity(airHumidity2)
                                                  .build();
            List<HumidityDto> humidityDtos = List.of(humidityDto, humidityDto2);
            List<Humidity> humidities = humidityDtos.stream()
                                                    .map(HumidityModelMapper::toModel)
                                                    .toList();
            when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate())).thenReturn(humidities);

            List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository, times(1)).getHumiditiesByDate(registeredAt.toLocalDate());
            assertThat(result)
                .containsExactlyElementsOf(humidityDtos);
        }

        @Test
        void givenInvalidDate_whenGetHumiditiesByDate_thenReturnEmptyList() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

            List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository, times(1)).getHumiditiesByDate(registeredAt.toLocalDate());
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
            Optional<Humidity> humidity = Optional.of(new Humidity(registeredAt, airHumidity));
            when(humidityRepository.createHumidity(any())).thenReturn(humidity);

            HumidityDto result = humidityService.createHumidity(humidityDto);

            verify(humidityRepository, times(1)).createHumidity(humidityArgumentCaptor.capture());
            assertThat(result)
                .isEqualTo(humidityDto);
        }

        @Test
        void givenInvalidHumidityModel_whenCreateHumidity_thenThrowResourceNotCreatedException() {
            assertThatThrownBy(() -> humidityService.createHumidity(null))
                .isInstanceOf(ResourceNotCreatedException.class)
                .hasMessage("Humidity cannot be created.");
        }
    }

    @Nested
    class DeleteMethods {
        @Test
        void givenValidId_whenDeleteHumidityById_thenDeleteHumidity(CapturedOutput capturedOutput) {
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

            verify(humidityRepository, times(1)).getHumidityById(id);
            verify(humidityRepository, times(1)).deleteHumidityById(id);
            assertThat(capturedOutput)
                .contains(String.format("Humidity with id=%s deleted.", id));
        }

        @Test
        void givenInvalidId_whenDeleteHumidityById_thenThrowResourceNotFoundException() {
            UUID invalidId = UUID.randomUUID();
            when(humidityRepository.getHumidityById(invalidId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> humidityService.deleteHumidityById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Humidity with id=%s not found.", invalidId));
        }

        @Test
        void givenValidTimestamp_whenDeleteHumiditiesByTimestamp_thenDeleteHumidity(CapturedOutput capturedOutput) {
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

            verify(humidityRepository, times(1)).getHumidityByTimestamp(registeredAt);
            verify(humidityRepository, times(1)).deleteHumidityByTimestamp(registeredAt);
            assertThat(capturedOutput)
                .contains(String.format("Deleted humidity with timestamp=%s.", humidities.getFirst().getRegisteredAt()));
        }

        @Test
        void givenInvalidTimestamp_whenDeleteHumiditiesByDateOrTimestamp_thenReturn(CapturedOutput capturedOutput) {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(emptyList());

            humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository, times(1)).getHumidityByTimestamp(registeredAt);
            verify(humidityRepository, times(0)).deleteHumidityByTimestamp(registeredAt);
            assertThat(capturedOutput)
                .contains(String.format("Humidities registeredAt=%s not found.", registeredAt));
        }

        @Test
        void givenValidDate_whenDeleteHumiditiesByDateOrTimestamp_thenDeleteHumidity(CapturedOutput capturedOutput) {
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

            verify(humidityRepository, times(1)).getHumiditiesByDate(registeredAt.toLocalDate());
            verify(humidityRepository, times(1)).deleteHumiditiesByDate(registeredAt.toLocalDate());
            assertThat(capturedOutput)
                .contains(String.format("Deleted humidities with date=%s.", humidities.getFirst().getRegisteredAt().toLocalDate()));
        }

        @Test
        void givenInvalidDate_whenDeleteHumiditiesByDateOrTimestamp_thenReturn(CapturedOutput capturedOutput) {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now();
            when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

            humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            verify(humidityRepository, times(1)).getHumiditiesByDate(registeredAt.toLocalDate());
            verify(humidityRepository, times(0)).deleteHumiditiesByDate(registeredAt.toLocalDate());
            assertThat(capturedOutput)
                .contains(String.format("Humidities registeredAt=%s not found.", registeredAt));
        }
    }
}
