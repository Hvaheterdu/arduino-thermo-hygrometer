package api.arduinothermohygrometer.services;

import java.time.LocalDate;
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

import api.arduinothermohygrometer.dtos.TemperatureDto;
import api.arduinothermohygrometer.entities.Temperature;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.mappers.TemperatureEntityMapper;
import api.arduinothermohygrometer.repositories.TemperatureRepository;
import api.arduinothermohygrometer.services.implementations.TemperatureServiceImpl;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for TemperatureServiceImpl")
@ExtendWith(MockitoExtension.class)
class TemperatureServiceImplTest {
    @Mock
    private TemperatureRepository temperatureRepository;

    @Captor
    ArgumentCaptor<Temperature> temperatureArgumentCaptor;

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
        verifyNoInteractions(temperatureRepository);
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
    @DisplayName("getTemperatureByTimestamp returns temperature with valid timestamp.")
    void givenValidTimestamp_whenGettingTemperatureByTimestamp_thenReturnTemperature() {
        LocalDateTime timestamp = LocalDateTime.now();
        Double temp = 75.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(timestamp)
                                                      .temp(temp)
                                                      .build();
        Temperature temperature = TemperatureEntityMapper.toEntity(temperatureDto);
        when(temperatureRepository.getTemperatureByTimestamp(timestamp)).thenReturn(Optional.of(temperature));

        TemperatureDto result = temperatureService.getTemperatureByTimestamp(timestamp);

        assertThat(result)
            .isEqualTo(temperatureDto);
    }

    @Test
    @DisplayName("getTemperatureByTimestamp throws ResourceNotFoundException with invalid timestamp.")
    void givenInvalidTimestamp_whenGettingTemperatureByTimestamp_thenThrowResourceNotFoundException() {
        LocalDateTime invalidTimestamp = LocalDateTime.now();
        when(temperatureRepository.getTemperatureByTimestamp(invalidTimestamp)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> temperatureService.getTemperatureByTimestamp(invalidTimestamp))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperature with timestamp=%s not found.", invalidTimestamp));
    }

    @Test
    @DisplayName("getTemperaturesByDate returns temperatures with valid date.")
    void givenValidDate_whenGettingTemperaturesByDate_thenReturnTemperatures() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double temp = 75.00;
        LocalDateTime registeredAt2 = LocalDateTime.now();
        Double temp2 = 80.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(registeredAt)
                                                      .temp(temp)
                                                      .build();
        TemperatureDto temperatureDto2 = TemperatureDto.builder()
                                                       .registeredAt(registeredAt2)
                                                       .temp(temp2)
                                                       .build();
        List<TemperatureDto> temperatureDtos = List.of(temperatureDto, temperatureDto2);
        List<Temperature> temperatures = temperatureDtos.stream()
                                                        .map(TemperatureEntityMapper::toEntity)
                                                        .toList();
        when(temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())).thenReturn(temperatures);

        List<TemperatureDto> result = temperatureService.getTemperaturesByDate(registeredAt.toLocalDate());

        assertThat(result)
            .hasSameElementsAs(temperatureDtos);
    }

    @Test
    @DisplayName("getTemperaturesByDate throws ResourceNotFoundException with invalid date.")
    void givenInvalidDate_whenGettingTemperaturesByDate_thenThrowResourceNotFoundException() {
        LocalDate invalidDate = LocalDate.now();
        when(temperatureRepository.getTemperaturesByDate(invalidDate)).thenReturn(emptyList());

        assertThatThrownBy(() -> temperatureService.getTemperaturesByDate(invalidDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperatures with date=%s not found.", invalidDate));
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
        verifyNoInteractions(temperatureRepository);
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
        verifyNoInteractions(temperatureRepository);
    }

    @Test
    @DisplayName("deleteTemperatureById throws ResourceNotFoundException with invalid id.")
    void givenInvalidId_whenDeletingTemperatureById_thenThrowResourceNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(temperatureRepository.getTemperatureById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> temperatureService.deleteTemperatureById(invalidId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperature with id=%s not found.", invalidId));
        verify(temperatureRepository, times(1)).getTemperatureById(invalidId);
        verify(temperatureRepository, times(0)).deleteTemperatureById(invalidId);
    }

    @Test
    @DisplayName("deleteTemperatureByTimestamp deletes temperature with valid timestamp.")
    void givenValidTimestamp_whenDeletingTemperatureByTimestamp_thenDeleteTemperature() {
        LocalDateTime timestamp = LocalDateTime.now();
        Double temp = 75.00;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(timestamp)
                                                      .temp(temp)
                                                      .build();
        Temperature temperature = TemperatureEntityMapper.toEntity(temperatureDto);
        when(temperatureRepository.getTemperatureByTimestamp(timestamp)).thenReturn(Optional.of(temperature));
        doNothing().when(temperatureRepository).deleteTemperatureByTimestamp(timestamp);

        temperatureService.deleteTemperatureByTimestamp(timestamp);

        verify(temperatureRepository, times(1)).getTemperatureByTimestamp(timestamp);
        verify(temperatureRepository, times(1)).deleteTemperatureByTimestamp(timestamp);
    }

    @Test
    @DisplayName("deleteTemperatureByTimestamp throws ResourceNotFoundException with invalid timestamp.")
    void givenInvalidTimestamp_whenDeletingTemperatureByTimestamp_thenThrowResourceNotFoundException() {
        LocalDateTime timestamp = LocalDateTime.now();
        when(temperatureRepository.getTemperatureByTimestamp(timestamp)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> temperatureService.deleteTemperatureByTimestamp(timestamp))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Temperature with timestamp=%s not found.", timestamp));
        verify(temperatureRepository).getTemperatureByTimestamp(timestamp);
        verify(temperatureRepository, times(0)).deleteTemperatureByTimestamp(timestamp);
    }
}
