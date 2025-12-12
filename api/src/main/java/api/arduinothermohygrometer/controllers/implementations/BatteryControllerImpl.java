package api.arduinothermohygrometer.controllers.implementations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.arduinothermohygrometer.controllers.BatteryController;
import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.services.BatteryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    @Parameter(name = "timestamp", in = ParameterIn.PATH, description = "Identifier for battery", required = true)
    @GetMapping(path = "/{id}", produces = "application/json", version = "v1.0.0")
    public ResponseEntity<BatteryDto> getBatteryById(@PathVariable UUID id) {
        Optional<BatteryDto> batteryDto = batteryService.getBatteryDtoById(id);

        return batteryDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Retrieve a battery by timestamp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Battery found."),
            @ApiResponse(responseCode = "404", description = "Battery not found.")
    })
    @Parameter(name = "timestamp", in = ParameterIn.PATH, description = "timestamp for battery", required = true)
    @GetMapping(path = "/{timestamp}", produces = "application/json", version = "v1.0.0")
    public ResponseEntity<BatteryDto> getBatteryByTimestamp(@PathVariable LocalDateTime timestamp) {
        Optional<BatteryDto> batteryDto = batteryService.getBatteryDtoByTimestamp(timestamp);

        return batteryDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
