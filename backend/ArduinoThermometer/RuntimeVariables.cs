namespace ArduinoThermometer.API;

public static class RuntimeVariables
{
    public static readonly string? CurrentEnvironment = Environment.GetEnvironmentVariable("ASPNETCORE_ENVIRONMENT");

    public static class Environments
    {
        public const string Development = "Development";
        public const string Test = "Test";
        public const string UnitTest = "UnitTest";
        public const string IntegrationTest = "IntegrationTest";
        public const string Production = "Production";
    }
}
