namespace ArduinoThermoHygrometer.Infrastructure.Options;

public sealed class ConnectionStringsOptions
{
    public const string SectionName = "ConnectionStrings";

    public string ArduinoThermoHygrometerLocal { get; set; } = string.Empty;
}
