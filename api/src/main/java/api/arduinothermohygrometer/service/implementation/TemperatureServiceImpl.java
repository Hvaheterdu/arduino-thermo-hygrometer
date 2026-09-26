package api.arduinothermohygrometer.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.model.Temperature;
import api.arduinothermohygrometer.repository.TemperatureRepository;
import api.arduinothermohygrometer.service.TemperatureService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TemperatureServiceImpl implements TemperatureService {
  private static final String REGISTERED_AT_NOT_FOUND = "Temperatures registeredAt={} not found.";

  private final TemperatureRepository temperatureRepository;

  public TemperatureServiceImpl(final TemperatureRepository temperatureRepository) {
    this.temperatureRepository = temperatureRepository;
  }

  @Override
  public Temperature getTemperatureById(final UUID id) throws ResourceNotFoundException {
    log.info("Retrieving temperature with id={}.", id);

    Temperature temperature =
        temperatureRepository
            .getTemperatureById(id)
            .orElseThrow(
                () -> {
                  log.error("Temperature with id={} not found.", id);
                  return new ResourceNotFoundException("Temperature not found.");
                });

    log.info("Temperature with id={} retrieved.", id);
    return temperature;
  }

  @Override
  public List<Temperature> getTemperaturesByDateOrTimestamp(
      final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException {
    log.info("Retrieving temperatures registeredAt={}, dateOnly={}.", registeredAt, dateOnly);

    List<Temperature> temperatures =
        dateOnly
            ? temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())
            : temperatureRepository.getTemperatureByTimestamp(registeredAt);

    if (temperatures.isEmpty()) {
      log.info(REGISTERED_AT_NOT_FOUND, registeredAt);
      throw new ResourceNotFoundException(
          "Temperatures not found for "
              + (dateOnly
                  ? "date " + registeredAt.toLocalDate() + "."
                  : "timestamp " + registeredAt + "."));
    }

    log.info("Temperatures registeredAt={} retrieved.", registeredAt);
    return temperatures;
  }

  @Override
  public Temperature createTemperature(final Temperature temperature)
      throws ResourceNotCreatedException {
    log.info("Creating temperature.");

    Temperature createdTemperature =
        temperatureRepository
            .createTemperature(temperature)
            .orElseThrow(
                () -> {
                  log.error("Temperature cannot be created.");
                  return new ResourceNotCreatedException("Temperature cannot be created.");
                });

    log.info(
        "Temperature with id={} and registered_at={} created.",
        createdTemperature.getId(),
        createdTemperature.getRegisteredAt());
    return createdTemperature;
  }

  @Override
  public void deleteTemperaturesByDateOrTimestamp(
      final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException {
    log.info("Deleting temperatures registeredAt={}, dateOnly={}.", registeredAt, dateOnly);

    List<Temperature> temperatures =
        dateOnly
            ? temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())
            : temperatureRepository.getTemperatureByTimestamp(registeredAt);

    if (temperatures.isEmpty()) {
      log.info(REGISTERED_AT_NOT_FOUND, registeredAt);
      throw new ResourceNotFoundException(
          "Temperatures not found for "
              + (dateOnly
                  ? "date " + registeredAt.toLocalDate() + "."
                  : "timestamp " + registeredAt + "."));
    }

    Temperature firstTemperature = temperatures.getFirst();
    if (dateOnly) {
      temperatureRepository.deleteTemperaturesByDate(
          firstTemperature.getRegisteredAt().toLocalDate());
      log.info(
          "Deleted temperatures with date={}.", firstTemperature.getRegisteredAt().toLocalDate());
    } else {
      temperatureRepository.deleteTemperatureByTimestamp(firstTemperature.getRegisteredAt());
      log.info("Deleted temperature with timestamp={}.", firstTemperature.getRegisteredAt());
    }
  }
}
