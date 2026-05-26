package api.arduinothermohygrometer.service.implementation;

import static java.util.Collections.emptyList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.exception.ResourceNotCreatedException;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.TemperatureModelMapper;
import api.arduinothermohygrometer.model.Temperature;
import api.arduinothermohygrometer.repository.TemperatureRepository;
import api.arduinothermohygrometer.service.TemperatureService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TemperatureServiceImpl implements TemperatureService {
    private static final String ID_NOT_FOUND = "Temperature with id=%s not found.";
    private static final String REGISTERED_AT_NOT_FOUND = "Temperatures registeredAt={} not found.";

    private final TemperatureRepository temperatureRepository;

    public TemperatureServiceImpl(final TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    @Override
    public TemperatureDto getTemperatureById(final UUID id) throws ResourceNotFoundException {
        log.info("Retrieving Temperature with id={}.", id);

        Temperature temperature = temperatureRepository.getTemperatureById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND, id)));

        log.info("Temperature with id={} retrieved.", id);
        return TemperatureModelMapper.toDto(temperature);
    }

    @Override
    public List<TemperatureDto> getTemperaturesByDateOrTimestamp(final LocalDateTime registeredAt, final boolean dateOnly) {
        log.info("Retrieving temperatures registeredAt={}, dateOnly={}.", registeredAt, dateOnly);

        List<Temperature> temperatures = dateOnly
                ? temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())
                : temperatureRepository.getTemperatureByTimestamp(registeredAt);

        if (temperatures.isEmpty()) {
            log.info(REGISTERED_AT_NOT_FOUND, registeredAt);
            return emptyList();
        }

        log.info("Temperatures registeredAt={} retrieved.", registeredAt);
        return temperatures.stream()
                .map(TemperatureModelMapper::toDto)
                .toList();
    }

    @Override
    public TemperatureDto createTemperature(final TemperatureDto temperatureDto) throws ResourceNotCreatedException {
        log.info("Creating Temperature.");

        if (temperatureDto == null) {
            throw new ResourceNotCreatedException("Temperature cannot be created.");
        }

        Temperature temperature = temperatureRepository.createTemperature(TemperatureModelMapper.toModel(temperatureDto))
                .orElseThrow(() -> new ResourceNotCreatedException("Temperature cannot be created."));

        log.info("Temperature with id={} and registered_at={} created.", temperature.getId(), temperature.getRegisteredAt());
        return TemperatureModelMapper.toDto(temperature);
    }

    @Override
    public void deleteTemperatureById(final UUID id) throws ResourceNotFoundException {
        log.info("Deleting Temperature with id={}.", id);

        Optional<Temperature> temperature = temperatureRepository.getTemperatureById(id);
        if (temperature.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ID_NOT_FOUND, id));
        }

        temperatureRepository.deleteTemperatureById(id);
        log.info("Temperature with id={} deleted.", id);
    }

    @Override
    public void deleteTemperaturesByDateOrTimestamp(final LocalDateTime registeredAt, final boolean dateOnly) throws ResourceNotFoundException {
        log.info("Deleting temperatures registeredAt={}, dateOnly={}.", registeredAt, dateOnly);

        List<Temperature> temperatures = dateOnly
                ? temperatureRepository.getTemperaturesByDate(registeredAt.toLocalDate())
                : temperatureRepository.getTemperatureByTimestamp(registeredAt);

        if (temperatures.isEmpty()) {
            log.info(REGISTERED_AT_NOT_FOUND, registeredAt);
            return;
        }

        Temperature firstTemperature = temperatures.getFirst();
        if (dateOnly) {
            temperatureRepository.deleteTemperaturesByDate(firstTemperature.getRegisteredAt().toLocalDate());
            log.info("Deleted temperatures with date={}.", firstTemperature.getRegisteredAt().toLocalDate());
        } else {
            temperatureRepository.deleteTemperatureByTimestamp(firstTemperature.getRegisteredAt());
            log.info("Deleted temperature with timestamp={}.", firstTemperature.getRegisteredAt());
        }
    }
}
