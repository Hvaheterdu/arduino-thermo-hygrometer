package api.arduinothermohygrometer.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;

public interface BatteryService {
    BatteryDto getBatteryDtoById(UUID id) throws ResourceNotFoundException;

    BatteryDto getBatteryDtoByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;

    List<BatteryDto> getBatteryDtosByDate(LocalDate date);

    BatteryDto createBatteryDto(BatteryDto batteryDto) throws ResourceNotCreatedException;

    void deleteBatteryById(UUID id) throws ResourceNotFoundException;

    void deleteBatteryByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;

}
