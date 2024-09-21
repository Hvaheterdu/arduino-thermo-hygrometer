namespace ArduinoThermoHygrometer.Api;

public static class GlobalVariables
{
    public static readonly string? CurrentEnvironment = Environment.GetEnvironmentVariable("ASPNETCORE_ENVIRONMENT");

    public const string Development = "Development";

    public const string Test = "Test";

    public const string Staging = "Staging";

    public const string Production = "Production";

    public const string UnitTest = "UnitTest";

    public const string IntegrationTest = "IntegrationTest";
}
