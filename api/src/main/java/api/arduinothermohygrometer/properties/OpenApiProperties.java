package api.arduinothermohygrometer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "info")
@Validated
public record OpenApiProperties(
    String title,
    String description,
    OpenApiContactProperties contact,
    OpenApiLicenseProperties license) {}
