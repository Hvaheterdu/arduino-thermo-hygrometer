namespace ArduinoThermoHygrometer.Infrastructure.Options;

public sealed class DatabaseOptions
{
    public const string SectionName = "Database";

    public bool RunMigrationsOnStartup { get; set; }

    public int RetryOnFailureAttempts { get; set; }
}
