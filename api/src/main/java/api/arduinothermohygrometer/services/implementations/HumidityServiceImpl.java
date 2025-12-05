package api.arduinothermohygrometer.services.implementations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.entities.Humidity;
import api.arduinothermohygrometer.mappers.HumidityEntityMapper;
import api.arduinothermohygrometer.repositories.HumidityRepository;
import api.arduinothermohygrometer.services.HumidityService;

@Service
public class HumidityServiceImpl implements HumidityService {
    private static final String HUMIDITY_ID_NOT_FOUND = "Humidity with id={} not found.";
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    private static final Logger LOGGER = LoggerFactory.getLogger(HumidityServiceImpl.class);

    private final HumidityRepository humidityRepository;

    public HumidityServiceImpl(HumidityRepository humidityRepository) {
        LOGGER.info("Initialising HumidityService");
        this.humidityRepository = humidityRepository;
    }

    @Override
    public Optional<HumidityDto> getHumidityDtoById(UUID id) {
        LOGGER.info("Retrieving Humidity with id={}", id);

        if (id == EMPTY_UUID) {
            LOGGER.info(HUMIDITY_ID_NOT_FOUND, id);
            return Optional.empty();
        }

        Optional<Humidity> humidity = humidityRepository.getHumidityById(id);
        if (humidity.isEmpty()) {
            LOGGER.info(HUMIDITY_ID_NOT_FOUND, id);
            return Optional.empty();
        }

        Optional<HumidityDto> humidityDto = HumidityEntityMapper.toDto(humidity.get());
        LOGGER.info("Humidity with id={} retrieved.", humidityDto);

        return humidityDto;
    }

    @Override
    public Optional<HumidityDto> getHumidityDtoByTimestamp(LocalDateTime timestamp) {
        LOGGER.info("Retrieving Humidity with timestamp={}", timestamp);

        Optional<Humidity> humidity = humidityRepository.getHumidityByTimestamp(timestamp);
        if (humidity.isEmpty()) {
            LOGGER.info("Humidity with timestamp={} not found.", timestamp);
            return Optional.empty();
        }

        Optional<HumidityDto> humidityDto = HumidityEntityMapper.toDto(humidity.get());
        LOGGER.info("Humidity with timestamp={} retrieved.", timestamp);

        return humidityDto;
    }

    @Override
    public List<HumidityDto> getHumidityDtosByDate(LocalDateTime localDateTime) {
        LOGGER.info("Retrieving temperatures with date={}", localDateTime.toLocalDate());

        List<Humidity> humidities = Optional.ofNullable(humidityRepository.getHumiditiesByDate(localDateTime))
                .orElse(Collections.emptyList());
        if (humidities.isEmpty()) {
            LOGGER.info("Humidities with date={} not found.", localDateTime.toLocalDate());
        }

        List<HumidityDto> humidityDtos = humidities.stream()
                .map(humidity -> HumidityEntityMapper.toDto(humidity).get())
                .toList();
        LOGGER.info("Humidities with date={} retrieved.", localDateTime);

        return humidityDtos;
    }

    @Override
    public Optional<HumidityDto> createHumidityDto(HumidityDto humidityDto) {
        LOGGER.info("Creating Humidity.");

        if (humidityDto == null) {
            LOGGER.info("Humidity can't be created.");
            return Optional.empty();
        }

        Optional<Humidity> humidity = HumidityEntityMapper.toModel(humidityDto);
        if (humidity.isEmpty()) {
            LOGGER.info("Humidity mapping failed during creation.");
            return Optional.empty();
        }

        humidityRepository.createHumidity(humidity.get());
        LOGGER.info("Humidity with id={} and registered_at={} created.",
                humidity.get().getId(),
                humidity.get().getRegisteredAt());

        return Optional.of(humidityDto);
    }

    @Override
    public void deleteHumidityById(UUID id) {
        LOGGER.info("Deleting Humidity with id={}", id);

        if (id == EMPTY_UUID) {
            LOGGER.info(HUMIDITY_ID_NOT_FOUND, id);
            return;
        }

        Optional<Humidity> humidity = humidityRepository.getHumidityById(id);
        if (humidity.isEmpty()) {
            LOGGER.info(HUMIDITY_ID_NOT_FOUND, id);
            return;
        }

        humidityRepository.deleteHumidityById(id);
        LOGGER.info("Humidity with id={} deleted.", id);
    }

    @Override
    public void deleteHumidityByTimestamp(LocalDateTime timestamp) {
        LOGGER.info("Deleting Humidity with timestamp={}", timestamp);

        Optional<Humidity> humidity = humidityRepository.getHumidityByTimestamp(timestamp);
        if (humidity.isEmpty()) {
            LOGGER.info("Humidity with timestamp={} not found.", timestamp);
            return;
        }

        humidityRepository.deleteHumidityByTimestamp(timestamp);
        LOGGER.info("Humidity with timestamp={} deleted.", timestamp);
    }
}
