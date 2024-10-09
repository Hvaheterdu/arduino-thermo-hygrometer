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

    public async Task<Humidity?> GetHumidityByTimestampAsync(DateTimeOffset timestamp)
    {
        Humidity? humidity = await _dbContext.Humidities
            .FirstOrDefaultAsync(h => h.RegisteredAt == timestamp);

        return humidity;
    }

    public async Task<IEnumerable<Humidity>> GetHumiditiesByDateAsync(DateTimeOffset dateTimeOffset)
    {
        IEnumerable<Humidity> humidities = await _dbContext.Humidities
            .Where(h => h.RegisteredAt.Date == dateTimeOffset.Date)
            .ToListAsync();

        return humidities;
    }

    public async Task CreateHumidityAsync(Humidity humidity) => await _dbContext.Humidities.AddAsync(humidity);

    public async Task<Humidity?> DeleteHumidityByIdAsync(Guid id)
    {
        Humidity? humidity = await _dbContext.Humidities.FindAsync(id);

        if (humidity == null)
        {
            return null;
        }

        _dbContext.Remove(humidity);

        return humidity;
    }

    public async Task<Humidity?> DeleteHumidityByTimestampAsync(DateTimeOffset timestamp)
    {
        Humidity? humidity = await _dbContext.Humidities.FirstOrDefaultAsync(h => h.RegisteredAt == timestamp);

        if (humidity == null)
        {
            return null;
        }

        _dbContext.Remove(humidity);

        return humidity;
    }

    public async Task SaveChangesAsync() => await _dbContext.SaveChangesAsync();
}
