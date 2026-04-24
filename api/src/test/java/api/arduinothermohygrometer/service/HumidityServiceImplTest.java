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

@DisplayName("Unit tests for HumidityServiceImpl.")
@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
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
        Humidity humidity = HumidityModelMapper.toModel(humidityDto);
        UUID id = humidity.getId();
        when(humidityRepository.getHumidityById(id)).thenReturn(Optional.of(humidity));

        HumidityDto result = humidityService.getHumidityById(id);

        assertThat(result)
            .isEqualTo(humidityDto);
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
        List<Humidity> humidities = List.of(HumidityModelMapper.toModel(humidityDto));
        when(humidityRepository.getHumidityByTimestamp(dateTime)).thenReturn(humidities);

        List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(humidityRepository, times(1)).getHumidityByTimestamp(dateTime);
        assertThat(result)
            .hasSameElementsAs(humidityDtos);
    }

    @Test
    @DisplayName("getHumiditiesByDateOrTimestamp returns empty list with invalid timestamp.")
    void givenInvalidTimestamp_whenGettingHumiditiesByDateOrTimestamp_thenReturnEmptyList() {
        boolean checkOnlyDate = false;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(humidityRepository.getHumidityByTimestamp(invalidDateTime)).thenReturn(emptyList());

        List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(invalidDateTime, checkOnlyDate);

        verify(humidityRepository, times(1)).getHumidityByTimestamp(invalidDateTime);
        assertThat(result)
            .isEmpty();
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
                                                .map(HumidityModelMapper::toModel)
                                                .toList();
        when(humidityRepository.getHumiditiesByDate(dateTime.toLocalDate())).thenReturn(humidities);

        List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(humidityRepository, times(1)).getHumiditiesByDate(dateTime.toLocalDate());
        assertThat(result)
            .hasSameElementsAs(humidityDtos);
    }

    @Test
    @DisplayName("getHumiditiesByDateOrTimestamp returns empty list with invalid date.")
    void givenInvalidDate_whenGettingHumiditiesByDate_thenReturnEmptyList() {
        boolean checkOnlyDate = true;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(humidityRepository.getHumiditiesByDate(invalidDateTime.toLocalDate())).thenReturn(emptyList());

        List<HumidityDto> result = humidityService.getHumiditiesByDateOrTimestamp(invalidDateTime, checkOnlyDate);

        verify(humidityRepository, times(1)).getHumiditiesByDate(invalidDateTime.toLocalDate());
        assertThat(result)
            .isEmpty();
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
    void givenValidId_whenDeletingHumidityById_thenDeleteHumidity(CapturedOutput capturedOutput) {
        LocalDateTime registeredAt = LocalDateTime.now();
        Double airHumidity = 75.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(registeredAt)
                                             .airHumidity(airHumidity)
                                             .build();
        Humidity humidity = HumidityModelMapper.toModel(humidityDto);
        UUID id = humidity.getId();
        when(humidityRepository.getHumidityById(id)).thenReturn(Optional.of(humidity));
        doNothing().when(humidityRepository).deleteHumidityById(id);

        humidityService.deleteHumidityById(id);

        verify(humidityRepository, times(1)).getHumidityById(id);
        verify(humidityRepository, times(1)).deleteHumidityById(id);
        assertThat(capturedOutput)
            .contains(String.format("Humidity with id=%s deleted.", id));
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
    void givenValidTimestamp_whenDeletingHumiditiesByTimestamp_thenDeleteHumidity(CapturedOutput capturedOutput) {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now();
        Double airHumidity = 75.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(dateTime)
                                             .airHumidity(airHumidity)
                                             .build();
        List<Humidity> humidities = List.of(HumidityModelMapper.toModel(humidityDto));
        when(humidityRepository.getHumidityByTimestamp(dateTime)).thenReturn(humidities);
        doNothing().when(humidityRepository).deleteHumidityByTimestamp(dateTime);

        humidityService.deleteHumiditiesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(humidityRepository, times(1)).getHumidityByTimestamp(dateTime);
        verify(humidityRepository, times(1)).deleteHumidityByTimestamp(dateTime);
        assertThat(capturedOutput)
            .contains(String.format("Deleted humidity with timestamp=%s.", humidities.getFirst().getRegisteredAt()));
    }

    @Test
    @DisplayName("deleteHumiditiesByDateOrTimestamp returns with invalid timestamp.")
    void givenInvalidTimestamp_whenDeletingHumiditiesByDateOrTimestamp_thenReturn(CapturedOutput capturedOutput) {
        boolean checkOnlyDate = false;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(humidityRepository.getHumidityByTimestamp(invalidDateTime)).thenReturn(emptyList());

        humidityService.deleteHumiditiesByDateOrTimestamp(invalidDateTime, checkOnlyDate);

        verify(humidityRepository, times(1)).getHumidityByTimestamp(invalidDateTime);
        verify(humidityRepository, times(0)).deleteHumidityByTimestamp(invalidDateTime);
        assertThat(capturedOutput)
            .contains(String.format("Humidities with dateTime=%s not found.", invalidDateTime));
    }

    @Test
    @DisplayName("deleteHumiditiesByDateOrTimestamp deletes humidity with valid date.")
    void givenValidDate_whenDeletingHumiditiesByDateOrTimestamp_thenDeleteHumidity(CapturedOutput capturedOutput) {
        boolean checkOnlyDate = true;
        LocalDateTime dateTime = LocalDateTime.now();
        Double airHumidity = 75.00;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(dateTime)
                                             .airHumidity(airHumidity)
                                             .build();
        List<Humidity> humidities = List.of(HumidityModelMapper.toModel(humidityDto));
        when(humidityRepository.getHumiditiesByDate(dateTime.toLocalDate())).thenReturn(humidities);
        doNothing().when(humidityRepository).deleteHumiditiesByDate(dateTime.toLocalDate());

        humidityService.deleteHumiditiesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(humidityRepository, times(1)).getHumiditiesByDate(dateTime.toLocalDate());
        verify(humidityRepository, times(1)).deleteHumiditiesByDate(dateTime.toLocalDate());
        assertThat(capturedOutput)
            .contains(String.format("Deleted humidities with date=%s.", humidities.getFirst().getRegisteredAt().toLocalDate()));
    }

    @Test
    @DisplayName("deleteHumiditiesByDateOrTimestamp returns with invalid date.")
    void givenInvalidDate_whenDeletingHumiditiesByDateOrTimestamp_thenReturn(CapturedOutput capturedOutput) {
        boolean checkOnlyDate = true;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(humidityRepository.getHumiditiesByDate(invalidDateTime.toLocalDate())).thenReturn(emptyList());

        humidityService.deleteHumiditiesByDateOrTimestamp(invalidDateTime, checkOnlyDate);

        verify(humidityRepository, times(1)).getHumiditiesByDate(invalidDateTime.toLocalDate());
        verify(humidityRepository, times(0)).deleteHumiditiesByDate(invalidDateTime.toLocalDate());
        assertThat(capturedOutput)
            .contains(String.format("Humidities with dateTime=%s not found.", invalidDateTime));
    }
}
