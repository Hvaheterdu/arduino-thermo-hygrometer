package api.arduinothermohygrometer.controllers.implementations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.controllers.TemperatureController;
import api.arduinothermohygrometer.dtos.TemperatureDto;
import api.arduinothermohygrometer.exceptions.ResourceMappingFailedException;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.services.TemperatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/temperatures")
@Tag(name = "Temperature component.", description = "Temperature measured by arduino device.")
public class TemperatureControllerImpl implements TemperatureController {
    private final TemperatureService temperatureService;

    public TemperatureControllerImpl(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @Operation(summary = "Retrieve measured temperature by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Temperature found."),
            @ApiResponse(responseCode = "404", description = "Temperature not found.")
    })
    @Parameter(name = "id", in = ParameterIn.PATH, description = "identifier for measured temperature", required = true)
    @GetMapping(path = "/{id}", produces = "application/json", version = "v1.0.0")
    public ResponseEntity<TemperatureDto> getTemperatureById(@PathVariable UUID id) throws ResourceNotFoundException {
        TemperatureDto temperatureDto = temperatureService.getTemperatureDtoById(id);
        if (temperatureDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Retrieve measured temperature by timestamp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Temperature found."),
            @ApiResponse(responseCode = "404", description = "Temperature not found.")
    })
    @Parameter(name = "timestamp", in = ParameterIn.PATH, description = "timestamp for measured temperature", required = true)
    @GetMapping(path = "/{timestamp}", produces = "application/json", version = "v1.0.0")
    public ResponseEntity<TemperatureDto> getTemperatureByTimestamp(@PathVariable LocalDateTime timestamp)
            throws ResourceNotFoundException {
        TemperatureDto temperatureDto = temperatureService.getTemperatureDtoByTimestamp(timestamp);
        if (temperatureDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Retrieve measured temperatures by date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Temperatures found."),
            @ApiResponse(responseCode = "404", description = "Temperatures not found.")
    })
    @Parameter(name = "date", in = ParameterIn.PATH, description = "date for measured temperatures", required = true)
    @GetMapping(path = "/{date}", produces = "application/json", version = "v1.0.0")
    public ResponseEntity<List<TemperatureDto>> getTemperaturessByDate(@PathVariable LocalDateTime date) {
        List<TemperatureDto> temperatures = temperatureService.getTemperatureDtosByDate(date);
        if (temperatures == null || temperatures.isEmpty()) {
            return new ResponseEntity<>(temperatures, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(temperatures, HttpStatus.OK);
    }

    @Operation(summary = "Create measured temperature.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Temperature created."),
            @ApiResponse(responseCode = "400", description = "Temperature failed to be created.")
    })
    @Parameter(name = "temperatureDto", description = "temperatureDto object", required = true)
    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json", version = "v1.0.0")
    public ResponseEntity<TemperatureDto> create(@Valid @RequestBody TemperatureDto temperatureDto)
            throws ResourceNotCreatedException, ResourceMappingFailedException {
        TemperatureDto createdTemperatureDto = temperatureService.createTemperatureDto(temperatureDto);

        return new ResponseEntity<>(createdTemperatureDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete measured temperature by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Temperature deleted."),
            @ApiResponse(responseCode = "400", description = "Temperature failed to be deleted.")
    })
    @Parameter(name = "id", in = ParameterIn.PATH, description = "identifier for measured temperature", required = true)
    @DeleteMapping(path = "/delete/{id}", version = "v1.0.0")
    public ResponseEntity<Void> deleteTemperatureDtoById(@PathVariable UUID id) throws ResourceNotFoundException {
        temperatureService.deleteTemperatureById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete measured temperature by timestamp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Temperature deleted."),
            @ApiResponse(responseCode = "400", description = "Temperature failed to be deleted.")
    })
    @Parameter(name = "timestamp", in = ParameterIn.PATH, description = "timestamp for measured temperature", required = true)
    @DeleteMapping(path = "/delete/{timestamp}", version = "v1.0.0")
    public ResponseEntity<Void> deleteTemperatureByTimestamp(@PathVariable LocalDateTime timestamp)
            throws ResourceNotFoundException {
        temperatureService.deleteTemperatureByTimestamp(timestamp);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
