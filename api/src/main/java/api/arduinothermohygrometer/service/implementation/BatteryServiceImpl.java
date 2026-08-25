package api.arduinothermohygrometer.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.BatteryModelMapper;
import api.arduinothermohygrometer.model.Battery;
import api.arduinothermohygrometer.repository.BatteryRepository;
import api.arduinothermohygrometer.service.BatteryService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BatteryServiceImpl implements BatteryService {
  private static final String REGISTERED_AT_NOT_FOUND = "Batteries registeredAt={} not found.";

  private final BatteryRepository batteryRepository;

  public BatteryServiceImpl(final BatteryRepository batteryRepository) {
    this.batteryRepository = batteryRepository;
  }

  @Override
  public BatteryDto getBatteryById(final UUID id) throws ResourceNotFoundException {
    log.info("Retrieving battery with id={}.", id);

    Battery battery =
        batteryRepository
            .getBatteryById(id)
            .orElseThrow(
                () -> {
                  log.error("Battery with id={} not found.", id);
                  return new ResourceNotFoundException("Battery not found.");
                });

    log.info("Battery with id={} retrieved.", id);
    return BatteryModelMapper.toDto(battery);
  }

  @Override
  public List<BatteryDto> getBatteriesByDateOrTimestamp(
      final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException {
    log.info("Retrieving batteries registeredAt={}, dateOnly={}.", registeredAt, dateOnly);

    List<Battery> batteries =
        dateOnly
            ? batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())
            : batteryRepository.getBatteryByTimestamp(registeredAt);

    if (batteries.isEmpty()) {
      log.info(REGISTERED_AT_NOT_FOUND, registeredAt);
      throw new ResourceNotFoundException(
          "Batteries not found for "
              + (dateOnly
                  ? "date " + registeredAt.toLocalDate() + "."
                  : "timestamp " + registeredAt + "."));
    }

    log.info("Batteries registeredAt={} retrieved.", registeredAt);
    return batteries.stream().map(BatteryModelMapper::toDto).toList();
  }

  @Override
  public BatteryDto createBattery(final BatteryDto batteryDto) throws ResourceNotCreatedException {
    log.info("Creating battery.");

    Battery battery =
        batteryRepository
            .createBattery(BatteryModelMapper.toModel(batteryDto))
            .orElseThrow(
                () -> {
                  log.error("Battery cannot be created.");
                  return new ResourceNotCreatedException("Battery cannot be created.");
                });

    log.info(
        "Battery with id={} and registered_at={} created.",
        battery.getId(),
        battery.getRegisteredAt());
    return BatteryModelMapper.toDto(battery);
  }

  @Override
  public void deleteBatteriesByDateOrTimestamp(
      final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException {
    log.info("Deleting batteries registeredAt={}, dateOnly={}.", registeredAt, dateOnly);

    List<Battery> batteries =
        dateOnly
            ? batteryRepository.getBatteriesByDate(registeredAt.toLocalDate())
            : batteryRepository.getBatteryByTimestamp(registeredAt);

    if (batteries.isEmpty()) {
      log.info(REGISTERED_AT_NOT_FOUND, registeredAt);
      throw new ResourceNotFoundException(
          "Batteries not found for "
              + (dateOnly
                  ? "date " + registeredAt.toLocalDate() + "."
                  : "timestamp " + registeredAt + "."));
    }

    Battery firstBattery = batteries.getFirst();
    if (dateOnly) {
      batteryRepository.deleteBatteriesByDate(firstBattery.getRegisteredAt().toLocalDate());
      log.info("Deleted batteries with date={}.", firstBattery.getRegisteredAt().toLocalDate());
    } else {
      batteryRepository.deleteBatteryByTimestamp(firstBattery.getRegisteredAt());
      log.info("Deleted battery with timestamp={}.", firstBattery.getRegisteredAt());
    }
  }
}
