package api.arduinothermohygrometer.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import api.arduinothermohygrometer.model.Humidity;

public interface HumidityRepository {
    Optional<Humidity> getHumidityById(final UUID id);

    List<Humidity> getHumidityByTimestamp(final LocalDateTime timestamp);

    List<Humidity> getHumiditiesByDate(final LocalDate date);

    Optional<Humidity> createHumidity(final Humidity humidity);

    void deleteHumidityById(final UUID id);

    void deleteHumidityByTimestamp(final LocalDateTime timestamp);

    void deleteHumiditiesByDate(final LocalDate date);
}
