namespace ArduinoThermoHygrometer.Domain.Entities;

public class RateLimitOptions
{
    public const string RateLimit = "RateLimit";
    public int PermitLimit { get; set; } = 60;
    public int Window { get; set; } = 60;
}
