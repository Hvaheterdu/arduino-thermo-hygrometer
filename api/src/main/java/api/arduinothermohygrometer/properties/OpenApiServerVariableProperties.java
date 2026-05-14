package api.arduinothermohygrometer.properties;

import java.util.List;

import org.springframework.boot.context.properties.bind.Name;

import jakarta.validation.constraints.NotNull;

public record OpenApiServerVariableProperties(@Name("default") @NotNull String _default,
                                              @Name("enum") @NotNull List<String> _enum
) {
}
