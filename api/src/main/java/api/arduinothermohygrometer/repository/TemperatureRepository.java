package api.arduinothermohygrometer.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.arduinothermohygrometer.model.Temperature;

public interface TemperatureRepository {
    Optional<Temperature> getTemperatureById(UUID id);

    List<Temperature> getTemperatureByTimestamp(LocalDateTime timestamp);

    List<Temperature> getTemperaturesByDate(LocalDate date);

    Optional<Temperature> createTemperature(Temperature temperature);

    void deleteTemperatureById(UUID id);

    void deleteTemperatureByTimestamp(LocalDateTime timestamp);

    void deleteTemperaturesByDate(LocalDate date);
}
