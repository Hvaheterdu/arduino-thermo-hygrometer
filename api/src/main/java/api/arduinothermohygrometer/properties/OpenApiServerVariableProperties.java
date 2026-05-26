package api.arduinothermohygrometer.properties;

import org.springframework.boot.context.properties.bind.Name;

import jakarta.validation.constraints.NotNull;

public record OpenApiServerVariableProperties(@Name("default") @NotNull String _default) {
}
