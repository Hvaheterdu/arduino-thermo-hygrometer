package api.arduinothermohygrometer.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.arduinothermohygrometer.model.Temperature;

public interface TemperatureRepository {
    Optional<Temperature> getTemperatureById(final UUID id);

    List<Temperature> getTemperatureByTimestamp(final LocalDateTime timestamp);

    List<Temperature> getTemperaturesByDate(final LocalDate date);

    Optional<Temperature> createTemperature(final Temperature temperature);

    void deleteTemperatureById(final UUID id);

    void deleteTemperatureByTimestamp(final LocalDateTime timestamp);

    void deleteTemperaturesByDate(final LocalDate date);
}
