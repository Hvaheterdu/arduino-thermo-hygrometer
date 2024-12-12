using ArduinoThermoHygrometer.Domain.Entities;

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
            List<Battery> batteries = new()
            {
                new Battery(90)
            };

            dbContext.AddRange(batteries);
            dbContext.SaveChanges();
        }

        if (!dbContext.Temperatures.Any())
        {
            List<Temperature> temperatures = new()
            {
                new Temperature(14)
            };

            dbContext.AddRange(temperatures);
            dbContext.SaveChanges();
        }

        if (!dbContext.Humidities.Any())
        {
            List<Humidity> humidities = new()
            {
                new Humidity(83)
            };

            dbContext.AddRange(humidities);
            dbContext.SaveChanges();
        }
    }
}
