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
import api.arduinothermohygrometer.model.Temperature;
import api.arduinothermohygrometer.repository.TemperatureRepository;
import api.arduinothermohygrometer.service.implementation.TemperatureServiceImpl;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class TemperatureServiceImplTest {
  @Mock private TemperatureRepository temperatureRepository;

  @InjectMocks private TemperatureServiceImpl temperatureService;

  private Temperature createTemperature(LocalDateTime registeredAt, Double temp) {
    return new Temperature(registeredAt, temp);
  }

  @Nested
  class GetMethods {
    @Test
    void givenValidId_thenReturnTemperature() {
      UUID id = UUID.randomUUID();
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      Temperature temperature = createTemperature(registeredAt, 70.00);
      when(temperatureRepository.getTemperatureById(id)).thenReturn(Optional.of(temperature));

      Temperature result = temperatureService.getTemperatureById(id);

      assertThat(result.getRegisteredAt()).isEqualTo(temperature.getRegisteredAt());
      assertThat(result.getTemp()).isEqualTo(temperature.getTemp());
    }

    @Test
    void givenInvalidId_thenReturnResourceNotFoundException() {
      UUID invalidId = UUID.randomUUID();
      when(temperatureRepository.getTemperatureById(invalidId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> temperatureService.getTemperatureById(invalidId))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Temperature not found.");
    }

    @Test
    void givenValidTimestamp_thenReturnTemperature() {
      boolean dateOnly = false;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      List<Temperature> temperatures = List.of(createTemperature(registeredAt, 70.00));
      when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(temperatures);

      List<Temperature> result =
          temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

      verify(temperatureRepository).getTemperatureByTimestamp(registeredAt);
      assertThat(result)
          .hasSize(1)
          .first()
          .satisfies(
              temperature -> {
                assertThat(temperature.getRegisteredAt())
                    .isEqualTo(temperatures.getFirst().getRegisteredAt());
                assertThat(temperature.getTemp()).isEqualTo(temperatures.getFirst().getTemp());
              });
    }

    @Test
    void givenInvalidTimestamp_thenThrowResourceNotFoundException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(emptyList());

      assertThatThrownBy(
              () -> temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, false))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Temperatures not found for timestamp " + registeredAt + ".");
    }

    @Test
    void givenValidDate_thenReturnTemperatures() {
      boolean dateOnly = true;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      List<Temperature> temperatures =
          List.of(createTemperature(registeredAt, 70.00), createTemperature(registeredAt, 65.00));
      when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate()))
          .thenReturn(temperatures);

      List<Temperature> result =
          temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

      verify(temperatureRepository).getTemperaturesByDate(registeredAt.toLocalDate());
      assertThat(result)
          .hasSize(2)
          .first()
          .satisfies(
              temperature -> {
                assertThat(temperature.getRegisteredAt())
                    .isEqualTo(temperatures.getFirst().getRegisteredAt());
                assertThat(temperature.getTemp()).isEqualTo(temperatures.getFirst().getTemp());
              });
    }

    @Test
    void givenInvalidDate_thenThrowResourceNotFoundException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate()))
          .thenReturn(emptyList());

      assertThatThrownBy(
              () -> temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, true))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Temperatures not found for date " + registeredAt.toLocalDate() + ".");
    }
  }

  @Nested
  class CreateMethods {
    @Test
    void givenValidTemperature_thenReturnCreatedTemperature() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      Temperature temperature = createTemperature(registeredAt, 70.00);
      ReflectionTestUtils.setField(temperature, "id", UUID.randomUUID());
      when(temperatureRepository.createTemperature(any(Temperature.class)))
          .thenReturn(Optional.of(temperature));

      Temperature result = temperatureService.createTemperature(temperature);

      verify(temperatureRepository).createTemperature(any(Temperature.class));
      assertThat(result.getRegisteredAt()).isEqualTo(temperature.getRegisteredAt());
      assertThat(result.getTemp()).isEqualTo(temperature.getTemp());
    }

    @Test
    void givenEmptyTemperature_thenThrowResourceNotCreatedException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      Temperature temperature = createTemperature(registeredAt, 70.00);
      when(temperatureRepository.createTemperature(any(Temperature.class)))
          .thenReturn(Optional.empty());

      assertThatThrownBy(() -> temperatureService.createTemperature(temperature))
          .isInstanceOf(ResourceNotCreatedException.class)
          .hasMessage("Temperature cannot be created.");
    }
  }

  @Nested
  class DeleteMethods {
    @Test
    void givenValidTimestamp_thenDeleteTemperature() {
      boolean dateOnly = false;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      List<Temperature> temperatures = List.of(createTemperature(registeredAt, 70.00));
      when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(temperatures);

      temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

      verify(temperatureRepository).getTemperatureByTimestamp(registeredAt);
      verify(temperatureRepository).deleteTemperatureByTimestamp(registeredAt);
    }

    @Test
    void givenInvalidTimestamp_thenThrowResourceNotFoundException() {
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(emptyList());

      assertThatThrownBy(
              () -> temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, false))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Temperatures not found for timestamp " + registeredAt + ".");
    }

    @Test
    void givenValidDate_thenDeleteTemperature() {
      boolean dateOnly = true;
      LocalDateTime registeredAt = LocalDateTime.now();
      List<Temperature> temperatures =
          List.of(
              createTemperature(registeredAt, 70.00),
              createTemperature(registeredAt.plusHours(1), 65.00));
      when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate()))
          .thenReturn(temperatures);

      temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

      verify(temperatureRepository).getTemperaturesByDate(registeredAt.toLocalDate());
      verify(temperatureRepository).deleteTemperaturesByDate(registeredAt.toLocalDate());
    }

    @Test
    void givenInvalidDate_thenThrowResourceNotFoundException() {
      LocalDateTime registeredAt = LocalDateTime.now();
      when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate()))
          .thenReturn(emptyList());

      assertThatThrownBy(
              () -> temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, true))
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Temperatures not found for date " + registeredAt.toLocalDate() + ".");
    }
  }
}
