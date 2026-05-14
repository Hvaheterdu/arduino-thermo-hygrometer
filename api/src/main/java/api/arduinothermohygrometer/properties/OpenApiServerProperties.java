package api.arduinothermohygrometer.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotEmpty;

@ConfigurationProperties(prefix = "springdoc")
public record OpenApiServerProperties(@NotEmpty List<OpenApiSingleServerProperties> servers) {
}
