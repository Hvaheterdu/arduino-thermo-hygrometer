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

    public async Task CreateTemperatureAsync(Temperature temperature) => await _dbContext.Temperatures.AddAsync(temperature);

    public async Task<Temperature?> DeleteTemperatureByIdAsync(Guid id)
    {
        Temperature? temperature = await _dbContext.Temperatures.FindAsync(id);

        if (temperature == null)
        {
            return null;
        }

        _dbContext.Remove(temperature);

        return temperature;
    }

    public async Task<Temperature?> DeleteTemperatureByTimestampAsync(DateTimeOffset timestamp)
    {
        Temperature? temperature = await _dbContext.Temperatures.FirstOrDefaultAsync(t => t.RegisteredAt == timestamp);

        if (temperature == null)
        {
            return null;
        }

        _dbContext.Remove(temperature);

        return temperature;
    }

    public async Task SaveChangesAsync() => await _dbContext.SaveChangesAsync();
}
