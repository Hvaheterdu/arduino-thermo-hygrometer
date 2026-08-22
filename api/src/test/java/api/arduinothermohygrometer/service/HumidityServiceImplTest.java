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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class HumidityServiceImplTest {
  @Mock private HumidityRepository humidityRepository;

  @InjectMocks private HumidityServiceImpl humidityService;

  private HumidityDto createHumidityDto(LocalDateTime registeredAt, Double airHumidity) {
    return HumidityDto.builder().registeredAt(registeredAt).airHumidity(airHumidity).build();
  }

  @Nested
  class GetMethods {
    @Test
    void givenValidId_thenReturnHumidity() {
      UUID id = UUID.randomUUID();
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      HumidityDto humidityDto = createHumidityDto(registeredAt, 70.00);
      Humidity humidity = HumidityModelMapper.toModel(humidityDto);
      when(humidityRepository.getHumidityById(id)).thenReturn(Optional.of(humidity));

      HumidityDto result = humidityService.getHumidityById(id);

      assertThat(result.getId()).isEqualTo(humidityDto.getId());
      assertThat(result.getRegisteredAt()).isEqualTo(humidityDto.getRegisteredAt());
      assertThat(result.getAirHumidity()).isEqualTo(humidityDto.getAirHumidity());
    }

    @Test
    void givenInvalidId_thenThrowResourceNotFoundException() {
      UUID invalidId = UUID.randomUUID();
      when(humidityRepository.getHumidityById(invalidId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> humidityService.getHumidityById(invalidId))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Humidity with id=%s not found.".formatted(invalidId));
    }

    @Test
    void givenValidTimestamp_thenReturnHumidity() {
      boolean dateOnly = false;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      HumidityDto humidityDto = createHumidityDto(registeredAt, 70.00);
      List<Humidity> humidities = List.of(HumidityModelMapper.toModel(humidityDto));
      when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(humidities);

      List<HumidityDto> result =
          humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

      verify(humidityRepository).getHumidityByTimestamp(registeredAt);
      assertThat(result)
          .hasSize(1)
          .first()
          .satisfies(
              humidity -> {
                assertThat(humidity.getId()).isEqualTo(humidityDto.getId());
                assertThat(humidity.getRegisteredAt()).isEqualTo(humidityDto.getRegisteredAt());
                assertThat(humidity.getAirHumidity()).isEqualTo(humidityDto.getAirHumidity());
              });
    }

    @Test
    void givenInvalidTimestamp_thenReturnEmptyList() {
      boolean dateOnly = false;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(emptyList());

      List<HumidityDto> result =
          humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

      verify(humidityRepository).getHumidityByTimestamp(registeredAt);
      assertThat(result).isEmpty();
    }

    @Test
    void givenValidDate_thenReturnHumidities() {
      boolean dateOnly = true;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      List<HumidityDto> humidityDtos =
          List.of(
              createHumidityDto(registeredAt, 70.00),
              createHumidityDto(registeredAt.minusHours(1), 65.00));
      List<Humidity> humidities = humidityDtos.stream().map(HumidityModelMapper::toModel).toList();
      when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate()))
          .thenReturn(humidities);

      List<HumidityDto> result =
          humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

      verify(humidityRepository).getHumiditiesByDate(registeredAt.toLocalDate());
      assertThat(result)
          .hasSize(2)
          .first()
          .satisfies(
              humidity -> {
                assertThat(humidity.getId()).isEqualTo(humidityDtos.getFirst().getId());
                assertThat(humidity.getRegisteredAt())
                    .isEqualTo(humidityDtos.getFirst().getRegisteredAt());
                assertThat(humidity.getAirHumidity())
                    .isEqualTo(humidityDtos.getFirst().getAirHumidity());
              });
    }

    @Test
    void givenInvalidDate_thenReturnEmptyList() {
      boolean dateOnly = true;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate()))
          .thenReturn(emptyList());

      List<HumidityDto> result =
          humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

      verify(humidityRepository).getHumiditiesByDate(registeredAt.toLocalDate());
      assertThat(result).isEmpty();
    }
  }

  @Nested
  class CreateMethods {
    @Test
    void givenValidHumidityModel_thenReturnCreatedHumidity() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      HumidityDto humidityDto = createHumidityDto(registeredAt, 70.00);
      Humidity humidity = new Humidity(registeredAt, 70.00);
      ReflectionTestUtils.setField(humidity, "id", UUID.randomUUID());
      when(humidityRepository.createHumidity(any(Humidity.class)))
          .thenReturn(Optional.of(humidity));

      HumidityDto result = humidityService.createHumidity(humidityDto);

      verify(humidityRepository).createHumidity(any(Humidity.class));
      assertThat(result.getId()).isNotNull();
      assertThat(result.getRegisteredAt()).isEqualTo(humidityDto.getRegisteredAt());
      assertThat(result.getAirHumidity()).isEqualTo(humidityDto.getAirHumidity());
    }

    @Test
    void givenInvalidHumidityModel_thenReturnResourceNotCreatedException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      HumidityDto humidityDto = createHumidityDto(registeredAt, 70.00);
      when(humidityRepository.createHumidity(any(Humidity.class))).thenReturn(Optional.empty());

      assertThatThrownBy(() -> humidityService.createHumidity(humidityDto))
          .isInstanceOf(ResourceNotCreatedException.class)
          .hasMessage("Humidity cannot be created.");
    }
  }

  @Nested
  class DeleteMethods {
    @Test
    void givenValidTimestamp_thenDeleteHumidity() {
      boolean dateOnly = false;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      HumidityDto humidityDto = createHumidityDto(registeredAt, 70.00);
      List<Humidity> humidities = List.of(HumidityModelMapper.toModel(humidityDto));
      when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(humidities);

      humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

      verify(humidityRepository).getHumidityByTimestamp(registeredAt);
      verify(humidityRepository).deleteHumidityByTimestamp(registeredAt);
    }

    @Test
    void givenInvalidTimestamp_thenReturn() {
      boolean dateOnly = false;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(emptyList());

      humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

      verify(humidityRepository).getHumidityByTimestamp(registeredAt);
      verify(humidityRepository, times(0)).deleteHumidityByTimestamp(registeredAt);
    }

    @Test
    void givenValidDate_thenDeleteHumidity() {
      boolean dateOnly = true;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      List<HumidityDto> humidityDtos =
          List.of(
              createHumidityDto(registeredAt, 70.00),
              createHumidityDto(registeredAt.minusHours(1), 65.00));
      List<Humidity> humidities = humidityDtos.stream().map(HumidityModelMapper::toModel).toList();
      when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate()))
          .thenReturn(humidities);

      humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

      verify(humidityRepository).getHumiditiesByDate(registeredAt.toLocalDate());
      verify(humidityRepository).deleteHumiditiesByDate(registeredAt.toLocalDate());
    }

    @Test
    void givenInvalidDate_thenReturn() {
      boolean dateOnly = true;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate()))
          .thenReturn(emptyList());

      humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

      verify(humidityRepository).getHumiditiesByDate(registeredAt.toLocalDate());
      verify(humidityRepository, times(0)).deleteHumiditiesByDate(registeredAt.toLocalDate());
    }
  }
}
