namespace ArduinoThermoHygrometer.Infrastructure.Data;
public static class DatabaseInitialiser
{
    /// <summary>
    /// Seeds the database with initial data.
    /// </summary>
    /// <param name="dbContext">The database context to which the data will be added.</param>
    public static void SeedDatabase(ArduinoThermoHygrometerDbContext dbContext)
    {
        ArgumentNullException.ThrowIfNull(dbContext);

        if (!dbContext.Batteries.Any())
        {
            return;
        }
    }
}
