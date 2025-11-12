using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;
using Bogus;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermoHygrometer.Test.Helpers;

public class IntegrationTestDataSeeder
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public IntegrationTestDataSeeder(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public async Task SeedAsync()
    {
        if (await _dbContext.Temperatures.AnyAsync()
            || await _dbContext.Humidities.AnyAsync()
            || await _dbContext.Batteries.AnyAsync())
        {
            return;
        }

        await GenerateData();
    }

    private async Task GenerateData(int total = 50)
    {
        DateTimeOffset now = DateTimeOffset.UtcNow;
        DateTimeOffset start = now.AddDays(-30);

        Faker<Battery> batteryFaker = new Faker<Battery>()
            .RuleFor(b => b.Id, _ => Guid.NewGuid())
            .RuleFor(b => b.RegisteredAt, f => f.Date.Between(start.DateTime, now.DateTime))
            .RuleFor(b => b.BatteryStatus, f => f.Random.Int(10, 100));

        List<Battery> batteries = batteryFaker.Generate(total);
        await _dbContext.Batteries.AddRangeAsync(batteries);

        Faker<Humidity> humidityFaker = new Faker<Humidity>()
            .RuleFor(h => h.Id, _ => Guid.NewGuid())
            .RuleFor(h => h.RegisteredAt, f => f.Date.Between(start.DateTime, now.DateTime))
            .RuleFor(h => h.AirHumidity, f => Math.Round(f.Random.Decimal(20, 90), 2));

        List<Humidity> humidities = humidityFaker.Generate(total);
        await _dbContext.Humidities.AddRangeAsync(humidities);

        Faker<Temperature> temperatureFaker = new Faker<Temperature>()
            .RuleFor(t => t.Id, _ => Guid.NewGuid())
            .RuleFor(t => t.RegisteredAt, f => f.Date.Between(start.DateTime, now.DateTime))
            .RuleFor(t => t.Temp, f => Math.Round(f.Random.Decimal(-35, 35), 2));

        List<Temperature> temperatures = temperatureFaker.Generate(total);
        await _dbContext.Temperatures.AddRangeAsync(temperatures);

        await _dbContext.SaveChangesAsync();
    }
}
