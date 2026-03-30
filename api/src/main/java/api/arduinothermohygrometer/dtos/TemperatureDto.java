package api.arduinothermohygrometer.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TemperatureDto(@NotNull LocalDateTime registeredAt,
                             @DecimalMin("-55.00") @DecimalMax("125.00") Double temp
) {}
