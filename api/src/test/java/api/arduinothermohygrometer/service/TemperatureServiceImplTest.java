package api.arduinothermohygrometer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
    private ArgumentCaptor<Temperature> temperatureArgumentCaptor;

    private TemperatureServiceImpl temperatureService;

    @BeforeEach
    void setUp() {
        temperatureService = new TemperatureServiceImpl(temperatureRepository);
        temperatureArgumentCaptor = ArgumentCaptor.forClass(Temperature.class);
    }

    @Test
    @DisplayName("getTemperatureById returns temperature with valid id.")
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
    @DisplayName("getTemperatureById throws ResourceNotFoundException with invalid id.")
    void givenInvalidId_whenGetTemperatureById_thenThrowResourceNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(temperatureRepository.getTemperatureById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> temperatureService.getTemperatureById(invalidId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperature with id=%s not found.", invalidId));
    }

    @Test
    @DisplayName("getTemperaturesByDateOrTimestamp returns temperature with valid timestamp.")
    void givenValidTimestamp_whenGetTemperaturesByDateOrTimestamp_thenReturnTemperature() {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now();
        Double temp = 75.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(dateTime)
                                                      .temp(temp)
                                                      .build();
        List<TemperatureDto> temperatureDtos = List.of(temperatureDto);
        List<Temperature> temperatures = List.of(TemperatureModelMapper.toModel(temperatureDto));
        when(temperatureRepository.getTemperatureByTimestamp(dateTime)).thenReturn(temperatures);

        List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(temperatureRepository, times(1)).getTemperatureByTimestamp(dateTime);
        assertThat(result)
            .hasSameElementsAs(temperatureDtos);
    }

    @Test
    @DisplayName("getTemperaturesByDateOrTimestamp returns empty list with invalid timestamp.")
    void givenInvalidTimestamp_whenGetTemperaturesByDateOrTimestamp_thenReturnEmptyList() {
        boolean checkOnlyDate = false;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(temperatureRepository.getTemperatureByTimestamp(invalidDateTime)).thenReturn(emptyList());

        List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(invalidDateTime, checkOnlyDate);

        verify(temperatureRepository, times(1)).getTemperatureByTimestamp(invalidDateTime);
        assertThat(result)
            .isEmpty();
    }

    @Test
    @DisplayName("getTemperaturesByDateOrTimestamp returns temperatures with valid date.")
    void givenValidDate_whenGetTemperaturesByDateOrTimestamp_thenReturnTemperatures() {
        boolean checkOnlyDate = true;
        LocalDateTime dateTime = LocalDateTime.now();
        Double temp = 75.00;
        Double temp2 = 80.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(dateTime)
                                                      .temp(temp)
                                                      .build();
        TemperatureDto temperatureDto2 = TemperatureDto.builder()
                                                       .registeredAt(dateTime.minusHours(1))
                                                       .temp(temp2)
                                                       .build();
        List<TemperatureDto> temperatureDtos = List.of(temperatureDto, temperatureDto2);
        List<Temperature> temperatures = temperatureDtos.stream()
                                                        .map(TemperatureModelMapper::toModel)
                                                        .toList();
        when(temperatureRepository.getTemperaturesByDate(dateTime.toLocalDate())).thenReturn(temperatures);

        List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(temperatureRepository, times(1)).getTemperaturesByDate(dateTime.toLocalDate());
        assertThat(result)
            .hasSameElementsAs(temperatureDtos);
    }

    @Test
    @DisplayName("getTemperaturesByDate returns empty list with invalid date.")
    void givenInvalidDate_whenGetTemperaturesByDate_thenReturnEmptyList() {
        boolean checkOnlyDate = true;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(temperatureRepository.getTemperaturesByDate(invalidDateTime.toLocalDate())).thenReturn(emptyList());

        List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(invalidDateTime, checkOnlyDate);

        verify(temperatureRepository, times(1)).getTemperaturesByDate(invalidDateTime.toLocalDate());
        assertThat(result)
            .isEmpty();
    }

    @Test
    @DisplayName("createTemperature returns created temperature with valid temperature model.")
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
    @DisplayName("createTemperature throws ResourceNotCreatedException with invalid temperature model.")
    void givenInvalidTemperatureModel_whenCreateTemperature_thenThrowResourceNotCreatedException() {
        assertThatThrownBy(() -> temperatureService.createTemperature(null))
            .isInstanceOf(ResourceNotCreatedException.class)
            .hasMessage("Temperature cannot be created.");
    }

    @Test
    @DisplayName("deleteTemperatureById deletes temperature with valid id.")
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
    @DisplayName("deleteTemperatureById throws ResourceNotFoundException with invalid id.")
    void givenInvalidId_whenDeleteTemperatureById_thenThrowResourceNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(temperatureRepository.getTemperatureById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> temperatureService.deleteTemperatureById(invalidId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperature with id=%s not found.", invalidId));
    }

    @Test
    @DisplayName("deleteTemperaturesByDateOrTimestamp deletes temperature with valid timestamp.")
    void givenValidTimestamp_whenDeleteTemperaturesByDateOrTimestamp_thenDeleteTemperature(CapturedOutput capturedOutput) {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now();
        Double temp = 75.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(dateTime)
                                                      .temp(temp)
                                                      .build();
        List<Temperature> temperatures = List.of(TemperatureModelMapper.toModel(temperatureDto));
        when(temperatureRepository.getTemperatureByTimestamp(dateTime)).thenReturn(temperatures);
        doNothing().when(temperatureRepository).deleteTemperatureByTimestamp(dateTime);

        temperatureService.deleteTemperaturesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(temperatureRepository, times(1)).getTemperatureByTimestamp(dateTime);
        verify(temperatureRepository, times(1)).deleteTemperatureByTimestamp(dateTime);
        assertThat(capturedOutput)
            .contains(String.format("Deleted temperature with timestamp=%s.", temperatures.getFirst().getRegisteredAt()));
    }

    @Test
    @DisplayName("deleteTemperaturesByDateOrTimestamp returns with invalid timestamp.")
    void givenInvalidTimestamp_whenDeleteTemperaturesByDateOrTimestamp_thenReturn(CapturedOutput capturedOutput) {
        boolean checkOnlyDate = false;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(temperatureRepository.getTemperatureByTimestamp(invalidDateTime)).thenReturn(emptyList());

        temperatureService.deleteTemperaturesByDateOrTimestamp(invalidDateTime, checkOnlyDate);

        verify(temperatureRepository, times(1)).getTemperatureByTimestamp(invalidDateTime);
        verify(temperatureRepository, times(0)).deleteTemperatureByTimestamp(invalidDateTime);
        assertThat(capturedOutput)
            .contains(String.format("Temperatures with dateTime=%s not found.", invalidDateTime));
    }

    @Test
    @DisplayName("deleteTemperaturesByDateOrTimestamp deletes temperature with valid date.")
    void givenValidDate_whenDeleteTemperaturesByDateOrTimestamp_thenDeleteTemperature(CapturedOutput capturedOutput) {
        boolean checkOnlyDate = true;
        LocalDateTime dateTime = LocalDateTime.now();
        Double temp = 75.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(dateTime)
                                                      .temp(temp)
                                                      .build();
        List<Temperature> temperatures = List.of(TemperatureModelMapper.toModel(temperatureDto));
        when(temperatureRepository.getTemperaturesByDate(dateTime.toLocalDate())).thenReturn(temperatures);
        doNothing().when(temperatureRepository).deleteTemperaturesByDate(dateTime.toLocalDate());

        temperatureService.deleteTemperaturesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(temperatureRepository, times(1)).getTemperaturesByDate(dateTime.toLocalDate());
        verify(temperatureRepository, times(1)).deleteTemperaturesByDate(dateTime.toLocalDate());
        assertThat(capturedOutput)
            .contains(String.format("Deleted temperatures with date=%s.", temperatures.getFirst().getRegisteredAt().toLocalDate()));
    }

    @Test
    @DisplayName("deleteTemperaturesByDateOrTimestamp returns with invalid date.")
    void givenInvalidDate_whenDeleteTemperaturesByDateOrTimestamp_thenReturn(CapturedOutput capturedOutput) {
        boolean checkOnlyDate = true;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(temperatureRepository.getTemperaturesByDate(invalidDateTime.toLocalDate())).thenReturn(emptyList());

        temperatureService.deleteTemperaturesByDateOrTimestamp(invalidDateTime, checkOnlyDate);

        verify(temperatureRepository, times(1)).getTemperaturesByDate(invalidDateTime.toLocalDate());
        verify(temperatureRepository, times(0)).deleteTemperaturesByDate(invalidDateTime.toLocalDate());
        assertThat(capturedOutput)
            .contains(String.format("Temperatures with dateTime=%s not found.", invalidDateTime));
    }
}
