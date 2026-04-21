package api.arduinothermohygrometer.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.HumidityEntityMapper;
import api.arduinothermohygrometer.model.Humidity;
import api.arduinothermohygrometer.repository.HumidityRepository;
import api.arduinothermohygrometer.service.HumidityService;

@Service
public class HumidityServiceImpl implements HumidityService {
    private static final String ID_NOT_FOUND = "Humidity with id=%s not found.";
    private static final String EMPTY_ID_NOT_FOUND = "Humidity with empty id=%s does not exist.";
    private static final String DATETIME_NOT_FOUND = "Humidities with dateTime=%s not found.";
    private static final UUID EMPTY_UUID = new UUID(0, 0);

    private static final Logger LOGGER = LoggerFactory.getLogger(HumidityServiceImpl.class);

    private final HumidityRepository humidityRepository;

    public HumidityServiceImpl(HumidityRepository humidityRepository) {
        this.humidityRepository = humidityRepository;
    }

    @Override
    public HumidityDto getHumidityById(final UUID id) throws ResourceNotFoundException {
        LOGGER.info("Retrieving Humidity with id={}.", id);

        if (EMPTY_UUID.equals(id)) {
            LOGGER.warn("Getting humidity with empty id={} failed.", id);
            throw new ResourceNotFoundException(String.format(EMPTY_ID_NOT_FOUND, id));
        }

        Optional<Humidity> humidity = humidityRepository.getHumidityById(id);
        if (humidity.isEmpty()) {
            LOGGER.warn("Humidity with id={} not found.", id);
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND, id));
        }

        HumidityDto humidityDto = HumidityEntityMapper.toDto(humidity.get());
        LOGGER.info("Humidity with id={} retrieved.", id);

        return humidityDto;
    }

    @Override
    public List<HumidityDto> getHumiditiesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) throws ResourceNotFoundException {
        LOGGER.info("Retrieving humidities with dateTime={}.", dateTime);

        List<Humidity> humidities;
        if (checkOnlyDate) {
            LOGGER.info("Humidities to retrieve with date={}", dateTime.toLocalDate());
            humidities = humidityRepository.getHumiditiesByDate(dateTime.toLocalDate());
        } else {
            LOGGER.info("Humidity to retrieve with timestamp={}", dateTime);
            humidities = humidityRepository.getHumidityByTimestamp(dateTime);
        }

        if (humidities.isEmpty()) {
            LOGGER.warn("Humidities with dateTime={} not found.", dateTime);
            throw new ResourceNotFoundException(String.format(DATETIME_NOT_FOUND, dateTime));
        }

        List<HumidityDto> humidityDtos = humidities.stream()
                                                   .map(HumidityEntityMapper::toDto)
                                                   .toList();
        LOGGER.info("Humidities with dateTime={} retrieved.", dateTime);

        return humidityDtos;
    }

    @Override
    public HumidityDto createHumidity(final HumidityDto humidityDto) throws ResourceNotCreatedException {
        LOGGER.info("Creating Humidity.");

        if (humidityDto == null) {
            LOGGER.warn("Humidity cannot be created.");
            throw new ResourceNotCreatedException("Humidity cannot be created.");
        }

        Humidity humidity = HumidityEntityMapper.toEntity(humidityDto);
        humidityRepository.createHumidity(humidity);
        LOGGER.info("Humidity with id={} and registered_at={} created.", humidity.getId(), humidity.getRegisteredAt());

        return HumidityEntityMapper.toDto(humidity);
    }

    @Override
    public void deleteHumidityById(final UUID id) throws ResourceNotFoundException {
        LOGGER.info("Deleting Humidity with id={}.", id);

        if (EMPTY_UUID.equals(id)) {
            LOGGER.warn("Deleting humidity failed with empty id={} failed.", id);
            throw new ResourceNotFoundException(String.format(EMPTY_ID_NOT_FOUND, id));
        }

        Optional<Humidity> humidity = humidityRepository.getHumidityById(id);
        if (humidity.isEmpty()) {
            LOGGER.warn("Humidity with id={} not deleted.", id);
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND, id));
        }

        humidityRepository.deleteHumidityById(id);
        LOGGER.info("Humidity with id={} deleted.", id);
    }

    @Override
    public void deleteHumiditiesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) throws ResourceNotFoundException {
        LOGGER.info("Deleting humidities with dateTime={}.", dateTime);

        List<Humidity> humidities;
        if (checkOnlyDate) {
            LOGGER.info("Humidities to delete with date={}.", dateTime.toLocalDate());
            humidities = humidityRepository.getHumiditiesByDate(dateTime.toLocalDate());
        } else {
            LOGGER.info("Humidity to delete with timestamp={}", dateTime);
            humidities = humidityRepository.getHumidityByTimestamp(dateTime);
        }

        if (humidities.isEmpty()) {
            LOGGER.warn("Humidities with dateTime={} not found.", dateTime);
            throw new ResourceNotFoundException(String.format(DATETIME_NOT_FOUND, dateTime));
        }

        if (checkOnlyDate) {
            LOGGER.info("Humidities with date={} deleted.", dateTime.toLocalDate());
            humidityRepository.deleteHumiditiesByDate(dateTime.toLocalDate());
        } else {
            LOGGER.info("Humidity with timestamp={} deleted.", dateTime);
            humidityRepository.deleteHumidityByTimestamp(dateTime);
        }
    }
}
