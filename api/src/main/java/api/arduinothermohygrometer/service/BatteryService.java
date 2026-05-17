package api.arduinothermohygrometer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;

public interface BatteryService {
    BatteryDto getBatteryById(final UUID id) throws ResourceNotFoundException;

    List<BatteryDto> getBatteriesByDateOrTimestamp(final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException;

    BatteryDto createBattery(final BatteryDto batteryDto) throws ResourceNotCreatedException;

    void deleteBatteryById(final UUID id) throws ResourceNotFoundException;

    void deleteBatteriesByDateOrTimestamp(final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException;
}
