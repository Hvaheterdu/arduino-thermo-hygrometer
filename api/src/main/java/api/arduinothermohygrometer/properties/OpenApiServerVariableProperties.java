package api.arduinothermohygrometer.properties;

import org.springframework.boot.context.properties.bind.Name;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenApiServerVariableProperties {
    @Name("default")
    private @NotNull String defaultValue = "";
}
