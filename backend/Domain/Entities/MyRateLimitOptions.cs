namespace ArduinoThermoHygrometer.Domain.Entities;

public class MyRateLimitOptions
{
    public const string MyRateLimit = "MyRateLimit";
    public int PermitLimit { get; set; } = 60;
    public int Window { get; set; } = 60;
}
