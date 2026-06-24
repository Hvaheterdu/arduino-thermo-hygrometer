package api.arduinothermohygrometer.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(@NotBlank String apiKey,
                                 @NotBlank String apiHeaderName,
                                 @NotEmpty List<String> apiRoles,
                                 @NotBlank String apiSchemeName) {
}
