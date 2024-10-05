using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermoHygrometer.Api.Repositories;

public class TemperatureRepository : ITemperatureRepository
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public TemperatureRepository(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public async Task<Temperature?> GetTemperatureByIdAsync(Guid id)
    {
        Temperature? temperature = await _dbContext.Temperatures
            .FindAsync(id);

        return temperature;
    }

    public async Task<Temperature?> GetTemperatureByTimestampAsync(DateTimeOffset registeredAt)
    {
        Temperature? temperature = await _dbContext.Temperatures
            .FirstOrDefaultAsync(t => t.RegisteredAt == registeredAt);

        return temperature;
    }

    public async Task<IEnumerable<Temperature>> GetTemperaturesByDateAsync(DateTimeOffset dateTimeOffset)
    {
        IEnumerable<Temperature> temperatures = await _dbContext.Temperatures
            .Where(t => t.RegisteredAt.Date == dateTimeOffset.Date)
            .ToListAsync();

        return temperatures;
    }

    public async Task<Temperature?> AddTemperatureAsync(Temperature temperature)
    {
        await _dbContext.Temperatures.AddAsync(temperature);

        return temperature;
    }

    public Temperature? RemoveTemperature(Temperature temperature)
    {
        _dbContext.Temperatures.Remove(temperature);

        return temperature;
    }

    public async Task SaveChangesAsync()
    {
        await _dbContext.SaveChangesAsync();
    }
}
