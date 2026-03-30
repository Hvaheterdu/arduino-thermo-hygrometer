package api.arduinothermohygrometer.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BatteryDto(@NotNull LocalDateTime registeredAt,
                         @Min(0) @Max(100) int batteryStatus
) {}
