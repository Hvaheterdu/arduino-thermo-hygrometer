package api.arduinothermohygrometer.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.exceptions.ResourceMappingFailedException;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;

public interface HumidityController {
    ResponseEntity<HumidityDto> getHumidityById(@PathVariable UUID id) throws ResourceNotFoundException;

    ResponseEntity<HumidityDto> getHumidityByTimestamp(@PathVariable LocalDateTime timestamp)
            throws ResourceNotFoundException;

    ResponseEntity<List<HumidityDto>> getHumiditiesByDate(@PathVariable LocalDateTime date);

    ResponseEntity<HumidityDto> create(@Valid @RequestBody final HumidityDto humidityDto)
            throws ResourceNotCreatedException, ResourceMappingFailedException;

    ResponseEntity<Void> deleteHumidityDtoById(@PathVariable UUID id) throws ResourceNotFoundException;

    ResponseEntity<Void> deleteHumidityByTimestamp(LocalDateTime timestamp) throws ResourceNotFoundException;

}
