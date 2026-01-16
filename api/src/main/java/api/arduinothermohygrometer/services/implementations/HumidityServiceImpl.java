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

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.entities.Humidity;
import api.arduinothermohygrometer.exceptions.ResourceMappingFailedException;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.mappers.HumidityEntityMapper;
import api.arduinothermohygrometer.repositories.HumidityRepository;
import api.arduinothermohygrometer.services.HumidityService;

@Service
public class HumidityServiceImpl implements HumidityService {
    private static final String HUMIDITY_ID_NOT_FOUND_EXCEPTION = "Humidity with id=%s not found.";
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    private static final Logger LOGGER = LoggerFactory.getLogger(HumidityServiceImpl.class);

    private final HumidityRepository humidityRepository;

    public HumidityServiceImpl(HumidityRepository humidityRepository) {
        LOGGER.info("Initialising HumidityService.");
        this.humidityRepository = humidityRepository;
    }

    @Override
    public HumidityDto getHumidityDtoById(UUID id) throws ResourceNotFoundException {
        LOGGER.info("Retrieving Humidity with id={}.", id);

        if (id == EMPTY_UUID) {
            LOGGER.warn("Getting humidity failed. Invalid id={}.", id);
            throw new ResourceNotFoundException(String.format(HUMIDITY_ID_NOT_FOUND_EXCEPTION, id));
        }

        Optional<Humidity> humidity = humidityRepository.getHumidityById(id);
        if (humidity.isEmpty()) {
            LOGGER.warn("Humidity with id={} not found.", id);
            throw new ResourceNotFoundException(String.format(HUMIDITY_ID_NOT_FOUND_EXCEPTION, id));
        }

        HumidityDto humidityDto = HumidityEntityMapper.toDto(humidity.get());
        LOGGER.info("Humidity with id={} retrieved.", humidityDto);

        return humidityDto;
    }

    @Override
    public HumidityDto getHumidityDtoByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException {
        LOGGER.info("Retrieving Humidity with timestamp={}.", timestamp);

        Optional<Humidity> humidity = humidityRepository.getHumidityByTimestamp(timestamp);
        if (humidity.isEmpty()) {
            LOGGER.warn("Humidity with timestamp={} not found.", timestamp);
            throw new ResourceNotFoundException(String.format("Humidity with timestamp=%s not found.", timestamp));
        }

        HumidityDto humidityDto = HumidityEntityMapper.toDto(humidity.get());
        LOGGER.info("Humidity with timestamp={} retrieved.", timestamp);

        return humidityDto;
    }

    @Override
    public List<HumidityDto> getHumidityDtosByDate(LocalDate date) {
        LOGGER.info("Retrieving temperatures with date={}.", date);

        List<Humidity> humidities = Optional.ofNullable(humidityRepository.getHumiditiesByDate(date))
                                            .orElse(Collections.emptyList());
        if (humidities.isEmpty()) {
            LOGGER.warn("Humidities with date={} not found.", date);
            throw new ResourceNotFoundException(String.format("Humidities with date=%s not found.", date));
        }

        List<HumidityDto> humidityDtos = humidities.stream()
                                                   .map(HumidityEntityMapper::toDto)
                                                   .toList();
        LOGGER.info("Humidities with date={} retrieved.", date);

        return humidityDtos;
    }

    @Override
    public HumidityDto createHumidityDto(HumidityDto humidityDto) throws ResourceNotCreatedException, ResourceMappingFailedException {
        LOGGER.info("Creating Humidity.");

        if (humidityDto == null) {
            LOGGER.warn("Humidity cannot be created.");
            throw new ResourceNotCreatedException("Humidity cannot be created.");
        }

        Humidity humidity = HumidityEntityMapper.toModel(humidityDto);
        if (humidity == null) {
            LOGGER.warn("Humidity mapping failed during creation.");
            throw new ResourceMappingFailedException("Humidity mapping failed during creation.");
        }

        humidityRepository.createHumidity(humidity);
        LOGGER.info("Humidity with id={} and registered_at={} created.", humidity.getId(), humidity.getRegisteredAt());

        return humidityDto;
    }

    @Override
    public void deleteHumidityById(UUID id) throws ResourceNotFoundException {
        LOGGER.info("Deleting Humidity with id={}.", id);

        if (id == EMPTY_UUID) {
            LOGGER.warn("Deleting humidity failed. Invalid id={}.", id);
            throw new ResourceNotFoundException(String.format(HUMIDITY_ID_NOT_FOUND_EXCEPTION, id));
        }

        Optional<Humidity> humidity = humidityRepository.getHumidityById(id);
        if (humidity.isEmpty()) {
            LOGGER.warn("Humidity with id={} not deleted.", id);
            throw new ResourceNotFoundException(String.format(HUMIDITY_ID_NOT_FOUND_EXCEPTION, id));
        }

        humidityRepository.deleteHumidityById(id);
        LOGGER.info("Humidity with id={} deleted.", id);
    }

    @Override
    public void deleteHumidityByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException {
        LOGGER.info("Deleting Humidity with timestamp={}.", timestamp);

        Optional<Humidity> humidity = humidityRepository.getHumidityByTimestamp(timestamp);
        if (humidity.isEmpty()) {
            LOGGER.warn("Humidity with timestamp={} not deleted.", timestamp);
            throw new ResourceNotFoundException(String.format("Humidity with timestamp=%s not found.", timestamp));
        }

        humidityRepository.deleteHumidityByTimestamp(timestamp);
        LOGGER.info("Humidity with timestamp={} deleted.", timestamp);
    }
}
