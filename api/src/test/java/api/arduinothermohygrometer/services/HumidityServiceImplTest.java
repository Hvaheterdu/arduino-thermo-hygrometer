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

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.entities.Humidity;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.mappers.HumidityEntityMapper;
import api.arduinothermohygrometer.repositories.HumidityRepository;
import api.arduinothermohygrometer.services.implementations.HumidityServiceImpl;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for HumidityServiceImpl")
@ExtendWith(MockitoExtension.class)
class HumidityServiceImplTest {
    @Mock
    private HumidityRepository humidityRepository;

    @Captor
    private ArgumentCaptor<Humidity> humidityArgumentCaptor;

    private HumidityServiceImpl humidityService;

    @BeforeEach
    void setUp() {
        humidityService = new HumidityServiceImpl(humidityRepository);
        humidityArgumentCaptor = ArgumentCaptor.forClass(Humidity.class);
    }

    @Test
    @DisplayName("getHumidityById returns humidity with valid id.")
    void givenValidId_whenGettingHumidityById_thenReturnHumidity() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double airHumidity = 70.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(registeredAt)
                                             .airHumidity(airHumidity)
                                             .build();
        Humidity humidity = HumidityEntityMapper.toEntity(humidityDto);
        UUID id = humidity.getId();
        when(humidityRepository.getHumidityById(id)).thenReturn(Optional.of(humidity));

        HumidityDto result = humidityService.getHumidityById(id);

