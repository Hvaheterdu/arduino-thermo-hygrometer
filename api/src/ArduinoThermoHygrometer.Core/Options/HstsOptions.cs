namespace ArduinoThermoHygrometer.Core.Options;

public sealed class HstsOptions
{
    public const string SectionName = "HSTS";

    public bool IncludeSubDomains { get; set; }

    public int MaxAge { get; set; }
}
