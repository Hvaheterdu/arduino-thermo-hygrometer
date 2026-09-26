package api.arduinothermohygrometer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.model.Humidity;

public interface HumidityService {
  Humidity getHumidityById(UUID id) throws ResourceNotFoundException;

  List<Humidity> getHumiditiesByDateOrTimestamp(LocalDateTime registeredAt, boolean dateOnly)
      throws ResourceNotFoundException;

  Humidity createHumidity(Humidity humidity) throws ResourceNotCreatedException;

  void deleteHumiditiesByDateOrTimestamp(LocalDateTime registeredAt, boolean dateOnly)
      throws ResourceNotFoundException;
}
