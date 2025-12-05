package api.arduinothermohygrometer.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record BatteryDto(
        @JsonIgnore @NotEmpty @NotNull @Pattern(regexp = "[a-f0-9]{8}(?:-[a-f0-9]{4}){4}[a-f0-9]{8}") UUID id,
        @NotEmpty @NotNull LocalDateTime registeredAt,
        @NotEmpty @NotNull @Min(0) @Max(100) @Pattern(regexp = "^\\d+\\.\\d+$") int batteryStatus) {
}
