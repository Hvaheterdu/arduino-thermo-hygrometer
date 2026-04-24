package api.arduinothermohygrometer.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

import static java.util.Collections.emptyList;

@Slf4j
@Service
public class BatteryServiceImpl implements BatteryService {
    private static final String ID_NOT_FOUND = "Battery with id=%s not found.";
    private static final String DATETIME_NOT_FOUND = "Batteries with dateTime={} not found.";

    private final BatteryRepository batteryRepository;

    public BatteryServiceImpl(BatteryRepository batteryRepository) {
        this.batteryRepository = batteryRepository;
    }

    @Override
    public BatteryDto getBatteryById(final UUID id) throws ResourceNotFoundException {
        log.info("Retrieving battery with id={}.", id);

        Battery battery = batteryRepository.getBatteryById(id)
                                           .orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND, id)));

        log.info("Battery with id={} retrieved.", id);
        return BatteryModelMapper.toDto(battery);
    }

    @Override
    public List<BatteryDto> getBatteriesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) {
        log.info("Retrieving batteries with dateTime={}, checkOnlyDate={}.", dateTime, checkOnlyDate);

        List<Battery> batteries = checkOnlyDate
            ? batteryRepository.getBatteriesByDate(dateTime.toLocalDate())
            : batteryRepository.getBatteryByTimestamp(dateTime);

        if (batteries.isEmpty()) {
            log.info(DATETIME_NOT_FOUND, dateTime);
            return emptyList();
        }

        log.info("Batteries with dateTime={} retrieved.", dateTime);
        return batteries.stream()
                        .map(BatteryModelMapper::toDto)
                        .toList();
    }

    @Override
    public BatteryDto createBattery(final BatteryDto batteryDto) throws ResourceNotCreatedException {
        log.info("Creating battery.");

        if (batteryDto == null) {
            throw new ResourceNotCreatedException("Battery cannot be created.");
        }

        Battery battery = BatteryModelMapper.toModel(batteryDto);
        batteryRepository.createBattery(battery);
        log.info("Battery with id={} and registered_at={} created.", battery.getId(), battery.getRegisteredAt());

        return BatteryModelMapper.toDto(battery);
    }

    @Override
    public void deleteBatteryById(final UUID id) throws ResourceNotFoundException {
        log.info("Deleting battery with id={}.", id);

        Optional<Battery> battery = batteryRepository.getBatteryById(id);
        if (battery.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND, id));
        }

        batteryRepository.deleteBatteryById(id);
        log.info("Battery with id={} deleted.", id);
    }

    @Override
    public void deleteBatteriesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) {
        log.info("Deleting batteries with dateTime={}, checkOnlyDate={}.", dateTime, checkOnlyDate);

        List<Battery> batteries = checkOnlyDate
            ? batteryRepository.getBatteriesByDate(dateTime.toLocalDate())
            : batteryRepository.getBatteryByTimestamp(dateTime);

        if (batteries.isEmpty()) {
            log.info(DATETIME_NOT_FOUND, dateTime);
            return;
        }

        Battery firstBattery = batteries.getFirst();
        if (checkOnlyDate) {
            batteryRepository.deleteBatteriesByDate(firstBattery.getRegisteredAt().toLocalDate());
            log.info("Deleted batteries with date={}.", firstBattery.getRegisteredAt().toLocalDate());
        } else {
            batteryRepository.deleteBatteryByTimestamp(firstBattery.getRegisteredAt());
            log.info("Deleted battery with timestamp={}.", firstBattery.getRegisteredAt());
        }
    }
}
