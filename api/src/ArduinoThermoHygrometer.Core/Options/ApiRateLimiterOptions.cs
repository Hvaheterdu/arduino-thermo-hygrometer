namespace ArduinoThermoHygrometer.Core.Options;

public sealed class ApiRateLimiterOptions
{
    public const string SectionName = "RateLimit";

    public int PermitLimit { get; set; }

    public int Window { get; set; }
}
