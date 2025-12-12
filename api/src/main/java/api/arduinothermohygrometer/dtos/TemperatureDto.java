package api.arduinothermohygrometer.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record TemperatureDto(
        @JsonIgnore @NotEmpty @NotNull @Pattern(regexp = "[a-f0-9]{8}(?:-[a-f0-9]{4}){4}[a-f0-9]{8}") UUID id,
        @NotEmpty @NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "no_NO") LocalDateTime registeredAt,
        @NotEmpty @NotNull @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT) @DecimalMin("-55.00") @DecimalMax("125.00") @Pattern(regexp = "^[+-]?\\d+\\.\\d+$") BigDecimal temp) {
}
