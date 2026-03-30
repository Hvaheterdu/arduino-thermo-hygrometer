package api.arduinothermohygrometer.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record HumidityDto(@NotNull LocalDateTime registeredAt,
                          @DecimalMin("20.00") @DecimalMax("90.00") Double airHumidity
) {}
