package api.arduinothermohygrometer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "info")
@Data
public class OpenApiProperties {
    private String version;
    private String title;
    private String description;
    private OpenApiContactProperties contact = new OpenApiContactProperties();
    private OpenApiLicenseProperties license = new OpenApiLicenseProperties();
}
