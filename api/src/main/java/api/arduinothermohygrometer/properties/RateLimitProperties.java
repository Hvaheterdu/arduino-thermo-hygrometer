package api.arduinothermohygrometer.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@ConfigurationProperties(prefix = "rate-limit")
@Validated
public record RateLimitProperties(
    @Positive long apiCapacity,
    @NotNull Duration apiRefillPeriod,
    @Positive long authenticationCapacity,
    @NotNull Duration authenticationRefillPeriod,
    @Positive long maximumBuckets,
    @NotNull Duration bucketExpiration) {}
