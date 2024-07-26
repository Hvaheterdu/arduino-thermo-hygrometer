namespace ArduinoThermoHygrometer.Infrastructure.Data;
public static class DatabaseInitialiser
{
    public static void SeedDatabase(ArduinoThermoHygrometerDbContext dbContext)
    {
        if (!dbContext.Batteries.Any())
        {

        }

        if (!dbContext.Temperatures.Any())
        {

        }
    }
}
