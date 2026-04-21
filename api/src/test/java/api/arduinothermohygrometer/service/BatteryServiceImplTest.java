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

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.BatteryEntityMapper;
import api.arduinothermohygrometer.model.Battery;
import api.arduinothermohygrometer.repository.BatteryRepository;
import api.arduinothermohygrometer.service.implementation.BatteryServiceImpl;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for BatteryServiceImpl")
@ExtendWith(MockitoExtension.class)
class BatteryServiceImplTest {
    @Mock
    private BatteryRepository batteryRepository;

    @Captor
    private ArgumentCaptor<Battery> batteryArgumentCaptor;

    private BatteryServiceImpl batteryService;

    @BeforeEach
    void setUp() {
        batteryService = new BatteryServiceImpl(batteryRepository);
        batteryArgumentCaptor = ArgumentCaptor.forClass(Battery.class);
    }

    @Test
    @DisplayName("getBatteryById returns battery with valid id.")
    void givenValidId_whenGettingBatteryById_thenReturnBattery() {
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();
        Battery battery = BatteryEntityMapper.toEntity(batteryDto);
        UUID id = battery.getId();
        when(batteryRepository.getBatteryById(id)).thenReturn(Optional.of(battery));

        BatteryDto result = batteryService.getBatteryById(id);

        assertThat(result)
            .isEqualTo(batteryDto);
    }

