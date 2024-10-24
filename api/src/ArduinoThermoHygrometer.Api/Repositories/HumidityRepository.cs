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

    /// <summary>
    /// Retrieves a Humidity object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to retrieve.</param>
    /// <returns>Returns the <see cref="Humidity"/> object if found; otherwise, null.</returns>
    public async Task<Humidity?> GetHumidityByIdAsync(Guid id)
    {
        Humidity? humidity = await _dbContext.Humidities.FindAsync(id);

        return humidity;
    }

    /// <summary>
    /// Retrieves a Humidity object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to retrieve.</param>
    /// <returns>Returns the <see cref="Humidity"/> object if found; otherwise, null.</returns>
    public async Task<Humidity?> GetHumidityByTimestampAsync(DateTimeOffset timestamp)
    {
        Humidity? humidity = await _dbContext.Humidities
            .AsNoTracking()
            .FirstOrDefaultAsync(h => h.RegisteredAt == timestamp);

        return humidity;
    }

    /// <summary>
    /// Retrieves a list of all Humidity objects of a date.
    /// </summary>
    /// <param name="dateTimeOffset">The <see cref="DateTimeOffset"/> of the objects to retrieve.</param>
    /// <returns>Returns a list of <see cref="Humidity"/> objects if non-empty list; otherwise, null.</returns>
    public async Task<IEnumerable<Humidity>> GetHumiditiesByDateAsync(DateTimeOffset dateTimeOffset)
    {
        IEnumerable<Humidity> humidities = await _dbContext.Humidities
            .Where(h => h.RegisteredAt.Date == dateTimeOffset.Date)
            .AsNoTracking()
            .ToListAsync();

        return humidities;
    }

    /// <summary>
    /// Creates a Humidity object asynchronously.
    /// </summary>
    /// <param name="humidity">The <see cref="Humidity"/> object to create.</param>
    /// <returns>Returns the <see cref="Humidity"/> object if created; otherwise, null.</returns>
    public async Task CreateHumidityAsync(Humidity humidity) => await _dbContext.Humidities.AddAsync(humidity);

    /// <summary>
    /// Deletes a Humidity object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="Humidity"/> object if deleted; otherwise, null.</returns>
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

    /// <summary>
    /// Deletes a Humidity object by its id asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="Humidity"/> object if deleted; otherwise, null.</returns>
    public async Task<Humidity?> DeleteHumidityByTimestampAsync(DateTimeOffset timestamp)
    {
        Humidity? humidity = await _dbContext.Humidities
            .FirstOrDefaultAsync(h => h.RegisteredAt == timestamp);

        if (humidity == null)
        {
            return null;
        }

        _dbContext.Remove(humidity);

        return humidity;
    }

    /// <summary>
    /// Save all changes made in this context to the database.
    /// </summary>
    public async Task SaveChangesAsync() => await _dbContext.SaveChangesAsync();
}
