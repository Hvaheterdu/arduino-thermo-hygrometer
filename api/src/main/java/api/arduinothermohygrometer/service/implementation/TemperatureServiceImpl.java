package api.arduinothermohygrometer.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.TemperatureEntityMapper;
import api.arduinothermohygrometer.model.Temperature;
import api.arduinothermohygrometer.repository.TemperatureRepository;
import api.arduinothermohygrometer.service.TemperatureService;

import static java.util.Collections.emptyList;

@Service
public class TemperatureServiceImpl implements TemperatureService {
    private static final String ID_NOT_FOUND = "Temperature with id=%s not found.";
    private static final String EMPTY_ID_NOT_FOUND = "Temperature with empty id=%s does not exist.";
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    private static final Logger LOGGER = LoggerFactory.getLogger(TemperatureServiceImpl.class);

    private final TemperatureRepository temperatureRepository;

    public TemperatureServiceImpl(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    @Override
    public TemperatureDto getTemperatureById(final UUID id) throws ResourceNotFoundException {
        LOGGER.info("Retrieving Temperature with id={}.", id);

        if (EMPTY_UUID.equals(id)) {
            LOGGER.warn("Getting temperature with empty id={} failed.", id);
            throw new ResourceNotFoundException(String.format(EMPTY_ID_NOT_FOUND, id));
        }

        Optional<Temperature> temperature = temperatureRepository.getTemperatureById(id);
        if (temperature.isEmpty()) {
            LOGGER.warn("Temperature with id={} not found.", id);
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND, id));
        }

        TemperatureDto temperatureDto = TemperatureEntityMapper.toDto(temperature.get());
        LOGGER.info("Temperature with id={} retrieved.", id);

        return temperatureDto;
    }

    @Override
    public List<TemperatureDto> getTemperaturesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) {
        LOGGER.info("Retrieving temperatures with dateTime={}, checkOnlyDate={}.", dateTime, checkOnlyDate);

        List<Temperature> temperatures = checkOnlyDate
            ? temperatureRepository.getTemperaturesByDate(dateTime.toLocalDate())
            : temperatureRepository.getTemperatureByTimestamp(dateTime);

        if (temperatures.isEmpty()) {
            LOGGER.info("No temperatures with dateTime={} found.", dateTime);
            return emptyList();
        }

        LOGGER.info("Temperatures with dateTime={} retrieved.", dateTime);
        return temperatures.stream()
                           .map(TemperatureEntityMapper::toDto)
                           .toList();
    }

    @Override
    public TemperatureDto createTemperature(final TemperatureDto temperatureDto) throws ResourceNotCreatedException {
        LOGGER.info("Creating Temperature.");

        if (temperatureDto == null) {
            LOGGER.warn("Temperature cannot be created.");
            throw new ResourceNotCreatedException("Temperature cannot be created.");
        }

        Temperature temperature = TemperatureEntityMapper.toEntity(temperatureDto);
        temperatureRepository.createTemperature(temperature);
        LOGGER.info("Temperature with id={} and registered_at={} created.", temperature.getId(), temperature.getRegisteredAt());

        return TemperatureEntityMapper.toDto(temperature);
    }

    @Override
    public void deleteTemperatureById(final UUID id) throws ResourceNotFoundException {
        LOGGER.info("Deleting Temperature with id={}.", id);

        if (EMPTY_UUID.equals(id)) {
            LOGGER.warn("Deleting temperature with empty id={} failed.", id);
            throw new ResourceNotFoundException(String.format(EMPTY_ID_NOT_FOUND, id));
        }

        Optional<Temperature> temperature = temperatureRepository.getTemperatureById(id);
        if (temperature.isEmpty()) {
            LOGGER.warn("Temperature with id={} not deleted.", id);
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND, id));
        }

        temperatureRepository.deleteTemperatureById(id);
        LOGGER.info("Temperature with id={} deleted.", id);
    }

    @Override
    public void deleteTemperaturesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) throws ResourceNotFoundException {
        LOGGER.info("Deleting temperatures with dateTime={}, checkOnlyDate={}.", dateTime, checkOnlyDate);

        List<Temperature> temperatures = checkOnlyDate
            ? temperatureRepository.getTemperaturesByDate(dateTime.toLocalDate())
            : temperatureRepository.getTemperatureByTimestamp(dateTime);

        if (temperatures.isEmpty()) {
            LOGGER.info("No temperatures with dateTime={} found for deletion.", dateTime);
            return;
        }

        Temperature firstTemperature = temperatures.getFirst();
        if (checkOnlyDate) {
            temperatureRepository.deleteTemperaturesByDate(firstTemperature.getRegisteredAt().toLocalDate());
            LOGGER.info("Temperatures with date={} deleted.", firstTemperature.getRegisteredAt().toLocalDate());
        } else {
            temperatureRepository.deleteTemperatureByTimestamp(firstTemperature.getRegisteredAt());
            LOGGER.info("Temperature with timestamp={} deleted.", firstTemperature.getRegisteredAt());
        }
    }
}
