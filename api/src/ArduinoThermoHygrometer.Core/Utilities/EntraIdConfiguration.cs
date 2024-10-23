namespace ArduinoThermoHygrometer.Core.Utilities;

public class EntraIdConfiguration
{
    public const string EntraId = "EntraId";

    public string? Domain { get; set; }

    public Uri? AuthorizationUrl { get; set; }

    public Uri? TokenUrl { get; set; }

    public string? Instance { get; set; }

    public string? TenantId { get; set; }

    public string? ClientId { get; set; }

    public string? CallBackPath { get; set; }

    public string? SignedOutCallbackPath { get; set; }

    public string? ClientSecret { get; set; }
}
