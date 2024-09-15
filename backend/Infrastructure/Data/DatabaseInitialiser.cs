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
                new Battery(DateTimeOffset.Now.AddDays(-2).AddHours(12), 90),
                new Battery(DateTimeOffset.Now.AddDays(-2).AddHours(13), 90),
                new Battery(DateTimeOffset.Now.AddDays(-2).AddHours(14), 89),
                new Battery(DateTimeOffset.Now.AddDays(-2).AddHours(15), 89),
                new Battery(DateTimeOffset.Now.AddDays(-2).AddHours(16), 89),
                new Battery(DateTimeOffset.Now.AddDays(-2).AddHours(17), 88),
            };

            dbContext.AddRange(batteries);
            dbContext.SaveChanges();
        }

        if (!dbContext.Temperatures.Any())
        {
            List<Temperature> temperatures = new()
            {
                new Temperature(DateTimeOffset.Now.AddDays(-2).AddHours(12), 14),
                new Temperature(DateTimeOffset.Now.AddDays(-2).AddHours(13), 15),
                new Temperature(DateTimeOffset.Now.AddDays(-2).AddHours(14), 16),
                new Temperature(DateTimeOffset.Now.AddDays(-2).AddHours(15), 16),
                new Temperature(DateTimeOffset.Now.AddDays(-2).AddHours(16), 16),
                new Temperature(DateTimeOffset.Now.AddDays(-2).AddHours(17), 16),
            };

            dbContext.AddRange(temperatures);
            dbContext.SaveChanges();
        }

        if (!dbContext.Humidities.Any())
        {
            List<Humidity> humidities = new()
            {
                new Humidity(DateTimeOffset.Now.AddDays(-2).AddHours(12), 83),
                new Humidity(DateTimeOffset.Now.AddDays(-2).AddHours(13), 86),
                new Humidity(DateTimeOffset.Now.AddDays(-2).AddHours(14), 88),
                new Humidity(DateTimeOffset.Now.AddDays(-2).AddHours(15), 89),
                new Humidity(DateTimeOffset.Now.AddDays(-2).AddHours(16), 87),
                new Humidity(DateTimeOffset.Now.AddDays(-2).AddHours(17), 87),
            };

            dbContext.AddRange(humidities);
            dbContext.SaveChanges();
        }
    }
}
