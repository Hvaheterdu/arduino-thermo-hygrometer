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

    /// <summary>
    /// Retrieves a Temperature object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to retrieve.</param>
    /// <returns>Returns the <see cref="Temperature"/> object if found; otherwise, null.</returns>
    public async Task<Temperature?> GetTemperatureByIdAsync(Guid id)
    {
        Temperature? temperature = await _dbContext.Temperatures.FindAsync(id);

        return temperature;
    }

    /// <summary>
    /// Retrieves a Temperature object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to retrieve.</param>
    /// <returns>Returns the <see cref="Temperature"/> object if found; otherwise, null.</returns>
    public async Task<Temperature?> GetTemperatureByTimestampAsync(DateTimeOffset timestamp)
    {
        Temperature? temperature = await _dbContext.Temperatures
            .AsNoTracking()
            .FirstOrDefaultAsync(t => t.RegisteredAt == timestamp);

        return temperature;
    }

    /// <summary>
    /// Retrieves a list of all Temperature objects of a date.
    /// </summary>
    /// <param name="dateTimeOffset">The <see cref="DateTimeOffset"/> of the objects to retrieve.</param>
    /// <returns>Returns a list of <see cref="Temperature"/> objects if non-empty list; otherwise, null.</returns>
    public async Task<IEnumerable<Temperature>> GetTemperaturesByDateAsync(DateTimeOffset dateTimeOffset)
    {
        IEnumerable<Temperature> temperatures = await _dbContext.Temperatures
            .Where(t => t.RegisteredAt.Date == dateTimeOffset.Date)
            .AsNoTracking()
            .ToListAsync();

        return temperatures;
    }

    /// <summary>
    /// Creates a Temperature object asynchronously.
    /// </summary>
    /// <param name="temperature">The <see cref="Temperature"/> object to create.</param>
    /// <returns>Returns the <see cref="Temperature"/> object if created; otherwise, null.</returns>
    public async Task CreateTemperatureAsync(Temperature temperature) => await _dbContext.Temperatures.AddAsync(temperature);

    /// <summary>
    /// Deletes a Temperature object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="Temperature"/> object if deleted; otherwise, null.</returns>
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

    /// <summary>
    /// Deletes a Temperature object by its id asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="Temperature"/> object if deleted; otherwise, null.</returns>
    public async Task<Temperature?> DeleteTemperatureByTimestampAsync(DateTimeOffset timestamp)
    {
        Temperature? temperature = await _dbContext.Temperatures
            .FirstOrDefaultAsync(t => t.RegisteredAt == timestamp);

        if (temperature == null)
        {
            return null;
        }

        _dbContext.Remove(temperature);

        return temperature;
    }

    /// <summary>
    /// Save all changes made in this context to the database.
    /// </summary>
    public async Task SaveChangesAsync() => await _dbContext.SaveChangesAsync();
}
