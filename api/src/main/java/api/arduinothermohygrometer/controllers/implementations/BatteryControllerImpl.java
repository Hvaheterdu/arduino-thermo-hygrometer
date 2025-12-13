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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.controllers.BatteryController;
import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.exceptions.ResourceMappingFailedException;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.services.BatteryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/batteries")
@Tag(name = "Battery component.", description = "Battery status of arduino device.")
public class BatteryControllerImpl implements BatteryController {
    private final BatteryService batteryService;

    public BatteryControllerImpl(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    @Operation(summary = "Retrieve a battery by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Battery found."),
            @ApiResponse(responseCode = "404", description = "Battery not found.")
    })
    @Parameter(name = "id", in = ParameterIn.PATH, description = "identifier for battery", required = true)
    @GetMapping(path = "/{id}", produces = "application/json", version = "v1.0.0")
    public ResponseEntity<BatteryDto> getBatteryById(@PathVariable UUID id) throws ResourceNotFoundException {
        BatteryDto batteryDto = batteryService.getBatteryDtoById(id);
        if (batteryDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Retrieve a battery by timestamp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Battery found."),
            @ApiResponse(responseCode = "404", description = "Battery not found.")
    })
    @Parameter(name = "timestamp", in = ParameterIn.PATH, description = "timestamp for battery", required = true)
    @GetMapping(path = "/{timestamp}", produces = "application/json", version = "v1.0.0")
    public ResponseEntity<BatteryDto> getBatteryByTimestamp(@PathVariable LocalDateTime timestamp)
            throws ResourceNotFoundException {
        BatteryDto batteryDto = batteryService.getBatteryDtoByTimestamp(timestamp);
        if (batteryDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Retrieve batteries by date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batteries found."),
            @ApiResponse(responseCode = "404", description = "Batteries not found.")
    })
    @Parameter(name = "date", in = ParameterIn.PATH, description = "date for batteries", required = true)
    @GetMapping(path = "/{date}", produces = "application/json", version = "v1.0.0")
    public ResponseEntity<List<BatteryDto>> getBatteriesByDate(@PathVariable LocalDateTime date) {
        List<BatteryDto> batteries = batteryService.getBatteryDtosByDate(date);
        if (batteries == null || batteries.isEmpty()) {
            return new ResponseEntity<>(batteries, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(batteries, HttpStatus.OK);
    }

    @Operation(summary = "Create battery.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Battery created."),
            @ApiResponse(responseCode = "400", description = "Battery failed to be created.")
    })
    @Parameter(name = "batteryDto", description = "batteryDto object", required = true)
    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json", version = "v1.0.0")
    public ResponseEntity<BatteryDto> create(@Valid @RequestBody BatteryDto batteryDto)
            throws ResourceNotCreatedException, ResourceMappingFailedException {
        BatteryDto createdBatteryDto = batteryService.createBatteryDto(batteryDto);

        return new ResponseEntity<>(createdBatteryDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete battery by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Battery deleted."),
            @ApiResponse(responseCode = "400", description = "Battery failed to be deleted.")
    })
    @Parameter(name = "id", in = ParameterIn.PATH, description = "identifier for battery", required = true)
    @DeleteMapping(path = "/delete/{id}", version = "v1.0.0")
    public ResponseEntity<Void> deleteBatteryDtoById(@PathVariable UUID id) throws ResourceNotFoundException {
        batteryService.deleteBatteryById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete battery by timestamp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Battery deleted."),
            @ApiResponse(responseCode = "400", description = "Battery failed to be deleted.")
    })
    @Parameter(name = "timestamp", in = ParameterIn.PATH, description = "timestamp for battery", required = true)
    @DeleteMapping(path = "/delete/{timestamp}", version = "v1.0.0")
    public ResponseEntity<Void> deleteBatteryByTimestamp(@PathVariable LocalDateTime timestamp)
            throws ResourceNotFoundException {
        batteryService.deleteBatteryByTimestamp(timestamp);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
