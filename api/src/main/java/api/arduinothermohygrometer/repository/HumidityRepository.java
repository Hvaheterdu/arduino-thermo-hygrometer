package api.arduinothermohygrometer.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.arduinothermohygrometer.model.Humidity;

public interface HumidityRepository {
    Optional<Humidity> getHumidityById(UUID id);

    List<Humidity> getHumidityByTimestamp(LocalDateTime timestamp);

    List<Humidity> getHumiditiesByDate(LocalDate date);

    Optional<Humidity> createHumidity(Humidity humidity);

    void deleteHumidityById(UUID id);

    void deleteHumidityByTimestamp(LocalDateTime timestamp);

    void deleteHumiditiesByDate(LocalDate date);
}
