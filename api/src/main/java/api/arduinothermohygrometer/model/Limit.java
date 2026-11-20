package api.arduinothermohygrometer.model;

import java.time.Duration;

public record Limit(long capacity, Duration refillPeriod) {}
