package api.arduinothermohygrometer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.model.Battery;

public interface BatteryService {
  Battery getBatteryById(UUID id) throws ResourceNotFoundException;

  List<Battery> getBatteriesByDateOrTimestamp(LocalDateTime registeredAt, boolean dateOnly)
      throws ResourceNotFoundException;

  Battery createBattery(Battery battery) throws ResourceNotCreatedException;

  void deleteBatteriesByDateOrTimestamp(LocalDateTime registeredAt, boolean dateOnly)
      throws ResourceNotFoundException;
}
