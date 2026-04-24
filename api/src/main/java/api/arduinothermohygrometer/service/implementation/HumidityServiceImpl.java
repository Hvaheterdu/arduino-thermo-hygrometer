package api.arduinothermohygrometer.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.HumidityModelMapper;
import api.arduinothermohygrometer.model.Humidity;
import api.arduinothermohygrometer.repository.HumidityRepository;
import api.arduinothermohygrometer.service.HumidityService;
import lombok.extern.slf4j.Slf4j;

import static java.util.Collections.emptyList;

@Slf4j
@Service
public class HumidityServiceImpl implements HumidityService {
    private static final String ID_NOT_FOUND = "Humidity with id=%s not found.";
    private static final String DATETIME_NOT_FOUND = "Humidities with dateTime=%s not found.";

    private final HumidityRepository humidityRepository;

    public HumidityServiceImpl(HumidityRepository humidityRepository) {
        this.humidityRepository = humidityRepository;
    }

    @Override
    public HumidityDto getHumidityById(final UUID id) throws ResourceNotFoundException {
        log.info("Retrieving Humidity with id={}.", id);

        Optional<Humidity> humidity = humidityRepository.getHumidityById(id);
        if (humidity.isEmpty()) {
            log.warn(String.format(DATETIME_NOT_FOUND, id));
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND, id));
        }

        HumidityDto humidityDto = HumidityModelMapper.toDto(humidity.get());
        log.info("Humidity with id={} retrieved.", id);

        return humidityDto;
    }

    @Override
    public List<HumidityDto> getHumiditiesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) {
        log.info("Retrieving humidities with dateTime={}, checkOnlyDate={}.", dateTime, checkOnlyDate);

        List<Humidity> humidities = checkOnlyDate
            ? humidityRepository.getHumiditiesByDate(dateTime.toLocalDate())
            : humidityRepository.getHumidityByTimestamp(dateTime);

        if (humidities.isEmpty()) {
            log.info(String.format(DATETIME_NOT_FOUND, dateTime));
            return emptyList();
        }

        log.info("Humidities with dateTime={} retrieved.", dateTime);
        return humidities.stream()
                         .map(HumidityModelMapper::toDto)
                         .toList();
    }

    @Override
    public HumidityDto createHumidity(final HumidityDto humidityDto) throws ResourceNotCreatedException {
        log.info("Creating Humidity.");

        if (humidityDto == null) {
            log.warn("Humidity cannot be created.");
            throw new ResourceNotCreatedException("Humidity cannot be created.");
        }

        Humidity humidity = HumidityModelMapper.toModel(humidityDto);
        humidityRepository.createHumidity(humidity);
        log.info("Humidity with id={} and registered_at={} created.", humidity.getId(), humidity.getRegisteredAt());

        return HumidityModelMapper.toDto(humidity);
    }

    @Override
    public void deleteHumidityById(final UUID id) throws ResourceNotFoundException {
        log.info("Deleting Humidity with id={}.", id);

        Optional<Humidity> humidity = humidityRepository.getHumidityById(id);
        if (humidity.isEmpty()) {
            log.warn(String.format(ID_NOT_FOUND, id));
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND, id));
        }

        humidityRepository.deleteHumidityById(id);
        log.info("Humidity with id={} deleted.", id);
    }

    @Override
    public void deleteHumiditiesByDateOrTimestamp(final LocalDateTime dateTime, final boolean checkOnlyDate) {
        log.info("Deleting humidities with dateTime={}, checkOnlyDate={}.", dateTime, checkOnlyDate);

        List<Humidity> humidities = checkOnlyDate
            ? humidityRepository.getHumiditiesByDate(dateTime.toLocalDate())
            : humidityRepository.getHumidityByTimestamp(dateTime);

        if (humidities.isEmpty()) {
            log.info(String.format(DATETIME_NOT_FOUND, dateTime));
            return;
        }

        Humidity firstHumidity = humidities.getFirst();
        if (checkOnlyDate) {
            humidityRepository.deleteHumiditiesByDate(firstHumidity.getRegisteredAt().toLocalDate());
            log.info("Deleted humidities with date={}.", firstHumidity.getRegisteredAt().toLocalDate());
        } else {
            humidityRepository.deleteHumidityByTimestamp(firstHumidity.getRegisteredAt());
            log.info("Deleted humidity with timestamp={}.", firstHumidity.getRegisteredAt());
        }
    }
}
