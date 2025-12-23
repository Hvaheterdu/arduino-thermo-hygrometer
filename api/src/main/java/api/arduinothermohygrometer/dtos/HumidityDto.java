package api.arduinothermohygrometer.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record HumidityDto(@JsonIgnore UUID id,
                          @NotNull LocalDateTime registeredAt,
                          @DecimalMin("20.00") @DecimalMax("90.00") BigDecimal airHumidity
) {}
