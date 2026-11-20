package api.arduinothermohygrometer.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;

@ConfigurationProperties(prefix = "springdoc")
@Validated
public record OpenApiServerProperties(@NotEmpty List<OpenApiSingleServerProperties> servers) {}
