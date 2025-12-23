package api.arduinothermohygrometer.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.arduinothermohygrometer.entities.Humidity;

public interface HumidityRepository {
    Optional<Humidity> getHumidityById(UUID id);

    Optional<Humidity> getHumidityByTimestamp(LocalDateTime timestamp);

    List<Humidity> getHumiditiesByDate(LocalDate date);

    void createHumidity(Humidity humidity);

    void deleteHumidityById(UUID id);

    void deleteHumidityByTimestamp(LocalDateTime timestamp);
}
