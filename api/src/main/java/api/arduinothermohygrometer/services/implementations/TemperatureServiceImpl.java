package api.arduinothermohygrometer.services.implementations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import api.arduinothermohygrometer.dtos.TemperatureDto;
import api.arduinothermohygrometer.entities.Temperature;
import api.arduinothermohygrometer.exceptions.ResourceMappingFailedException;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.mappers.TemperatureEntityMapper;
import api.arduinothermohygrometer.repositories.TemperatureRepository;
import api.arduinothermohygrometer.services.TemperatureService;

@Service
public class TemperatureServiceImpl implements TemperatureService {
    private static final String TEMPERATURE_ID_NOT_FOUND = "Temperature with id={} not found.";
    private static final String TEMPERATURE_ID_NOT_FOUND_EXCEPTION = "Temperature with id=%uuid not found.";
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    private static final Logger LOGGER = LoggerFactory.getLogger(TemperatureServiceImpl.class);

    private final TemperatureRepository temperatureRepository;

    public TemperatureServiceImpl(TemperatureRepository temperatureRepository) {
        LOGGER.info("Initialising TemperatureService.");
        this.temperatureRepository = temperatureRepository;
    }

    @Override
    public TemperatureDto getTemperatureDtoById(UUID id) throws ResourceNotFoundException {
        LOGGER.info("Retrieving Temperature with id={}.", id);

        if (id == EMPTY_UUID) {
            LOGGER.info(TEMPERATURE_ID_NOT_FOUND, id);
            throw new ResourceNotFoundException(String.format(TEMPERATURE_ID_NOT_FOUND_EXCEPTION, id));
        }

        Optional<Temperature> temperature = temperatureRepository.getTemperatureById(id);
        if (temperature.isEmpty()) {
            LOGGER.info(TEMPERATURE_ID_NOT_FOUND, id);
            throw new ResourceNotFoundException(String.format(TEMPERATURE_ID_NOT_FOUND_EXCEPTION, id));
        }

        TemperatureDto temperatureDto = TemperatureEntityMapper.toDto(temperature.get());
        LOGGER.info("Temperature with id={} retrieved.", temperatureDto);

        return temperatureDto;
    }

    @Override
    public TemperatureDto getTemperatureDtoByTimestamp(LocalDateTime timestamp)
            throws ResourceNotFoundException {
        LOGGER.info("Retrieving Temperature with timestamp={}.", timestamp);

        Optional<Temperature> temperature = temperatureRepository.getTemperatureByTimestamp(timestamp);
        if (temperature.isEmpty()) {
            LOGGER.info("Temperature with timestamp={} not found.", timestamp);
            throw new ResourceNotFoundException(String.format("Temperature with timestamp=%s not found.", timestamp));
        }

        TemperatureDto temperatureDto = TemperatureEntityMapper.toDto(temperature.get());
        LOGGER.info("Temperature with timestamp={} retrieved.", timestamp);

        return temperatureDto;
    }

    @Override
    public List<TemperatureDto> getTemperatureDtosByDate(LocalDateTime date) {
        LOGGER.info("Retrieving temperatures with date={}.", date.toLocalDate());

        List<Temperature> temperatures = Optional.ofNullable(temperatureRepository.getTemperaturesByDate(date))
                .orElse(Collections.emptyList());
        if (temperatures.isEmpty()) {
            LOGGER.info("Temperatures with date={} not found.", date.toLocalDate());
        }

        List<TemperatureDto> temperatureDtos = temperatures.stream()
                .map(TemperatureEntityMapper::toDto)
                .toList();
        LOGGER.info("Temperatures with date={} retrieved.", date);

        return temperatureDtos;
    }

    @Override
    public TemperatureDto createTemperatureDto(TemperatureDto temperatureDto)
            throws ResourceNotCreatedException, ResourceMappingFailedException {
        LOGGER.info("Creating Temperature.");

        if (temperatureDto == null) {
            LOGGER.info("Temperature can't be created.");
            throw new ResourceNotCreatedException("Temperature can't be created.");
        }

        Temperature temperature = TemperatureEntityMapper.toModel(temperatureDto);
        if (temperature == null) {
            LOGGER.info("Temperature mapping failed during creation.");
            throw new ResourceMappingFailedException("Temperature mapping failed during creation.");
        }

        temperatureRepository.createTemperature(temperature);
        LOGGER.info("Temperature with id={} and registered_at={} created.",
                temperature.getId(),
                temperature.getRegisteredAt());

        return temperatureDto;
    }

    @Override
    public void deleteTemperatureById(UUID id) throws ResourceNotFoundException {
        LOGGER.info("Deleting Temperature with id={}.", id);

        if (id == EMPTY_UUID) {
            LOGGER.info(TEMPERATURE_ID_NOT_FOUND, id);
            throw new ResourceNotFoundException(String.format(TEMPERATURE_ID_NOT_FOUND_EXCEPTION, id));
        }

        Optional<Temperature> temperature = temperatureRepository.getTemperatureById(id);
        if (temperature.isEmpty()) {
            LOGGER.info(TEMPERATURE_ID_NOT_FOUND, id);
            throw new ResourceNotFoundException(String.format(TEMPERATURE_ID_NOT_FOUND_EXCEPTION, id));
        }

        temperatureRepository.deleteTemperatureById(id);
        LOGGER.info("Temperature with id={} deleted.", id);
    }

    @Override
    public void deleteTemperatureByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException {
        LOGGER.info("Deleting Temperature with timestamp={}.", timestamp);

        Optional<Temperature> temperature = temperatureRepository.getTemperatureByTimestamp(timestamp);
        if (temperature.isEmpty()) {
            LOGGER.info("Temperature with timestamp={} not found.", timestamp);
            throw new ResourceNotFoundException(String.format("Temperature with timestamp=%s not found.", timestamp));
        }

        temperatureRepository.deleteTemperatureByTimestamp(timestamp);
        LOGGER.info("Temperature with timestamp={} deleted.", timestamp);
    }
}
