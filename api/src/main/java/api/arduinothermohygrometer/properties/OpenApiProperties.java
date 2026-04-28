package api.arduinothermohygrometer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "info")
@Data
public class OpenApiProperties {
    private String title;
    private String description;
    private OpenApiContactProperties contact = new OpenApiContactProperties(getContact().name(), getContact().email());
    private OpenApiLicenseProperties license = new OpenApiLicenseProperties(getLicense().name(), getLicense().url());
}