    @Test
    @DisplayName("getBatteryById throws ResourceNotFoundException with empty id.")
    void givenEmptyId_whenGettingBatteryById_thenThrowResourceNotFoundException() {
        UUID emptyId = new UUID(0, 0);

        assertThatThrownBy(() -> batteryService.getBatteryById(emptyId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Battery with empty id=%s does not exist.", emptyId));
    }

    @Test
    @DisplayName("getBatteryById throws ResourceNotFoundException with invalid id.")
    void givenInvalidId_whenGettingBatteryById_thenThrowResourceNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(batteryRepository.getBatteryById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> batteryService.getBatteryById(invalidId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Battery with id=%s not found.", invalidId));
    }

    @Test
    @DisplayName("getBatteriesByDateOrTimestamp returns battery with valid timestamp.")
    void givenValidTimestamp_whenGettingBatteriesByDateOrTimestamp_thenReturnBattery() {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now();
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(dateTime)
                                          .batteryStatus(batteryStatus)
                                          .build();
        List<BatteryDto> batteryDtos = List.of(batteryDto);
        List<Battery> batteries = List.of(BatteryEntityMapper.toEntity(batteryDto));
        when(batteryRepository.getBatteryByTimestamp(dateTime)).thenReturn(batteries);

        List<BatteryDto> result = batteryService.getBatteriesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(batteryRepository, times(1)).getBatteryByTimestamp(dateTime);
        assertThat(result)
            .hasSameElementsAs(batteryDtos);
    }

    @Test
    @DisplayName("getBatteriesByDateOrTimestamp throws ResourceNotFoundException with invalid timestamp.")
    void givenInvalidTimestamp_whenGettingBatteriesByDateOrTimestamp_thenThrowResourceNotFoundException() {
        boolean checkOnlyDate = false;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(batteryRepository.getBatteryByTimestamp(invalidDateTime)).thenReturn(emptyList());

        assertThatThrownBy(() -> batteryService.getBatteriesByDateOrTimestamp(invalidDateTime, checkOnlyDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Batteries with dateTime=%s not found.", invalidDateTime));
    }

    @Test
    @DisplayName("getBatteriesByDateOrTimestamp returns batteries with valid date.")
    void givenValidDate_whenGettingBatteriesByDateOrTimestamp_thenReturnBatteries() {
        boolean checkOnlyDate = true;
        LocalDateTime dateTime = LocalDateTime.now();
        int batteryStatus = 90;
        int batteryStatus2 = 95;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(dateTime)
                                          .batteryStatus(batteryStatus)
                                          .build();
        BatteryDto batteryDto2 = BatteryDto.builder()
                                           .registeredAt(dateTime.minusHours(1))
                                           .batteryStatus(batteryStatus2)
                                           .build();
        List<BatteryDto> batteryDtos = List.of(batteryDto, batteryDto2);
        List<Battery> batteries = batteryDtos.stream()
                                             .map(BatteryEntityMapper::toEntity)
                                             .toList();
        when(batteryRepository.getBatteriesByDate(dateTime.toLocalDate())).thenReturn(batteries);

        List<BatteryDto> result = batteryService.getBatteriesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(batteryRepository, times(1)).getBatteriesByDate(dateTime.toLocalDate());
        assertThat(result)
            .hasSameElementsAs(batteryDtos);
    }

    @Test
    @DisplayName("getBatteriesByDateOrTimestamp throws ResourceNotFoundException with invalid date.")
    void givenInvalidDate_whenGettingBatteriesByDateOrTimestamp_thenThrowResourceNotFoundException() {
        boolean checkOnlyDate = true;
        LocalDateTime invalidDateTime = LocalDateTime.now();
        when(batteryRepository.getBatteriesByDate(invalidDateTime.toLocalDate())).thenReturn(emptyList());

        assertThatThrownBy(() -> batteryService.getBatteriesByDateOrTimestamp(invalidDateTime, checkOnlyDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Batteries with dateTime=%s not found.", invalidDateTime));
    }

    @Test
    @DisplayName("createBattery returns created battery with valid battery model.")
    void givenValidBatteryModel_whenCreatingBattery_thenReturnCreatedBattery() {
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();
        doNothing().when(batteryRepository).createBattery(any());

        BatteryDto result = batteryService.createBattery(batteryDto);

        verify(batteryRepository, times(1)).createBattery(batteryArgumentCaptor.capture());
        assertThat(result)
            .isEqualTo(batteryDto);
    }

    @Test
    @DisplayName("createBattery throws ResourceNotCreatedException with invalid battery model.")
    void givenInvalidBatteryModel_whenCreatingBattery_thenThrowResourceNotCreatedException() {
        assertThatThrownBy(() -> batteryService.createBattery(null))
            .isInstanceOf(ResourceNotCreatedException.class)
            .hasMessage("Battery cannot be created.");
    }

    @Test
    @DisplayName("deleteBatteryById deletes battery with valid id.")
    void givenValidId_whenDeletingBatteryById_thenDeleteBattery() {
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();
        Battery battery = BatteryEntityMapper.toEntity(batteryDto);
        UUID id = battery.getId();
        when(batteryRepository.getBatteryById(id)).thenReturn(Optional.of(battery));
        doNothing().when(batteryRepository).deleteBatteryById(id);

        batteryService.deleteBatteryById(id);

        verify(batteryRepository, times(1)).getBatteryById(id);
        verify(batteryRepository, times(1)).deleteBatteryById(id);
    }

    @Test
    @DisplayName("deleteBatteryById throws ResourceNotFoundException with empty id.")
    void givenEmptyId_whenDeletingBatteryById_thenThrowResourceNotFoundException() {
        UUID emptyId = new UUID(0, 0);

        assertThatThrownBy(() -> batteryService.deleteBatteryById(emptyId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Battery with empty id=%s does not exist.", emptyId));
    }

    @Test
    @DisplayName("deleteBatteryById throws ResourceNotFoundException with invalid id.")
    void givenInvalidId_whenDeletingBatteryById_thenThrowResourceNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(batteryRepository.getBatteryById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> batteryService.deleteBatteryById(invalidId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Battery with id=%s not found.", invalidId));
    }

    @Test
    @DisplayName("deleteBatteryByDateOrTimestamp deletes battery with valid timestamp.")
    void givenValidTimestamp_whenDeletingBatteryByDateOrTimestamp_thenDeleteBattery() {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now();
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(dateTime)
                                          .batteryStatus(batteryStatus)
                                          .build();
        List<Battery> batteries = List.of(BatteryEntityMapper.toEntity(batteryDto));
        when(batteryRepository.getBatteryByTimestamp(dateTime)).thenReturn(batteries);
        doNothing().when(batteryRepository).deleteBatteryByTimestamp(dateTime);

        batteryService.deleteBatteriesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(batteryRepository, times(1)).getBatteryByTimestamp(dateTime);
        verify(batteryRepository, times(1)).deleteBatteryByTimestamp(dateTime);
    }

    @Test
    @DisplayName("deleteBatteryByDateOrTimestamp throws ResourceNotFoundException with invalid timestamp.")
    void givenInvalidTimestamp_whenDeletingBatteryByDateOrTimestamp_thenThrowResourceNotFoundException() {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now();
        when(batteryRepository.getBatteryByTimestamp(dateTime)).thenReturn(emptyList());

        assertThatThrownBy(() -> batteryService.deleteBatteriesByDateOrTimestamp(dateTime, checkOnlyDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Batteries with dateTime=%s not found.", dateTime));
    }

    @Test
    @DisplayName("deleteBatteryByDateOrTimestamp deletes battery with valid date.")
    void givenValidDate_whenDeletingBatteryByDateOrTimestamp_thenDeleteBattery() {
        boolean checkOnlyDate = true;
        LocalDateTime dateTime = LocalDateTime.now();
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(dateTime)
                                          .batteryStatus(batteryStatus)
                                          .build();
        BatteryDto batteryDto2 = BatteryDto.builder()
                                           .registeredAt(dateTime.minusHours(1))
                                           .batteryStatus(batteryStatus)
                                           .build();
        List<BatteryDto> batteryDtos = List.of(batteryDto, batteryDto2);
        List<Battery> batteries = batteryDtos.stream()
                                             .map(BatteryEntityMapper::toEntity)
                                             .toList();
        when(batteryRepository.getBatteriesByDate(dateTime.toLocalDate())).thenReturn(batteries);
        doNothing().when(batteryRepository).deleteBatteriesByDate(dateTime.toLocalDate());

        batteryService.deleteBatteriesByDateOrTimestamp(dateTime, checkOnlyDate);

        verify(batteryRepository, times(1)).getBatteriesByDate(dateTime.toLocalDate());
        verify(batteryRepository, times(1)).deleteBatteriesByDate(dateTime.toLocalDate());
    }

    @Test
    @DisplayName("deleteBatteryByDateOrTimestamp throws ResourceNotFoundException with invalid date.")
    void givenInvalidDate_whenDeletingBatteryByDateOrTimestamp_thenThrowResourceNotFoundException() {
        boolean checkOnlyDate = true;
        LocalDateTime dateTime = LocalDateTime.now();
        when(batteryRepository.getBatteriesByDate(dateTime.toLocalDate())).thenReturn(emptyList());

        assertThatThrownBy(() -> batteryService.deleteBatteriesByDateOrTimestamp(dateTime, checkOnlyDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Batteries with dateTime=%s not found.", dateTime));
    }
}
