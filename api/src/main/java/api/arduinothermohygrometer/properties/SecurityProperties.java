package api.arduinothermohygrometer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(@NotBlank String apiKey,
                                 @NotBlank String apiHeader,
                                 @NotBlank String apiRole,
                                 @NotBlank String apiSchemeName
) {
}
