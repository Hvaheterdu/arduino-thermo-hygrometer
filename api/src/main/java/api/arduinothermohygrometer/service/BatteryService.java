package api.arduinothermohygrometer.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;

public interface BatteryService {
    BatteryDto getBatteryById(UUID id) throws ResourceNotFoundException;

    BatteryDto getBatteryByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;

    List<BatteryDto> getBatteriesByDate(LocalDate date);

    BatteryDto createBattery(BatteryDto batteryDto) throws ResourceNotCreatedException;

    void deleteBatteryById(UUID id) throws ResourceNotFoundException;

    void deleteBatteryByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;

}
