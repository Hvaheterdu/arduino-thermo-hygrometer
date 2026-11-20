package api.arduinothermohygrometer.properties;

import org.springframework.validation.annotation.Validated;

@Validated
public record OpenApiContactProperties(String name, String email) {}
