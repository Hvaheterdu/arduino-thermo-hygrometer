package api.arduinothermohygrometer.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.arduinothermohygrometer.entities.Temperature;

public interface TemperatureRepository {
    Optional<Temperature> getTemperatureById(UUID id);

    Optional<Temperature> getTemperatureByTimestamp(LocalDateTime timestamp);

    List<Temperature> getTemperaturesByDate(LocalDateTime localDateTime);

    void createTemperature(Temperature temperature);

    void deleteTemperatureById(UUID id);

    void deleteTemperatureByTimestamp(LocalDateTime timestamp);
}
