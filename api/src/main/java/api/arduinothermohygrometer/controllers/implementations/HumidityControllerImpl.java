package api.arduinothermohygrometer.controllers.implementations;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.controllers.HumidityController;
import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.services.HumidityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/api/humidities")
@Tag(name = "Humidity component.", description = "Humidity measured by arduino device.")
public class HumidityControllerImpl implements HumidityController {
    private final HumidityService humidityService;

    public HumidityControllerImpl(HumidityService humidityService) {
        this.humidityService = humidityService;
    }

    @Override
    @Operation(summary = "Retrieve measured humidity by id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Humidity found."),
        @ApiResponse(responseCode = "404", description = "Humidity not found.")
    })
    @Parameter(name = "id", in = ParameterIn.PATH, description = "identifier for measured humidity", required = true)
    @GetMapping(path = "/id/{id}", produces = "application/json")
    public ResponseEntity<HumidityDto> getHumidityById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        HumidityDto humidityDto = humidityService.getHumidityById(id);

        return new ResponseEntity<>(humidityDto, HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Retrieve measured humidity by timestamp.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Humidity found."),
        @ApiResponse(responseCode = "404", description = "Humidity not found.")
    })
    @Parameter(name = "timestamp", in = ParameterIn.QUERY, description = "timestamp for measured humidity", required = true)
    @GetMapping(path = "/timestamp", produces = "application/json")
    public ResponseEntity<HumidityDto> getHumidityByTimestamp(@RequestParam("timestamp") LocalDateTime timestamp) throws ResourceNotFoundException {
        HumidityDto humidityDto = humidityService.getHumidityByTimestamp(timestamp);

        return new ResponseEntity<>(humidityDto, HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Retrieve measured humidities by date.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Humidities found."),
        @ApiResponse(responseCode = "404", description = "Humidities not found.")
    })
    @Parameter(name = "date", in = ParameterIn.QUERY, description = "date for measured humidities", required = true)
    @GetMapping(path = "/date", produces = "application/json")
    public ResponseEntity<List<HumidityDto>> getHumiditiesByDate(@RequestParam("date") LocalDate date) {
        List<HumidityDto> humidities = humidityService.getHumiditiesByDate(date);

        return new ResponseEntity<>(humidities, HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Create measured humidity.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Humidity created."),
        @ApiResponse(responseCode = "400", description = "Humidity failed to be created.")
    })
    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<HumidityDto> create(@Valid @RequestBody HumidityDto humidityDto) throws ResourceNotCreatedException {
        HumidityDto createdHumidityDto = humidityService.createHumidity(humidityDto);

        return new ResponseEntity<>(createdHumidityDto, HttpStatus.CREATED);
    }

    @Override
    @Operation(summary = "Delete measured humidity by id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Humidity deleted."),
        @ApiResponse(responseCode = "400", description = "Humidity failed to be deleted.")
    })
    @Parameter(name = "id", in = ParameterIn.PATH, description = "identifier for measured humidity", required = true)
    @DeleteMapping(path = "/delete/id/{id}")
    public ResponseEntity<Void> deleteHumidityDtoById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        humidityService.deleteHumidityById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @Operation(summary = "Delete measured humidity by timestamp.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Humidity deleted."),
        @ApiResponse(responseCode = "400", description = "Humidity failed to be deleted.")
    })
    @Parameter(name = "timestamp", in = ParameterIn.QUERY, description = "timestamp for measured humidity", required = true)
    @DeleteMapping(path = "/delete/timestamp")
    public ResponseEntity<Void> deleteHumidityByTimestamp(@RequestParam("timestamp") LocalDateTime timestamp) throws ResourceNotFoundException {
        humidityService.deleteHumidityByTimestamp(timestamp);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
