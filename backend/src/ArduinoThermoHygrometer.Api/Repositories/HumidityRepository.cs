using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermoHygrometer.Api.Repositories;

public class HumidityRepository : IHumidityRepository
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public HumidityRepository(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public async Task<Humidity?> GetHumidityByIdAsync(Guid id)
    {
        Humidity? humidity = await _dbContext.Humidities
            .FindAsync(id);

        return humidity;
    }

    public async Task<Humidity?> GetHumidityByDateAndTimeAsync(DateTimeOffset registeredAt)
    {
        Humidity? humidity = await _dbContext.Humidities
            .FirstOrDefaultAsync(h => h.RegisteredAt == registeredAt);

        return humidity;
    }

    public async Task<IEnumerable<Humidity?>> GetAllHumiditiesWithinTimestampRangeAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp)
    {
        IEnumerable<Humidity?> humidities = await _dbContext.Humidities
            .Where(h => h.RegisteredAt >= startTimestamp && h.RegisteredAt <= endTimestamp)
            .ToListAsync();

        return humidities;
    }

    public async Task<Humidity?> AddHumidityAsync(Humidity humidity)
    {
        await _dbContext.Humidities.AddAsync(humidity);

        return humidity;
    }

    public Humidity? RemoveHumidity(Humidity humidity)
    {
        _dbContext.Humidities.Remove(humidity);

        return humidity;
    }

    public async Task SaveChangesAsync()
    {
        await _dbContext.SaveChangesAsync();
    }
}
