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

import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.entities.Battery;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.mappers.BatteryEntityMapper;
import api.arduinothermohygrometer.repositories.BatteryRepository;
import api.arduinothermohygrometer.services.implementations.BatteryServiceImpl;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for BatteryServiceImpl")
@ExtendWith(MockitoExtension.class)
class BatteryServiceImplTest {
    @Mock
    private BatteryRepository batteryRepository;

    @Captor
    ArgumentCaptor<Battery> batteryArgumentCaptor;

    private BatteryServiceImpl batteryService;

    @BeforeEach
    void setUp() {
        batteryService = new BatteryServiceImpl(batteryRepository);
        batteryArgumentCaptor = ArgumentCaptor.forClass(Battery.class);
    }

    @Test
    @DisplayName("getBatteryById returns battery with valid id.")
    void givenValidId_whenGettingBatteryById_thenReturnBattery() {
        UUID id = UUID.randomUUID();
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .id(id)
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();
        Battery battery = BatteryEntityMapper.toEntity(batteryDto);
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
        verifyNoInteractions(batteryRepository);
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
    @DisplayName("getBatteryByTimestamp returns battery with valid timestamp.")
    void givenValidTimestamp_whenGettingBatteryByTimestamp_thenReturnBattery() {
        UUID id = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .id(id)
                                          .registeredAt(timestamp)
                                          .batteryStatus(batteryStatus)
                                          .build();
        Battery battery = BatteryEntityMapper.toEntity(batteryDto);
        when(batteryRepository.getBatteryByTimestamp(timestamp)).thenReturn(Optional.of(battery));

        BatteryDto result = batteryService.getBatteryByTimestamp(timestamp);

        assertThat(result)
            .isEqualTo(batteryDto);
    }

    @Test
    @DisplayName("getBatteryByTimestamp throws ResourceNotFoundException with invalid timestamp.")
    void givenInvalidTimestamp_whenGettingBatteryByTimestamp_thenThrowResourceNotFoundException() {
        LocalDateTime invalidTimestamp = LocalDateTime.now();
        when(batteryRepository.getBatteryByTimestamp(invalidTimestamp)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> batteryService.getBatteryByTimestamp(invalidTimestamp))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Battery with timestamp=%s not found.", invalidTimestamp));
    }

    @Test
    @DisplayName("getBatteriesByDate returns batteries with valid date.")
    void givenValidDate_whenGettingBatteriesByDate_thenReturnBatteries() {
        UUID id = UUID.randomUUID();
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 90;
        UUID id2 = UUID.randomUUID();
        LocalDateTime registeredAt2 = LocalDateTime.now();
        int batteryStatus2 = 95;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .id(id)
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();
        BatteryDto batteryDto2 = BatteryDto.builder()
                                           .id(id2)
                                           .registeredAt(registeredAt2)
                                           .batteryStatus(batteryStatus2)
                                           .build();
        List<BatteryDto> batteryDtos = List.of(batteryDto, batteryDto2);
        List<Battery> batteries = batteryDtos.stream()
                                             .map(BatteryEntityMapper::toEntity)
                                             .toList();
        when(batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())).thenReturn(batteries);

        List<BatteryDto> result = batteryService.getBatteriesByDate(registeredAt.toLocalDate());

        assertThat(result)
            .hasSameElementsAs(batteryDtos);
    }

    @Test
    @DisplayName("getBatteriesByDate throws ResourceNotFoundException with invalid date.")
    void givenInvalidDate_whenGettingBatteriesByDate_thenThrowResourceNotFoundException() {
        LocalDate invalidDate = LocalDate.now();
        when(batteryRepository.getBatteriesByDate(invalidDate)).thenReturn(emptyList());

        assertThatThrownBy(() -> batteryService.getBatteriesByDate(invalidDate))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Batteries with date=%s not found.", invalidDate));
    }

    @Test
    @DisplayName("createBattery returns created battery with valid battery model.")
    void givenValidBatteryModel_whenCreatingBattery_thenReturnCreatedBattery() {
        UUID id = UUID.randomUUID();
        LocalDateTime registeredAt = LocalDateTime.now();
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .id(id)
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
        verifyNoInteractions(batteryRepository);
    }

    @Test
    @DisplayName("deleteBatteryById deletes battery with valid id.")
    void givenValidId_whenDeletingBatteryById_thenDeleteBattery() {
        UUID id = UUID.randomUUID();
        LocalDateTime registeredAt = LocalDateTime.now();
        BatteryDto batteryDto = BatteryDto.builder()
                                          .id(id)
                                          .registeredAt(registeredAt)
                                          .batteryStatus(90)
                                          .build();
        Battery battery = BatteryEntityMapper.toEntity(batteryDto);
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
        verifyNoInteractions(batteryRepository);
    }

    @Test
    @DisplayName("deleteBatteryById throws ResourceNotFoundException with invalid id.")
    void givenInvalidId_whenDeletingBatteryById_thenThrowResourceNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(batteryRepository.getBatteryById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> batteryService.deleteBatteryById(invalidId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Battery with id=%s not found.", invalidId));
        verify(batteryRepository, times(1)).getBatteryById(invalidId);
        verify(batteryRepository, times(0)).deleteBatteryById(invalidId);
    }

    @Test
    @DisplayName("deleteBatteryByTimestamp deletes battery with valid timestamp.")
    void givenValidTimestamp_whenDeletingBatteryByTimestamp_thenDeleteBattery() {
        UUID id = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();
        BatteryDto batteryDto = BatteryDto.builder()
                                          .id(id)
                                          .registeredAt(timestamp)
                                          .batteryStatus(90)
                                          .build();
        Battery battery = BatteryEntityMapper.toEntity(batteryDto);
        when(batteryRepository.getBatteryByTimestamp(timestamp)).thenReturn(Optional.of(battery));
        doNothing().when(batteryRepository).deleteBatteryByTimestamp(timestamp);

        batteryService.deleteBatteryByTimestamp(timestamp);

        verify(batteryRepository, times(1)).getBatteryByTimestamp(timestamp);
        verify(batteryRepository, times(1)).deleteBatteryByTimestamp(timestamp);
    }

    @Test
    @DisplayName("deleteBatteryByTimestamp throws ResourceNotFoundException with invalid timestamp.")
    void givenInvalidTimestamp_whenDeletingBatteryByTimestamp_thenThrowResourceNotFoundException() {
        LocalDateTime timestamp = LocalDateTime.now();
        when(batteryRepository.getBatteryByTimestamp(timestamp)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> batteryService.deleteBatteryByTimestamp(timestamp))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage(String.format("Battery with timestamp=%s not found.", timestamp));
        verify(batteryRepository).getBatteryByTimestamp(timestamp);
        verify(batteryRepository, times(0)).deleteBatteryByTimestamp(timestamp);
    }
}
