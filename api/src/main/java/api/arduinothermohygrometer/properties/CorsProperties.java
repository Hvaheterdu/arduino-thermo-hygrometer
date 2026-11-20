package api.arduinothermohygrometer.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;

@ConfigurationProperties(prefix = "cors")
@Validated
public record CorsProperties(
    @NotEmpty List<String> allowedHeaders,
    @NotEmpty List<String> allowedMethods,
    @NotEmpty List<String> allowedOrigins) {}
