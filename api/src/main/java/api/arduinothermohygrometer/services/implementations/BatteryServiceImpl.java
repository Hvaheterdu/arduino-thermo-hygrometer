package api.arduinothermohygrometer.services.implementations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.entities.Battery;
import api.arduinothermohygrometer.mappers.BatteryEntityMapper;
import api.arduinothermohygrometer.repositories.BatteryRepository;
import api.arduinothermohygrometer.services.BatteryService;

@Service
public class BatteryServiceImpl implements BatteryService {
    private static final String BATTERY_ID_NOT_FOUND = "Battery with id={} not found.";
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    private static final Logger LOGGER = LoggerFactory.getLogger(BatteryServiceImpl.class);

    private final BatteryRepository batteryRepository;

    public BatteryServiceImpl(BatteryRepository batteryRepository) {
        LOGGER.info("Initialising BatteryService");
        this.batteryRepository = batteryRepository;
    }

    @Override
    public Optional<BatteryDto> getBatteryDtoById(UUID id) {
        LOGGER.info("Retrieving battery with id={}", id);

        if (id == EMPTY_UUID) {
            LOGGER.info(BATTERY_ID_NOT_FOUND, id);
            return Optional.empty();
        }

        Optional<Battery> battery = batteryRepository.getBatteryById(id);
        if (battery.isEmpty()) {
            LOGGER.info(BATTERY_ID_NOT_FOUND, id);
            return Optional.empty();
        }

        Optional<BatteryDto> batteryDto = BatteryEntityMapper.toDto(battery.get());
        LOGGER.info("Battery with id={} retrieved.", batteryDto);

        return batteryDto;
    }

    @Override
    public Optional<BatteryDto> getBatteryDtoByTimestamp(LocalDateTime timestamp) {
        LOGGER.info("Retrieving battery with timestamp={}", timestamp);

        Optional<Battery> battery = batteryRepository.getBatteryByTimestamp(timestamp);
        if (battery.isEmpty()) {
            LOGGER.info("Battery with timestamp={} not found.", timestamp);
            return Optional.empty();
        }

        Optional<BatteryDto> batteryDto = BatteryEntityMapper.toDto(battery.get());
        LOGGER.info("Battery with timestamp={} retrieved.", timestamp);

        return batteryDto;
    }

    @Override
    public List<BatteryDto> getBatteryDtosByDate(LocalDateTime localDateTime) {
        LOGGER.info("Retrieving batteries with date={}", localDateTime.toLocalDate());

        List<Battery> batteries =
                Optional.ofNullable(batteryRepository.getBatteriesByDate(localDateTime))
                        .orElse(Collections.emptyList());
        if (batteries.isEmpty()) {
            LOGGER.info("Batteries with date={} not found.", localDateTime.toLocalDate());
        }

        List<BatteryDto> batteryDtos = batteries.stream()
                .map(battery -> BatteryEntityMapper.toDto(battery).get())
                .toList();
        LOGGER.info("Batteries with date={} retrieved.", localDateTime);

        return batteryDtos;
    }

    @Override
    public void createBatteryDto(BatteryDto batteryDto) {
        LOGGER.info("Creating battery.");

        if (batteryDto == null) {
            LOGGER.info("Battery can't be created.");
            return;
        }

        Optional<Battery> battery = BatteryEntityMapper.toModel(batteryDto);
        if (battery.isEmpty()) {
            LOGGER.info("Battery mapping failed during creation.");
            return;
        }

        batteryRepository.createBattery(battery.get());
        LOGGER.info("Battery with id={} and registered_at={} created.",
                battery.get().getId(),
                battery.get().getRegisteredAt());
    }

    @Override
    public void deleteBatteryById(UUID id) {
        LOGGER.info("Deleting battery with id={}", id);

        if (id == EMPTY_UUID) {
            LOGGER.info(BATTERY_ID_NOT_FOUND, id);
            return;
        }

        Optional<Battery> battery = batteryRepository.getBatteryById(id);
        if (battery.isEmpty()) {
            LOGGER.info(BATTERY_ID_NOT_FOUND, id);
            return;
        }

        batteryRepository.deleteBatteryById(id);
        LOGGER.info("Battery with id={} deleted.", id);
    }

    @Override
    public void deleteBatteryByTimestamp(LocalDateTime timestamp) {
        LOGGER.info("Deleting battery with timestamp={}", timestamp);

        Optional<Battery> battery = batteryRepository.getBatteryByTimestamp(timestamp);
        if (battery.isEmpty()) {
            LOGGER.info("Battery with timestamp={} not found.", timestamp);
            return;
        }

        batteryRepository.deleteBatteryByTimestamp(timestamp);
        LOGGER.info("Battery with timestamp={} deleted.", timestamp);
    }
}
