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

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.TemperatureEntityMapper;
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

@DisplayName("Unit tests for TemperatureServiceImpl")
@ExtendWith(MockitoExtension.class)
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
    void givenValidId_whenGettingTemperatureById_thenReturnTemperature() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double temp = 70.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(registeredAt)
                                                      .temp(temp)
                                                      .build();
        Temperature temperature = TemperatureEntityMapper.toEntity(temperatureDto);
        UUID id = temperature.getId();
        when(temperatureRepository.getTemperatureById(id)).thenReturn(Optional.of(temperature));

        TemperatureDto result = temperatureService.getTemperatureById(id);

        assertThat(result)
            .isEqualTo(temperatureDto);
    }

    @Test
    @DisplayName("getTemperatureById throws ResourceNotFoundException with empty id.")
    void givenEmptyId_whenGettingTemperatureById_thenThrowResourceNotFoundException() {
        UUID emptyId = new UUID(0, 0);

        assertThatThrownBy(() -> temperatureService.getTemperatureById(emptyId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperature with empty id=%s does not exist.", emptyId));
    }

    @Test
    @DisplayName("getTemperatureById throws ResourceNotFoundException with invalid id.")
    void givenInvalidId_whenGettingTemperatureById_thenThrowResourceNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(temperatureRepository.getTemperatureById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> temperatureService.getTemperatureById(invalidId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperature with id=%s not found.", invalidId));
    }

    @Test
    @DisplayName("getTemperaturesByDateOrTimestamp returns temperature with valid timestamp.")
    void givenValidTimestamp_whenGettingTemperaturesByDateOrTimestamp_thenReturnTemperature() {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now();
        Double temp = 75.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(dateTime)
                                                      .temp(temp)
                                                      .build();
        List<TemperatureDto> temperatureDtos = List.of(temperatureDto);
        List<Temperature> temperatures = List.of(TemperatureEntityMapper.toEntity(temperatureDto));
        when(temperatureRepository.getTemperatureByTimestamp(dateTime)).thenReturn(temperatures);

        List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(temperatureRepository, times(1)).getTemperatureByTimestamp(dateTime);
        assertThat(result)
            .hasSameElementsAs(temperatureDtos);
    }

    @Test
    @DisplayName("getTemperaturesByDateOrTimestamp throws ResourceNotFoundException with invalid timestamp.")
    void givenInvalidTimestamp_whenGettingTemperaturesByDateOrTimestamp_thenThrowResourceNotFoundException() {
        boolean checkOnlyDate = false;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(temperatureRepository.getTemperatureByTimestamp(invalidDateTime)).thenReturn(emptyList());

        assertThatThrownBy(() -> temperatureService.getTemperaturesByDateOrTimestamp(invalidDateTime, checkOnlyDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperatures with dateTime=%s not found.", invalidDateTime));
    }

    @Test
    @DisplayName("getTemperaturesByDateOrTimestamp returns temperatures with valid date.")
    void givenValidDate_whenGettingTemperaturesByDateOrTimestamp_thenReturnTemperatures() {
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
                                                        .map(TemperatureEntityMapper::toEntity)
                                                        .toList();
        when(temperatureRepository.getTemperaturesByDate(dateTime.toLocalDate())).thenReturn(temperatures);

        List<TemperatureDto> result = temperatureService.getTemperaturesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(temperatureRepository, times(1)).getTemperaturesByDate(dateTime.toLocalDate());
        assertThat(result)
            .hasSameElementsAs(temperatureDtos);
    }

    @Test
    @DisplayName("getTemperaturesByDate throws ResourceNotFoundException with invalid date.")
    void givenInvalidDate_whenGettingTemperaturesByDate_thenThrowResourceNotFoundException() {
        boolean checkOnlyDate = true;
        LocalDateTime invalidDateTime = LocalDateTime.now();

        when(temperatureRepository.getTemperaturesByDate(invalidDateTime.toLocalDate())).thenReturn(emptyList());

        assertThatThrownBy(() -> temperatureService.getTemperaturesByDateOrTimestamp(invalidDateTime, checkOnlyDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperatures with dateTime=%s not found.", invalidDateTime));
    }

    @Test
    @DisplayName("createTemperature returns created temperature with valid temperature model.")
    void givenValidTemperatureModel_whenCreatingTemperature_thenReturnCreatedTemperature() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double temp = 75.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(registeredAt)
                                                      .temp(temp)
                                                      .build();
        doNothing().when(temperatureRepository).createTemperature(any());

        TemperatureDto result = temperatureService.createTemperature(temperatureDto);

        verify(temperatureRepository, times(1)).createTemperature(temperatureArgumentCaptor.capture());
        assertThat(result)
            .isEqualTo(temperatureDto);
    }

    @Test
    @DisplayName("createTemperature throws ResourceNotCreatedException with invalid temperature model.")
    void givenInvalidTemperatureModel_whenCreatingTemperature_thenThrowResourceNotCreatedException() {
        assertThatThrownBy(() -> temperatureService.createTemperature(null))
            .isInstanceOf(ResourceNotCreatedException.class)
            .hasMessage("Temperature cannot be created.");
    }

    @Test
    @DisplayName("deleteTemperatureById deletes temperature with valid id.")
    void givenValidId_whenDeletingTemperatureById_thenDeleteTemperature() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double temp = 75.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(registeredAt)
                                                      .temp(temp)
                                                      .build();
        Temperature temperature = TemperatureEntityMapper.toEntity(temperatureDto);
        UUID id = temperature.getId();
        when(temperatureRepository.getTemperatureById(id)).thenReturn(Optional.of(temperature));
        doNothing().when(temperatureRepository).deleteTemperatureById(id);

        temperatureService.deleteTemperatureById(id);

        verify(temperatureRepository, times(1)).getTemperatureById(id);
        verify(temperatureRepository, times(1)).deleteTemperatureById(id);
    }

    @Test
    @DisplayName("deleteTemperatureById throws ResourceNotFoundException with empty id.")
    void givenEmptyId_whenDeletingTemperatureById_thenThrowResourceNotFoundException() {
        UUID emptyId = new UUID(0, 0);

        assertThatThrownBy(() -> temperatureService.deleteTemperatureById(emptyId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperature with empty id=%s does not exist.", emptyId));
    }

    @Test
    @DisplayName("deleteTemperatureById throws ResourceNotFoundException with invalid id.")
    void givenInvalidId_whenDeletingTemperatureById_thenThrowResourceNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(temperatureRepository.getTemperatureById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> temperatureService.deleteTemperatureById(invalidId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperature with id=%s not found.", invalidId));
    }

    @Test
    @DisplayName("deleteTemperaturesByDateOrTimestamp deletes temperature with valid timestamp.")
    void givenValidTimestamp_whenDeletingTemperaturesByDateOrTimestamp_thenDeleteTemperature() {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now();
        Double temp = 75.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(dateTime)
                                                      .temp(temp)
                                                      .build();
        List<Temperature> temperatures = List.of(TemperatureEntityMapper.toEntity(temperatureDto));
        when(temperatureRepository.getTemperatureByTimestamp(dateTime)).thenReturn(temperatures);
        doNothing().when(temperatureRepository).deleteTemperatureByTimestamp(dateTime);

        temperatureService.deleteTemperaturesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(temperatureRepository, times(1)).getTemperatureByTimestamp(dateTime);
        verify(temperatureRepository, times(1)).deleteTemperatureByTimestamp(dateTime);
    }

    @Test
    @DisplayName("deleteTemperaturesByDateOrTimestamp throws ResourceNotFoundException with invalid timestamp.")
    void givenInvalidTimestamp_whenDeletingTemperaturesByDateOrTimestamp_thenThrowResourceNotFoundException() {
        boolean checkOnlyDate = false;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(temperatureRepository.getTemperatureByTimestamp(invalidDateTime)).thenReturn(emptyList());

        assertThatThrownBy(() -> temperatureService.deleteTemperaturesByDateOrTimestamp(invalidDateTime, checkOnlyDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperatures with dateTime=%s not found.", invalidDateTime));
    }

    @Test
    @DisplayName("deleteTemperaturesByDateOrTimestamp deletes temperature with valid date.")
    void givenValidDate_whenDeletingTemperaturesByDateOrTimestamp_thenDeleteTemperature() {
        boolean checkOnlyDate = true;
        LocalDateTime dateTime = LocalDateTime.now();
        Double temp = 75.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(dateTime)
                                                      .temp(temp)
                                                      .build();
        List<Temperature> temperatures = List.of(TemperatureEntityMapper.toEntity(temperatureDto));
        when(temperatureRepository.getTemperaturesByDate(dateTime.toLocalDate())).thenReturn(temperatures);
        doNothing().when(temperatureRepository).deleteTemperaturesByDate(dateTime.toLocalDate());

        temperatureService.deleteTemperaturesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(temperatureRepository, times(1)).getTemperaturesByDate(dateTime.toLocalDate());
        verify(temperatureRepository, times(1)).deleteTemperaturesByDate(dateTime.toLocalDate());
    }

    @Test
    @DisplayName("deleteTemperaturesByDateOrTimestamp throws ResourceNotFoundException with invalid date.")
    void givenInvalidDate_whenDeletingTemperaturesByDateOrTimestamp_thenThrowResourceNotFoundException() {
        boolean checkOnlyDate = true;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(temperatureRepository.getTemperaturesByDate(invalidDateTime.toLocalDate())).thenReturn(emptyList());

        assertThatThrownBy(() -> temperatureService.deleteTemperaturesByDateOrTimestamp(invalidDateTime, checkOnlyDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperatures with dateTime=%s not found.", invalidDateTime));
        verify(temperatureRepository, times(1)).getTemperaturesByDate(invalidDateTime.toLocalDate());
        verify(temperatureRepository, times(0)).deleteTemperaturesByDate(invalidDateTime.toLocalDate());
    }
}
