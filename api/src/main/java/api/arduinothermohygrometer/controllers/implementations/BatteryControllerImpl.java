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

import api.arduinothermohygrometer.controllers.BatteryController;
import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.exceptions.ResourceNotCreatedException;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.services.BatteryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/api/batteries")
@Tag(name = "Battery component.", description = "Battery status of arduino device.")
public class BatteryControllerImpl implements BatteryController {
    private final BatteryService batteryService;

    public BatteryControllerImpl(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    @Override
    @Operation(summary = "Retrieve a battery by id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Battery found."),
        @ApiResponse(responseCode = "404", description = "Battery not found.")
    })
    @Parameter(name = "id", in = ParameterIn.PATH, description = "identifier for battery", required = true)
    @GetMapping(path = "/id/{id}", produces = "application/json")
    public ResponseEntity<BatteryDto> getBatteryById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        BatteryDto batteryDto = batteryService.getBatteryById(id);

        return new ResponseEntity<>(batteryDto, HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Retrieve a battery by timestamp.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Battery found."),
        @ApiResponse(responseCode = "404", description = "Battery not found.")
    })
    @Parameter(name = "timestamp", in = ParameterIn.QUERY, description = "timestamp for battery", required = true)
    @GetMapping(path = "/timestamp", produces = "application/json")
    public ResponseEntity<BatteryDto> getBatteryByTimestamp(@RequestParam("timestamp") LocalDateTime timestamp) throws ResourceNotFoundException {
        BatteryDto batteryDto = batteryService.getBatteryByTimestamp(timestamp);

        return new ResponseEntity<>(batteryDto, HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Retrieve batteries by date.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Batteries found."),
        @ApiResponse(responseCode = "404", description = "Batteries not found.")
    })
    @Parameter(name = "date", in = ParameterIn.QUERY, description = "date for batteries", required = true)
    @GetMapping(path = "/date", produces = "application/json")
    public ResponseEntity<List<BatteryDto>> getBatteriesByDate(@RequestParam("date") LocalDate date) throws ResourceNotFoundException {
        List<BatteryDto> batteries = batteryService.getBatteriesByDate(date);

        return new ResponseEntity<>(batteries, HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Create battery.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Battery created."),
        @ApiResponse(responseCode = "400", description = "Battery failed to be created.")
    })
    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BatteryDto> create(@Valid @RequestBody final BatteryDto batteryDto) throws ResourceNotCreatedException {
        BatteryDto createdBatteryDto = batteryService.createBattery(batteryDto);

        return new ResponseEntity<>(createdBatteryDto, HttpStatus.CREATED);
    }

    @Override
    @Operation(summary = "Delete battery by id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Battery deleted."),
        @ApiResponse(responseCode = "400", description = "Battery failed to be deleted.")
    })
    @Parameter(name = "id", in = ParameterIn.PATH, description = "identifier for battery", required = true)
    @DeleteMapping(path = "/delete/id/{id}")
    public ResponseEntity<Void> deleteBatteryDtoById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        batteryService.deleteBatteryById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @Operation(summary = "Delete battery by timestamp.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Battery deleted."),
        @ApiResponse(responseCode = "400", description = "Battery failed to be deleted.")
    })
    @Parameter(name = "timestamp", in = ParameterIn.QUERY, description = "timestamp for battery", required = true)
    @DeleteMapping(path = "/delete/timestamp")
    public ResponseEntity<Void> deleteBatteryByTimestamp(@RequestParam("timestamp") LocalDateTime timestamp) throws ResourceNotFoundException {
        batteryService.deleteBatteryByTimestamp(timestamp);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