        assertThat(result)
            .isEqualTo(humidityDto);
    }

    @Test
    @DisplayName("getHumidityById throws ResourceNotFoundException with empty id.")
    void givenEmptyId_whenGettingHumidityById_thenThrowResourceNotFoundException() {
        UUID emptyId = new UUID(0, 0);

        assertThatThrownBy(() -> humidityService.getHumidityById(emptyId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Humidity with empty id=%s does not exist.", emptyId));
        verifyNoInteractions(humidityRepository);
    }

    @Test
    @DisplayName("getHumidityById throws ResourceNotFoundException with invalid id.")
    void givenInvalidId_whenGettingHumidityById_thenThrowResourceNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(humidityRepository.getHumidityById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> humidityService.getHumidityById(invalidId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Humidity with id=%s not found.", invalidId));
    }

    @Test
    @DisplayName("getHumidityByTimestamp returns humidity with valid timestamp.")
    void givenValidTimestamp_whenGettingHumidityByTimestamp_thenReturnHumidity() {
        LocalDateTime timestamp = LocalDateTime.now();
        Double airHumidity = 75.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(timestamp)
                                             .airHumidity(airHumidity)
                                             .build();
        Humidity humidity = HumidityEntityMapper.toEntity(humidityDto);
        when(humidityRepository.getHumidityByTimestamp(timestamp)).thenReturn(Optional.of(humidity));

        HumidityDto result = humidityService.getHumidityByTimestamp(timestamp);

        assertThat(result)
            .isEqualTo(humidityDto);
    }

    @Test
    @DisplayName("getHumidityByTimestamp throws ResourceNotFoundException with invalid timestamp.")
    void givenInvalidTimestamp_whenGettingHumidityByTimestamp_thenThrowResourceNotFoundException() {
        LocalDateTime invalidTimestamp = LocalDateTime.now();
        when(humidityRepository.getHumidityByTimestamp(invalidTimestamp)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> humidityService.getHumidityByTimestamp(invalidTimestamp))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Humidity with timestamp=%s not found.", invalidTimestamp));
    }

    @Test
    @DisplayName("getHumiditiesByDate returns humidities with valid date.")
    void givenValidDate_whenGettingHumiditiesByDate_thenReturnHumidities() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double airHumidity = 75.00;
        LocalDateTime registeredAt2 = LocalDateTime.now();
        Double airHumidity2 = 80.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(registeredAt)
                                             .airHumidity(airHumidity)
                                             .build();
        HumidityDto humidityDto2 = HumidityDto.builder()
                                              .registeredAt(registeredAt2)
                                              .airHumidity(airHumidity2)
                                              .build();
        List<HumidityDto> humidityDtos = List.of(humidityDto, humidityDto2);
        List<Humidity> humidities = humidityDtos.stream()
                                                .map(HumidityEntityMapper::toEntity)
                                                .toList();
        when(humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate())).thenReturn(humidities);

        List<HumidityDto> result = humidityService.getHumiditiesByDate(registeredAt.toLocalDate());

        assertThat(result)
            .hasSameElementsAs(humidityDtos);
    }

    @Test
    @DisplayName("getHumiditiesByDate throws ResourceNotFoundException with invalid date.")
    void givenInvalidDate_whenGettingHumiditiesByDate_thenThrowResourceNotFoundException() {
        LocalDate invalidDate = LocalDate.now();
        when(humidityRepository.getHumiditiesByDate(invalidDate)).thenReturn(emptyList());

        assertThatThrownBy(() -> humidityService.getHumiditiesByDate(invalidDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Humidities with date=%s not found.", invalidDate));
    }

    @Test
    @DisplayName("createHumidity returns created humidity with valid humidity model.")
    void givenValidHumidityModel_whenCreatingHumidity_thenReturnCreatedHumidity() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double airHumidity = 75.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(registeredAt)
                                             .airHumidity(airHumidity)
                                             .build();
        doNothing().when(humidityRepository).createHumidity(any());

        HumidityDto result = humidityService.createHumidity(humidityDto);

        verify(humidityRepository, times(1)).createHumidity(humidityArgumentCaptor.capture());
        assertThat(result)
            .isEqualTo(humidityDto);
    }

    @Test
    @DisplayName("createHumidity throws ResourceNotCreatedException with invalid humidity model.")
    void givenInvalidHumidityModel_whenCreatingHumidity_thenThrowResourceNotCreatedException() {
        assertThatThrownBy(() -> humidityService.createHumidity(null))
            .isInstanceOf(ResourceNotCreatedException.class)
            .hasMessage("Humidity cannot be created.");
        verifyNoInteractions(humidityRepository);
    }

    @Test
    @DisplayName("deleteHumidityById deletes humidity with valid id.")
    void givenValidId_whenDeletingHumidityById_thenDeleteHumidity() {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double airHumidity = 75.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(registeredAt)
                                             .airHumidity(airHumidity)
                                             .build();
        Humidity humidity = HumidityEntityMapper.toEntity(humidityDto);
        UUID id = humidity.getId();
        when(humidityRepository.getHumidityById(id)).thenReturn(Optional.of(humidity));
        doNothing().when(humidityRepository).deleteHumidityById(id);

        humidityService.deleteHumidityById(id);

        verify(humidityRepository, times(1)).getHumidityById(id);
        verify(humidityRepository, times(1)).deleteHumidityById(id);
    }

    @Test
    @DisplayName("deleteHumidityById throws ResourceNotFoundException with empty id.")
    void givenEmptyId_whenDeletingHumidityById_thenThrowResourceNotFoundException() {
        UUID emptyId = new UUID(0, 0);

        assertThatThrownBy(() -> humidityService.deleteHumidityById(emptyId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Humidity with empty id=%s does not exist.", emptyId));
        verifyNoInteractions(humidityRepository);
    }

    @Test
    @DisplayName("deleteHumidityById throws ResourceNotFoundException with invalid id.")
    void givenInvalidId_whenDeletingHumidityById_thenThrowResourceNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(humidityRepository.getHumidityById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> humidityService.deleteHumidityById(invalidId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Humidity with id=%s not found.", invalidId));
        verify(humidityRepository, times(1)).getHumidityById(invalidId);
        verify(humidityRepository, times(0)).deleteHumidityById(invalidId);
    }

    @Test
    @DisplayName("deleteHumidityByTimestamp deletes humidity with valid timestamp.")
    void givenValidTimestamp_whenDeletingHumidityByTimestamp_thenDeleteHumidity() {
        LocalDateTime timestamp = LocalDateTime.now();
        Double airHumidity = 75.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(timestamp)
                                             .airHumidity(airHumidity)
                                             .build();
        Humidity humidity = HumidityEntityMapper.toEntity(humidityDto);
        when(humidityRepository.getHumidityByTimestamp(timestamp)).thenReturn(Optional.of(humidity));
        doNothing().when(humidityRepository).deleteHumidityByTimestamp(timestamp);

        humidityService.deleteHumidityByTimestamp(timestamp);

        verify(humidityRepository, times(1)).getHumidityByTimestamp(timestamp);
        verify(humidityRepository, times(1)).deleteHumidityByTimestamp(timestamp);
    }

    @Test
    @DisplayName("deleteHumidityByTimestamp throws ResourceNotFoundException with invalid timestamp.")
    void givenInvalidTimestamp_whenDeletingHumidityByTimestamp_thenThrowResourceNotFoundException() {
        LocalDateTime timestamp = LocalDateTime.now();
        when(humidityRepository.getHumidityByTimestamp(timestamp)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> humidityService.deleteHumidityByTimestamp(timestamp))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Humidity with timestamp=%s not found.", timestamp));
        verify(humidityRepository).getHumidityByTimestamp(timestamp);
        verify(humidityRepository, times(0)).deleteHumidityByTimestamp(timestamp);
    }
}
