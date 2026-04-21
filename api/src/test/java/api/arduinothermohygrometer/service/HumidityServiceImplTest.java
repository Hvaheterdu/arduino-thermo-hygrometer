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

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.HumidityEntityMapper;
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
    @DisplayName("getHumiditiesByDateOrTimestamp returns humidity with valid timestamp.")
    void givenValidTimestamp_whenGettingHumiditiesByDateOrTimestamp_thenReturnHumidity() {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now();
        Double airHumidity = 75.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(dateTime)
                                             .airHumidity(airHumidity)
                                             .build();
        List<HumidityDto> humidityDtos = List.of(humidityDto);
        List<Humidity> humidities = List.of(HumidityEntityMapper.toEntity(humidityDto));
        when(humidityRepository.getHumidityByTimestamp(dateTime)).thenReturn(humidities);

        List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(humidityRepository, times(1)).getHumidityByTimestamp(dateTime);
        assertThat(result)
            .hasSameElementsAs(humidityDtos);
    }

    @Test
    @DisplayName("getHumiditiesByDateOrTimestamp throws ResourceNotFoundException with invalid timestamp.")
    void givenInvalidTimestamp_whenGettingHumiditiesByDateOrTimestamp_thenThrowResourceNotFoundException() {
        boolean checkOnlyDate = false;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(humidityRepository.getHumidityByTimestamp(invalidDateTime)).thenReturn(emptyList());

        assertThatThrownBy(() -> humidityService.getHumiditiesByDateOrTimestamp(invalidDateTime, checkOnlyDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Humidities with dateTime=%s not found.", invalidDateTime));
    }

    @Test
    @DisplayName("getHumiditiesByDateOrTimestamp returns humidities with valid date.")
    void givenValidDate_whenGettingHumiditiesByDateOrTimestamp_thenReturnHumidities() {
        boolean checkOnlyDate = true;
        LocalDateTime dateTime = LocalDateTime.now();
        Double airHumidity = 75.00;
        Double airHumidity2 = 80.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(dateTime)
                                             .airHumidity(airHumidity)
                                             .build();
        HumidityDto humidityDto2 = HumidityDto.builder()
                                              .registeredAt(dateTime.minusHours(1))
                                              .airHumidity(airHumidity2)
                                              .build();
        List<HumidityDto> humidityDtos = List.of(humidityDto, humidityDto2);
        List<Humidity> humidities = humidityDtos.stream()
                                                .map(HumidityEntityMapper::toEntity)
                                                .toList();
        when(humidityRepository.getHumiditiesByDate(dateTime.toLocalDate())).thenReturn(humidities);

        List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(humidityRepository, times(1)).getHumiditiesByDate(dateTime.toLocalDate());
        assertThat(result)
            .hasSameElementsAs(humidityDtos);
    }

    @Test
    @DisplayName("getHumiditiesByDateOrTimestamp throws ResourceNotFoundException with invalid date.")
    void givenInvalidDate_whenGettingHumiditiesByDate_thenThrowResourceNotFoundException() {
        boolean checkOnlyDate = true;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(humidityRepository.getHumiditiesByDate(invalidDateTime.toLocalDate())).thenReturn(emptyList());

        assertThatThrownBy(() -> humidityService.getHumiditiesByDateOrTimestamp(invalidDateTime, checkOnlyDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Humidities with dateTime=%s not found.", invalidDateTime));
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
    }

    @Test
    @DisplayName("deleteHumidityById throws ResourceNotFoundException with invalid id.")
    void givenInvalidId_whenDeletingHumidityById_thenThrowResourceNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(humidityRepository.getHumidityById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> humidityService.deleteHumidityById(invalidId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Humidity with id=%s not found.", invalidId));
    }

    @Test
    @DisplayName("deleteHumiditiesByTimestamp deletes humidity with valid timestamp.")
    void givenValidTimestamp_whenDeletingHumiditiesByTimestamp_thenDeleteHumidity() {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now();
        Double airHumidity = 75.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(dateTime)
                                             .airHumidity(airHumidity)
                                             .build();
        List<Humidity> humidities = List.of(HumidityEntityMapper.toEntity(humidityDto));
        when(humidityRepository.getHumidityByTimestamp(dateTime)).thenReturn(humidities);
        doNothing().when(humidityRepository).deleteHumidityByTimestamp(dateTime);

        humidityService.deleteHumiditiesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(humidityRepository, times(1)).getHumidityByTimestamp(dateTime);
        verify(humidityRepository, times(1)).deleteHumidityByTimestamp(dateTime);
    }

    @Test
    @DisplayName("deleteHumiditiesByDateOrTimestamp throws ResourceNotFoundException with invalid timestamp.")
    void givenInvalidTimestamp_whenDeletingHumiditiesByDateOrTimestamp_thenThrowResourceNotFoundException() {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now();
        when(humidityRepository.getHumidityByTimestamp(dateTime)).thenReturn(emptyList());

        assertThatThrownBy(() -> humidityService.deleteHumiditiesByDateOrTimestamp(dateTime, checkOnlyDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Humidities with dateTime=%s not found.", dateTime));
    }

    @Test
    @DisplayName("deleteHumiditiesByDateOrTimestamp deletes humidity with valid date.")
    void givenValidDate_whenDeletingHumiditiesByDateOrTimestamp_thenDeleteHumidity() {
        boolean checkOnlyDate = true;
        LocalDateTime dateTime = LocalDateTime.now();
        Double airHumidity = 75.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(dateTime)
                                             .airHumidity(airHumidity)
                                             .build();
        List<Humidity> humidities = List.of(HumidityEntityMapper.toEntity(humidityDto));
        when(humidityRepository.getHumiditiesByDate(dateTime.toLocalDate())).thenReturn(humidities);
        doNothing().when(humidityRepository).deleteHumiditiesByDate(dateTime.toLocalDate());

        humidityService.deleteHumiditiesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(humidityRepository, times(1)).getHumiditiesByDate(dateTime.toLocalDate());
        verify(humidityRepository, times(1)).deleteHumiditiesByDate(dateTime.toLocalDate());
    }

    @Test
    @DisplayName("deleteHumiditiesByDateOrTimestamp throws ResourceNotFoundException with invalid date.")
    void givenInvalidDate_whenDeletingHumiditiesByDateOrTimestamp_thenThrowResourceNotFoundException() {
        boolean checkOnlyDate = true;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(humidityRepository.getHumiditiesByDate(invalidDateTime.toLocalDate())).thenReturn(emptyList());

        assertThatThrownBy(() -> humidityService.deleteHumiditiesByDateOrTimestamp(invalidDateTime, checkOnlyDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Humidities with dateTime=%s not found.", invalidDateTime));
    }
}
