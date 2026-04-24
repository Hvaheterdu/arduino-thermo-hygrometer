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

import static java.util.Collections.emptyList;

@Service
public class BatteryServiceImpl implements BatteryService {
    private static final String ID_NOT_FOUND = "Battery with id=%s not found.";
    private static final String EMPTY_ID_NOT_FOUND = "Battery with empty id=%s does not exist.";
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
    public List<BatteryDto> getBatteriesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) {
        LOGGER.info("Retrieving batteries with dateTime={}, checkOnlyDate={}.", dateTime, checkOnlyDate);

        List<Battery> batteries = checkOnlyDate
            ? batteryRepository.getBatteriesByDate(dateTime.toLocalDate())
            : batteryRepository.getBatteryByTimestamp(dateTime);

        if (batteries.isEmpty()) {
            LOGGER.info("No batteries with dateTime={} found.", dateTime);
            return emptyList();
        }

        LOGGER.info("Batteries with dateTime={} retrieved.", dateTime);
        return batteries.stream()
                        .map(BatteryEntityMapper::toDto)
                        .toList();
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
    public void deleteBatteriesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) {
        LOGGER.info("Deleting batteries with dateTime={}, checkOnlyDate={}.", dateTime, checkOnlyDate);

        List<Battery> batteries = checkOnlyDate
            ? batteryRepository.getBatteriesByDate(dateTime.toLocalDate())
            : batteryRepository.getBatteryByTimestamp(dateTime);

        if (batteries.isEmpty()) {
            LOGGER.info("No batteries with dateTime={} found for deletion.", dateTime);
            return;
        }

        Battery firstBattery = batteries.getFirst();
        if (checkOnlyDate) {
            batteryRepository.deleteBatteriesByDate(firstBattery.getRegisteredAt().toLocalDate());
            LOGGER.info("Deleted batteries with date={}.", firstBattery.getRegisteredAt().toLocalDate());
        } else {
            batteryRepository.deleteBatteryByTimestamp(firstBattery.getRegisteredAt());
            LOGGER.info("Deleted battery with timestamp={}.", firstBattery.getRegisteredAt());
        }
    }
}
