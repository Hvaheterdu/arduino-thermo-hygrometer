package api.arduinothermohygrometer.properties;

import org.springframework.boot.context.properties.bind.Name;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

@Validated
public record OpenApiServerVariableProperties(@Name("default") @NotNull String defaultValue) {}
