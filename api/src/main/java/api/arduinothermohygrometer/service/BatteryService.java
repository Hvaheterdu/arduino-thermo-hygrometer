package api.arduinothermohygrometer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;

public interface BatteryService {
    BatteryDto getBatteryById(UUID id) throws ResourceNotFoundException;

    List<BatteryDto> getBatteriesByDateOrTimestamp(LocalDateTime registeredAt, boolean dateOnly) throws ResourceNotFoundException;

    BatteryDto createBattery(BatteryDto batteryDto) throws ResourceNotCreatedException;

    void deleteBatteryById(UUID id) throws ResourceNotFoundException;

    void deleteBatteriesByDateOrTimestamp(LocalDateTime registeredAt, boolean dateOnly) throws ResourceNotFoundException;
}
