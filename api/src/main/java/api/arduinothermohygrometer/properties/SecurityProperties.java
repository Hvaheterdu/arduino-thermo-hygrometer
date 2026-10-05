package api.arduinothermohygrometer.properties;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "security")
@Validated
public record SecurityProperties(
    @NotBlank String jwtBearerFormat,
    @NotBlank String jwtSecretBase64,
    @NotBlank String jwtIssuer,
    @NotBlank String jwtAudience,
    @NotNull Duration accessTokenTtl,
    @NotBlank String username,
    @NotBlank String passwordHash,
    @NotEmpty List<String> roles,
    @NotBlank String jwtSchemeName) {}
