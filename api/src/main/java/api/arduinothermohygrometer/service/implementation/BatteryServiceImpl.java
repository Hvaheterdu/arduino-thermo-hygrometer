package api.arduinothermohygrometer.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.BatteryEntityMapper;
import api.arduinothermohygrometer.model.Battery;
import api.arduinothermohygrometer.repository.BatteryRepository;
import api.arduinothermohygrometer.service.BatteryService;

@Service
public class BatteryServiceImpl implements BatteryService {
    private static final String ID_NOT_FOUND = "Battery with id=%s not found.";
    private static final String EMPTY_ID_NOT_FOUND = "Battery with empty id=%s does not exist.";
    private static final String DATETIME_NOT_FOUND = "Batteries with dateTime=%s not found.";
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    private static final Logger LOGGER = LoggerFactory.getLogger(BatteryServiceImpl.class);

    private final BatteryRepository batteryRepository;

    public BatteryServiceImpl(BatteryRepository batteryRepository) {
        this.batteryRepository = batteryRepository;
    }

    @Override
    public BatteryDto getBatteryById(final UUID id) throws ResourceNotFoundException {
        LOGGER.info("Retrieving battery with id={}.", id);

        if (EMPTY_UUID.equals(id)) {
            LOGGER.warn("Getting battery with empty id={} failed.", id);
            throw new ResourceNotFoundException(String.format(EMPTY_ID_NOT_FOUND, id));
        }

        Optional<Battery> battery = batteryRepository.getBatteryById(id);
        if (battery.isEmpty()) {
            LOGGER.warn("Battery with id={} not found.", id);
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND, id));
        }

        BatteryDto batteryDto = BatteryEntityMapper.toDto(battery.get());
        LOGGER.info("Battery with id={} retrieved.", id);

        return batteryDto;
    }

    @Override
    public List<BatteryDto> getBatteriesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) throws ResourceNotFoundException {
        LOGGER.info("Retrieving batteries with dateTime={}.", dateTime);

        List<Battery> batteries;
        if (checkOnlyDate) {
            LOGGER.info("Batteries to retrieve with date={}.", dateTime.toLocalDate());
            batteries = batteryRepository.getBatteriesByDate(dateTime.toLocalDate());
        } else {
            LOGGER.info("Battery to retrieve with timestamp={}.", dateTime);
            batteries = batteryRepository.getBatteryByTimestamp(dateTime);
        }

        if (batteries.isEmpty()) {
            LOGGER.warn("Batteries with dateTime={} not found.", dateTime);
            throw new ResourceNotFoundException(String.format(DATETIME_NOT_FOUND, dateTime));
        }

        List<BatteryDto> batteryDtos = batteries.stream()
                                                .map(BatteryEntityMapper::toDto)
                                                .toList();
        LOGGER.info("Batteries with dateTime={} retrieved.", dateTime);

        return batteryDtos;
    }

    @Override
    public BatteryDto createBattery(final BatteryDto batteryDto) throws ResourceNotCreatedException {
        LOGGER.info("Creating battery.");

        if (batteryDto == null) {
            LOGGER.warn("Battery cannot be created.");
            throw new ResourceNotCreatedException("Battery cannot be created.");
        }

        Battery battery = BatteryEntityMapper.toEntity(batteryDto);
        batteryRepository.createBattery(battery);
        LOGGER.info("Battery with id={} and registered_at={} created.", battery.getId(), battery.getRegisteredAt());

        return BatteryEntityMapper.toDto(battery);
    }

    @Override
    public void deleteBatteryById(final UUID id) throws ResourceNotFoundException {
        LOGGER.info("Deleting battery with id={}.", id);

        if (EMPTY_UUID.equals(id)) {
            LOGGER.warn("Deleting battery with empty id={} failed.", id);
            throw new ResourceNotFoundException(String.format(EMPTY_ID_NOT_FOUND, id));
        }

        Optional<Battery> battery = batteryRepository.getBatteryById(id);
        if (battery.isEmpty()) {
            LOGGER.warn("Battery with id={} not deleted.", id);
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND, id));
        }

        batteryRepository.deleteBatteryById(id);
        LOGGER.info("Battery with id={} deleted.", id);
    }

    @Override
    public void deleteBatteriesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) throws ResourceNotFoundException {
        LOGGER.info("Deleting batteries with dateTime={}.", dateTime);

        List<Battery> batteries;
        if (checkOnlyDate) {
            LOGGER.info("Batteries to delete with date={}.", dateTime.toLocalDate());
            batteries = batteryRepository.getBatteriesByDate(dateTime.toLocalDate());
        } else {
            LOGGER.info("Battery to delete with timestamp={}.", dateTime);
            batteries = batteryRepository.getBatteryByTimestamp(dateTime);
        }

        if (batteries.isEmpty()) {
            LOGGER.warn("Batteries with dateTime={} not found.", dateTime);
            throw new ResourceNotFoundException(String.format(DATETIME_NOT_FOUND, dateTime));
        }

        if (checkOnlyDate) {
            LOGGER.info("Batteries with date={} deleted.", dateTime.toLocalDate());
            batteryRepository.deleteBatteriesByDate(dateTime.toLocalDate());
        } else {
            LOGGER.info("Battery with timestamp={} deleted.", dateTime);
            batteryRepository.deleteBatteryByTimestamp(dateTime);
        }
    }
}
