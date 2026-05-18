package api.arduinothermohygrometer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.TemperatureModelMapper;
import api.arduinothermohygrometer.model.Temperature;
import api.arduinothermohygrometer.repository.TemperatureRepository;
import api.arduinothermohygrometer.service.implementation.TemperatureServiceImpl;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("TemperatureServiceImpl unit tests.")
@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class TemperatureServiceImplTest {
    @Mock
    private TemperatureRepository temperatureRepository;

    @Captor
    private ArgumentCaptor<Temperature> temperatureArgumentCaptor = ArgumentCaptor.forClass(Temperature.class);

    @InjectMocks
    private TemperatureServiceImpl temperatureService;

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
            .hasMessage(String.format("Temperature with id=%s not found.", invalidId));
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

        verify(temperatureRepository, times(1)).getTemperatureByTimestamp(registeredAt);
        assertThat(result)
            .hasSameElementsAs(temperatureDtos);
    }

    @Test
    void givenInvalidTimestamp_whenGetTemperaturesByDateOrTimestamp_thenReturnEmptyList() {
        boolean dateOnly = false;
        LocalDateTime registeredAt = LocalDateTime.now();
        when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(emptyList());

        List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

        verify(temperatureRepository, times(1)).getTemperatureByTimestamp(registeredAt);
        assertThat(result)
            .isEmpty();
    }

    @Test
    void givenValidDate_whenGetTemperaturesByDateOrTimestamp_thenReturnTemperatures() {
        boolean dateOnly = true;
        LocalDateTime registeredAt = LocalDateTime.now();
        Double temp = 75.00;
        Double temp2 = 80.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(registeredAt)
                                                      .temp(temp)
                                                      .build();
        TemperatureDto temperatureDto2 = TemperatureDto.builder()
                                                       .registeredAt(registeredAt.minusHours(1))
                                                       .temp(temp2)
                                                       .build();
        List<TemperatureDto> temperatureDtos = List.of(temperatureDto, temperatureDto2);
        List<Temperature> temperatures = temperatureDtos.stream()
                                                        .map(TemperatureModelMapper::toModel)
                                                        .toList();
        when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())).thenReturn(temperatures);

        List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

        verify(temperatureRepository, times(1)).getTemperaturesByDate(registeredAt.toLocalDate());
        assertThat(result)
            .hasSameElementsAs(temperatureDtos);
    }

    @Test
    void givenInvalidDate_whenGetTemperaturesByDate_thenReturnEmptyList() {
        boolean dateOnly = true;
        LocalDateTime registeredAt = LocalDateTime.now();
        when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

        List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

        verify(temperatureRepository, times(1)).getTemperaturesByDate(registeredAt.toLocalDate());
        assertThat(result)
            .isEmpty();
    }

    @Test
    void givenValidTemperatureModel_whenCreateTemperature_thenReturnCreatedTemperature() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double temp = 75.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(registeredAt)
                                                      .temp(temp)
                                                      .build();
        Optional<Temperature> temperature = Optional.of(new Temperature(registeredAt, temp));
        when(temperatureRepository.createTemperature(any())).thenReturn(temperature);

        TemperatureDto result = temperatureService.createTemperature(temperatureDto);

        verify(temperatureRepository, times(1)).createTemperature(temperatureArgumentCaptor.capture());
        assertThat(result)
            .isEqualTo(temperatureDto);
    }

    @Test
    void givenInvalidTemperatureModel_whenCreateTemperature_thenThrowResourceNotCreatedException() {
        assertThatThrownBy(() -> temperatureService.createTemperature(null))
            .isInstanceOf(ResourceNotCreatedException.class)
            .hasMessage("Temperature cannot be created.");
    }

    @Test
    void givenValidId_whenDeleteTemperatureById_thenDeleteTemperature(CapturedOutput capturedOutput) {
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

        verify(temperatureRepository, times(1)).getTemperatureById(id);
        verify(temperatureRepository, times(1)).deleteTemperatureById(id);
        assertThat(capturedOutput)
            .contains(String.format("Temperature with id=%s deleted.", id));
    }

    @Test
    void givenInvalidId_whenDeleteTemperatureById_thenThrowResourceNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(temperatureRepository.getTemperatureById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> temperatureService.deleteTemperatureById(invalidId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperature with id=%s not found.", invalidId));
    }

    @Test
    void givenValidTimestamp_whenDeleteTemperaturesByDateOrTimestamp_thenDeleteTemperature(CapturedOutput capturedOutput) {
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

        verify(temperatureRepository, times(1)).getTemperatureByTimestamp(registeredAt);
        verify(temperatureRepository, times(1)).deleteTemperatureByTimestamp(registeredAt);
        assertThat(capturedOutput)
            .contains(String.format("Deleted temperature with timestamp=%s.", temperatures.getFirst().getRegisteredAt()));
    }

    @Test
    void givenInvalidTimestamp_whenDeleteTemperaturesByDateOrTimestamp_thenReturn(CapturedOutput capturedOutput) {
        boolean dateOnly = false;
        LocalDateTime registeredAt = LocalDateTime.now();
        when(temperatureRepository.getTemperatureByTimestamp(registeredAt)).thenReturn(emptyList());

        temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

        verify(temperatureRepository, times(1)).getTemperatureByTimestamp(registeredAt);
        verify(temperatureRepository, times(0)).deleteTemperatureByTimestamp(registeredAt);
        assertThat(capturedOutput)
            .contains(String.format("Temperatures registeredAt=%s not found.", registeredAt));
    }

    @Test
    void givenValidDate_whenDeleteTemperaturesByDateOrTimestamp_thenDeleteTemperature(CapturedOutput capturedOutput) {
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

        verify(temperatureRepository, times(1)).getTemperaturesByDate(registeredAt.toLocalDate());
        verify(temperatureRepository, times(1)).deleteTemperaturesByDate(registeredAt.toLocalDate());
        assertThat(capturedOutput)
            .contains(String.format("Deleted temperatures with date=%s.", temperatures.getFirst().getRegisteredAt().toLocalDate()));
    }

    @Test
    void givenInvalidDate_whenDeleteTemperaturesByDateOrTimestamp_thenReturn(CapturedOutput capturedOutput) {
        boolean dateOnly = true;
        LocalDateTime registeredAt = LocalDateTime.now();
        when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())).thenReturn(emptyList());

        temperatureService.deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

        verify(temperatureRepository, times(1)).getTemperaturesByDate(registeredAt.toLocalDate());
        verify(temperatureRepository, times(0)).deleteTemperaturesByDate(registeredAt.toLocalDate());
        assertThat(capturedOutput)
            .contains(String.format("Temperatures registeredAt=%s not found.", registeredAt));
    }
}
