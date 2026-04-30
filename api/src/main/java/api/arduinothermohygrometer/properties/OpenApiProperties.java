package api.arduinothermohygrometer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "info")
public record OpenApiProperties(String title,
                                String description,
                                OpenApiContactProperties contact,
                                OpenApiLicenseProperties license
) {
}
