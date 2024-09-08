namespace ArduinoThermoHygrometer.Web.Options;

public class ApiRateLimiterOptions
{
    public const string RateLimit = "RateLimit";

    public int PermitLimit { get; set; } = 60;

    public int Window { get; set; } = 60;
}
