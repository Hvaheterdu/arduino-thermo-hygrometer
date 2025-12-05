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
import api.arduinothermohygrometer.mappers.TemperatureEntityMapper;
import api.arduinothermohygrometer.repositories.TemperatureRepository;
import api.arduinothermohygrometer.services.TemperatureService;

@Service
public class TemperatureServiceImpl implements TemperatureService {
    private static final String TEMPERATURE_ID_NOT_FOUND = "Temperature with id={} not found.";
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    private static final Logger LOGGER = LoggerFactory.getLogger(TemperatureServiceImpl.class);

    private final TemperatureRepository temperatureRepository;

    public TemperatureServiceImpl(TemperatureRepository temperatureRepository) {
        LOGGER.info("Initialising TemperatureService");
        this.temperatureRepository = temperatureRepository;
    }

    @Override
    public Optional<TemperatureDto> getTemperatureDtoById(UUID id) {
        LOGGER.info("Retrieving Temperature with id={}", id);

        if (id == EMPTY_UUID) {
            LOGGER.info(TEMPERATURE_ID_NOT_FOUND, id);
            return Optional.empty();
        }

        Optional<Temperature> temperature = temperatureRepository.getTemperatureById(id);
        if (temperature.isEmpty()) {
            LOGGER.info(TEMPERATURE_ID_NOT_FOUND, id);
            return Optional.empty();
        }

        Optional<TemperatureDto> temperatureDto = TemperatureEntityMapper.toDto(temperature.get());
        LOGGER.info("Temperature with id={} retrieved.", temperatureDto);

        return temperatureDto;
    }

    @Override
    public Optional<TemperatureDto> getTemperatureDtoByTimestamp(LocalDateTime timestamp) {
        LOGGER.info("Retrieving Temperature with timestamp={}", timestamp);

        Optional<Temperature> temperature = temperatureRepository.getTemperatureByTimestamp(timestamp);
        if (temperature.isEmpty()) {
            LOGGER.info("Temperature with timestamp={} not found.", timestamp);
            return Optional.empty();
        }

        Optional<TemperatureDto> temperatureDto = TemperatureEntityMapper.toDto(temperature.get());
        LOGGER.info("Temperature with timestamp={} retrieved.", timestamp);

        return temperatureDto;
    }

    @Override
    public List<TemperatureDto> getTemperatureDtosByDate(LocalDateTime localDateTime) {
        LOGGER.info("Retrieving temperatures with date={}", localDateTime.toLocalDate());

        List<Temperature> temperatures = Optional.ofNullable(temperatureRepository.getTemperaturesByDate(localDateTime))
                .orElse(Collections.emptyList());
        if (temperatures.isEmpty()) {
            LOGGER.info("Temperatures with date={} not found.", localDateTime.toLocalDate());
        }

        List<TemperatureDto> temperatureDtos = temperatures.stream()
                .map(temperature -> TemperatureEntityMapper.toDto(temperature).get())
                .toList();
        LOGGER.info("Temperatures with date={} retrieved.", localDateTime);

        return temperatureDtos;
    }

    @Override
    public Optional<TemperatureDto> createTemperatureDto(TemperatureDto TemperatureDto) {
        LOGGER.info("Creating Temperature.");

        if (TemperatureDto == null) {
            LOGGER.info("Temperature can't be created.");
            return Optional.empty();
        }

        Optional<Temperature> temperature = TemperatureEntityMapper.toModel(TemperatureDto);
        if (temperature.isEmpty()) {
            LOGGER.info("Temperature mapping failed during creation.");
            return Optional.empty();
        }

        temperatureRepository.createTemperature(temperature.get());
        LOGGER.info("Temperature with id={} and registered_at={} created.",
                temperature.get().getId(),
                temperature.get().getRegisteredAt());

        return Optional.of(TemperatureDto);
    }

    @Override
    public void deleteTemperatureById(UUID id) {
        LOGGER.info("Deleting Temperature with id={}", id);

        if (id == EMPTY_UUID) {
            LOGGER.info(TEMPERATURE_ID_NOT_FOUND, id);
            return;
        }

        Optional<Temperature> temperature = temperatureRepository.getTemperatureById(id);
        if (temperature.isEmpty()) {
            LOGGER.info(TEMPERATURE_ID_NOT_FOUND, id);
            return;
        }

        temperatureRepository.deleteTemperatureById(id);
        LOGGER.info("Temperature with id={} deleted.", id);
    }

    @Override
    public void deleteTemperatureByTimestamp(LocalDateTime timestamp) {
        LOGGER.info("Deleting Temperature with timestamp={}", timestamp);

        Optional<Temperature> temperature = temperatureRepository.getTemperatureByTimestamp(timestamp);
        if (temperature.isEmpty()) {
            LOGGER.info("Temperature with timestamp={} not found.", timestamp);
            return;
        }

        temperatureRepository.deleteTemperatureByTimestamp(timestamp);
        LOGGER.info("Temperature with timestamp={} deleted.", timestamp);
    }
}
