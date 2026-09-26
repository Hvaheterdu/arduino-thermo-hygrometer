package api.arduinothermohygrometer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.model.Temperature;

public interface TemperatureService {
  Temperature getTemperatureById(UUID id) throws ResourceNotFoundException;

  List<Temperature> getTemperaturesByDateOrTimestamp(LocalDateTime registeredAt, boolean dateOnly)
      throws ResourceNotFoundException;

  Temperature createTemperature(Temperature temperature) throws ResourceNotCreatedException;

  void deleteTemperaturesByDateOrTimestamp(LocalDateTime registeredAt, boolean dateOnly)
      throws ResourceNotFoundException;
}
