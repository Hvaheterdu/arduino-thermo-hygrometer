package api.arduinothermohygrometer.services.implementations;

import java.time.LocalDate;
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
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.mappers.BatteryEntityMapper;
import api.arduinothermohygrometer.repositories.BatteryRepository;
import api.arduinothermohygrometer.services.BatteryService;

@Service
public class BatteryServiceImpl implements BatteryService {
    private static final String ID_NOT_FOUND_EXCEPTION = "Battery with id=%s not found.";
    private static final String EMPTY_ID_NOT_FOUND = "Battery with empty id=%s does not exist.";
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    private static final Logger LOGGER = LoggerFactory.getLogger(BatteryServiceImpl.class);

    private final BatteryRepository batteryRepository;

    public BatteryServiceImpl(BatteryRepository batteryRepository) {
        LOGGER.info("Initialising BatteryService.");
        this.batteryRepository = batteryRepository;
    }

    @Override
    public BatteryDto getBatteryDtoById(UUID id) throws ResourceNotFoundException {
        LOGGER.info("Retrieving battery with id={}.", id);

        if (id == EMPTY_UUID) {
            LOGGER.warn("Getting battery with empty id={} failed.", id);
            throw new ResourceNotFoundException(String.format(EMPTY_ID_NOT_FOUND, id));
        }

        Optional<Battery> battery = batteryRepository.getBatteryById(id);
        if (battery.isEmpty()) {
            LOGGER.warn("Battery with id={} not found.", id);
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND_EXCEPTION, id));
        }

        BatteryDto batteryDto = BatteryEntityMapper.toDto(battery.get());
        LOGGER.info("Battery with id={} retrieved.", id);

        return batteryDto;
    }

    @Override
    public BatteryDto getBatteryDtoByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException {
        LOGGER.info("Retrieving battery with timestamp={}.", timestamp);

        Optional<Battery> battery = batteryRepository.getBatteryByTimestamp(timestamp);
        if (battery.isEmpty()) {
            LOGGER.warn("Battery with timestamp={} not found.", timestamp);
            throw new ResourceNotFoundException(String.format("Battery with timestamp=%s not found", timestamp));
        }

        BatteryDto batteryDto = BatteryEntityMapper.toDto(battery.get());
        LOGGER.info("Battery with timestamp={} retrieved.", timestamp);

        return batteryDto;
    }

    @Override
    public List<BatteryDto> getBatteryDtosByDate(LocalDate date) {
        LOGGER.info("Retrieving batteries with date={}.", date);

        List<Battery> batteries = Optional.ofNullable(batteryRepository.getBatteriesByDate(date))
                                          .orElse(Collections.emptyList());
        if (batteries.isEmpty()) {
            LOGGER.warn("Batteries with date={} not found.", date);
            throw new ResourceNotFoundException(String.format("Batteries with date=%s not found.", date));
        }

        List<BatteryDto> batteryDtos = batteries.stream()
                                                .map(BatteryEntityMapper::toDto)
                                                .toList();
        LOGGER.info("Batteries with date={} retrieved.", date);

        return batteryDtos;
    }

    @Override
    public BatteryDto createBatteryDto(BatteryDto batteryDto) throws ResourceNotCreatedException {
        LOGGER.info("Creating battery.");

        if (batteryDto == null) {
            LOGGER.warn("Battery cannot be created.");
            throw new ResourceNotCreatedException("Battery cannot be created.");
        }

        Battery battery = BatteryEntityMapper.toModel(batteryDto);
        batteryRepository.createBattery(battery);
        LOGGER.info("Battery with id={} and registered_at={} created.", battery.getId(), battery.getRegisteredAt());

        return batteryDto;
    }

    @Override
    public void deleteBatteryById(UUID id) throws ResourceNotFoundException {
        LOGGER.info("Deleting battery with id={}.", id);

        if (id == EMPTY_UUID) {
            LOGGER.warn("Deleting battery with empty id={} failed.", id);
            throw new ResourceNotFoundException(String.format(EMPTY_ID_NOT_FOUND, id));
        }

        Optional<Battery> battery = batteryRepository.getBatteryById(id);
        if (battery.isEmpty()) {
            LOGGER.warn("Battery with id={} not deleted.", id);
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND_EXCEPTION, id));
        }

        batteryRepository.deleteBatteryById(id);
        LOGGER.info("Battery with id={} deleted.", id);
    }

    @Override
    public void deleteBatteryByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException {
        LOGGER.info("Deleting battery with timestamp={}.", timestamp);

        Optional<Battery> battery = batteryRepository.getBatteryByTimestamp(timestamp);
        if (battery.isEmpty()) {
            LOGGER.warn("Battery with timestamp={} not deleted.", timestamp);
            throw new ResourceNotFoundException(String.format("Battery with timestamp=%s not found.", timestamp));
        }

        batteryRepository.deleteBatteryByTimestamp(timestamp);
        LOGGER.info("Battery with timestamp={} deleted.", timestamp);
    }
}
