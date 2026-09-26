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

import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.model.Humidity;
import api.arduinothermohygrometer.repository.HumidityRepository;
import api.arduinothermohygrometer.service.implementation.HumidityServiceImpl;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class HumidityServiceImplTest {
  @Mock private HumidityRepository humidityRepository;

  @InjectMocks private HumidityServiceImpl humidityService;

  private Humidity createHumidity(LocalDateTime registeredAt, Double airHumidity) {
    return new Humidity(registeredAt, airHumidity);
  }

  @Nested
  class GetMethods {
    @Test
    void givenValidId_thenReturnHumidity() {
      UUID id = UUID.randomUUID();
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      Humidity humidity = createHumidity(registeredAt, 70.00);
      when(humidityRepository.getHumidityById(id)).thenReturn(Optional.of(humidity));

      Humidity result = humidityService.getHumidityById(id);

      assertThat(result.getRegisteredAt()).isEqualTo(humidity.getRegisteredAt());
      assertThat(result.getAirHumidity()).isEqualTo(humidity.getAirHumidity());
    }

    @Test
    void givenInvalidId_thenThrowResourceNotFoundException() {
      UUID invalidId = UUID.randomUUID();
      when(humidityRepository.getHumidityById(invalidId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> humidityService.getHumidityById(invalidId))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Humidity not found.");
    }

    @Test
    void givenValidTimestamp_thenReturnHumidities() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      List<Humidity> humidities = List.of(createHumidity(registeredAt, 70.00));
      when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(humidities);

      List<Humidity> result = humidityService.getHumiditiesByDateOrTimestamp(registeredAt, false);

      verify(humidityRepository).getHumidityByTimestamp(registeredAt);
      assertThat(result)
          .hasSize(1)
          .first()
          .satisfies(
              humidity -> {
                assertThat(humidity.getRegisteredAt())
                    .isEqualTo(humidities.getFirst().getRegisteredAt());
                assertThat(humidity.getAirHumidity())
                    .isEqualTo(humidities.getFirst().getAirHumidity());
              });
    }

    @Test
    void givenInvalidTimestamp_thenThrowResourceNotFoundException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(emptyList());

      assertThatThrownBy(() -> humidityService.getHumiditiesByDateOrTimestamp(registeredAt, false))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Humidities not found for timestamp " + registeredAt + ".");
    }

    @Test
    void givenValidDate_thenReturnHumidities() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      List<Humidity> humidities =
          List.of(
              createHumidity(registeredAt, 70.00),
              createHumidity(registeredAt.plusHours(1), 65.00));
      when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate()))
          .thenReturn(humidities);

      List<Humidity> result = humidityService.getHumiditiesByDateOrTimestamp(registeredAt, true);

      verify(humidityRepository).getHumiditiesByDate(registeredAt.toLocalDate());
      assertThat(result)
          .hasSize(2)
          .first()
          .satisfies(
              humidity -> {
                assertThat(humidity.getRegisteredAt())
                    .isEqualTo(humidities.getFirst().getRegisteredAt());
                assertThat(humidity.getAirHumidity())
                    .isEqualTo(humidities.getFirst().getAirHumidity());
              });
    }

    @Test
    void givenInvalidDate_thenThrowResourceNotFoundException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate()))
          .thenReturn(emptyList());

      assertThatThrownBy(() -> humidityService.getHumiditiesByDateOrTimestamp(registeredAt, true))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Humidities not found for date " + registeredAt.toLocalDate() + ".");
    }
  }

  @Nested
  class CreateMethods {
    @Test
    void givenValidHumidity_thenReturnCreatedHumidity() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      Humidity humidity = createHumidity(registeredAt, 70.00);
      ReflectionTestUtils.setField(humidity, "id", UUID.randomUUID());
      when(humidityRepository.createHumidity(any(Humidity.class)))
          .thenReturn(Optional.of(humidity));

      Humidity result = humidityService.createHumidity(humidity);

      verify(humidityRepository).createHumidity(any(Humidity.class));
      assertThat(result.getRegisteredAt()).isEqualTo(humidity.getRegisteredAt());
      assertThat(result.getAirHumidity()).isEqualTo(humidity.getAirHumidity());
    }

    @Test
    void givenEmptyHumidity_thenThrowResourceNotCreatedException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      Humidity humidity = createHumidity(registeredAt, 70.00);
      when(humidityRepository.createHumidity(any(Humidity.class))).thenReturn(Optional.empty());

      assertThatThrownBy(() -> humidityService.createHumidity(humidity))
          .isInstanceOf(ResourceNotCreatedException.class)
          .hasMessage("Humidity cannot be created.");
    }
  }

  @Nested
  class DeleteMethods {
    @Test
    void givenValidTimestamp_thenDeleteHumidity() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      List<Humidity> humidities = List.of(createHumidity(registeredAt, 70.00));
      when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(humidities);

      humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, false);

      verify(humidityRepository).getHumidityByTimestamp(registeredAt);
      verify(humidityRepository).deleteHumidityByTimestamp(registeredAt);
    }

    @Test
    void givenInvalidTimestamp_thenThrowResourceNotFoundException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(humidityRepository.getHumidityByTimestamp(registeredAt)).thenReturn(emptyList());

      assertThatThrownBy(() -> humidityService.getHumiditiesByDateOrTimestamp(registeredAt, false))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Humidities not found for timestamp " + registeredAt + ".");
    }

    @Test
    void givenValidDate_thenDeleteHumidity() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      List<Humidity> humidities =
          List.of(
              createHumidity(registeredAt, 70.00),
              createHumidity(registeredAt.plusHours(1), 65.00));
      when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate()))
          .thenReturn(humidities);

      humidityService.deleteHumiditiesByDateOrTimestamp(registeredAt, true);

      verify(humidityRepository).getHumiditiesByDate(registeredAt.toLocalDate());
      verify(humidityRepository).deleteHumiditiesByDate(registeredAt.toLocalDate());
    }

    @Test
    void givenInvalidDate_thenThrowResourceNotFoundException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate()))
          .thenReturn(emptyList());

      assertThatThrownBy(() -> humidityService.getHumiditiesByDateOrTimestamp(registeredAt, true))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Humidities not found for date " + registeredAt.toLocalDate() + ".");
    }
  }
}
