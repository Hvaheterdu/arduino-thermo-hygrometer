package api.arduinothermohygrometer.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.HumidityModelMapper;
import api.arduinothermohygrometer.model.Humidity;
import api.arduinothermohygrometer.repository.HumidityRepository;
import api.arduinothermohygrometer.service.HumidityService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HumidityServiceImpl implements HumidityService {
  private static final String REGISTERED_AT_NOT_FOUND = "Humidities registeredAt={} not found.";

  private final HumidityRepository humidityRepository;

  public HumidityServiceImpl(final HumidityRepository humidityRepository) {
    this.humidityRepository = humidityRepository;
  }

  @Override
  public HumidityDto getHumidityById(final UUID id) throws ResourceNotFoundException {
    log.info("Retrieving humidity with id={}.", id);

    Humidity humidity =
        humidityRepository
            .getHumidityById(id)
            .orElseThrow(
                () -> {
                  log.error("Humidity with id={} not found.", id);
                  return new ResourceNotFoundException("Humidity not found.");
                });

    log.info("Humidity with id={} retrieved.", id);
    return HumidityModelMapper.toDto(humidity);
  }

  @Override
  public List<HumidityDto> getHumiditiesByDateOrTimestamp(
      final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException {
    log.info("Retrieving humidities registeredAt={}, dateOnly={}.", registeredAt, dateOnly);

    List<Humidity> humidities =
        dateOnly
            ? humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate())
            : humidityRepository.getHumidityByTimestamp(registeredAt);

    if (humidities.isEmpty()) {
      log.info(REGISTERED_AT_NOT_FOUND, registeredAt);
      throw new ResourceNotFoundException(
          "Humidities not found for "
              + (dateOnly
                  ? "date " + registeredAt.toLocalDate() + "."
                  : "timestamp " + registeredAt + "."));
    }

    log.info("Humidities registeredAt={} retrieved.", registeredAt);
    return humidities.stream().map(HumidityModelMapper::toDto).toList();
  }

  @Override
  public HumidityDto createHumidity(final HumidityDto humidityDto)
      throws ResourceNotCreatedException {
    log.info("Creating humidity.");

    Humidity humidity =
        humidityRepository
            .createHumidity(HumidityModelMapper.toModel(humidityDto))
            .orElseThrow(
                () -> {
                  log.error("Humidity cannot be created.");
                  return new ResourceNotCreatedException("Humidity cannot be created.");
                });

    log.info(
        "Humidity with id={} and registered_at={} created.",
        humidity.getId(),
        humidity.getRegisteredAt());
    return HumidityModelMapper.toDto(humidity);
  }

  @Override
  public void deleteHumiditiesByDateOrTimestamp(
      final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException {
    log.info("Deleting humidities registeredAt={}, dateOnly={}.", registeredAt, dateOnly);

    List<Humidity> humidities =
        dateOnly
            ? humidityRepository.getHumiditiesByDate(registeredAt.toLocalDate())
            : humidityRepository.getHumidityByTimestamp(registeredAt);

    if (humidities.isEmpty()) {
      log.info(REGISTERED_AT_NOT_FOUND, registeredAt);
      throw new ResourceNotFoundException(
          "Humidities not found for "
              + (dateOnly
                  ? "date " + registeredAt.toLocalDate() + "."
                  : "timestamp " + registeredAt + "."));
    }

    Humidity firstHumidity = humidities.getFirst();
    if (dateOnly) {
      humidityRepository.deleteHumiditiesByDate(firstHumidity.getRegisteredAt().toLocalDate());
      log.info("Deleted humidities with date={}.", firstHumidity.getRegisteredAt().toLocalDate());
    } else {
      humidityRepository.deleteHumidityByTimestamp(firstHumidity.getRegisteredAt());
      log.info("Deleted humidity with timestamp={}.", firstHumidity.getRegisteredAt());
    }
  }
}
