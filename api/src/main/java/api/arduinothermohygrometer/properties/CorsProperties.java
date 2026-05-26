package api.arduinothermohygrometer.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotEmpty;

@ConfigurationProperties(prefix = "cors")
public record CorsProperties(@NotEmpty List<String> allowedHeaders, @NotEmpty List<String> allowedMethods, @NotEmpty List<String> allowedOrigins) {
}
