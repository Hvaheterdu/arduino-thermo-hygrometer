package api.arduinothermohygrometer.properties;

import org.springframework.validation.annotation.Validated;

@Validated
public record OpenApiLicenseProperties(String name, String url) {}
