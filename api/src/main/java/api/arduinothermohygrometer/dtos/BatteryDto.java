package api.arduinothermohygrometer.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BatteryDto(@JsonIgnore UUID id, @NotNull LocalDateTime registeredAt, @Min(0) @Max(100) int batteryStatus
) {}
