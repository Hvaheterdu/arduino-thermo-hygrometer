using ArduinoThermoHygrometer.Core.Repositories.Contracts;
using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermoHygrometer.Core.Repositories;

public class BatteryRepository : IBatteryRepository
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public BatteryRepository(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    /// <summary>
    /// Retrieves a Battery object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to retrieve.</param>
    /// <returns>Returns the <see cref="Battery"/> object if found; otherwise, null.</returns>
    public async Task<Battery?> GetBatteryByIdAsync(Guid id)
    {
        Battery? battery = await _dbContext.Batteries.FindAsync(id);

        return battery;
    }

    /// <summary>
    /// Retrieves a Battery object by its registered timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to retrieve.</param>
    /// <returns>Returns the <see cref="Battery"/> object if found; otherwise, null.</returns>
    public async Task<Battery?> GetBatteryByTimestampAsync(DateTimeOffset timestamp)
    {
        Battery? battery = await _dbContext.Batteries
            .AsNoTracking()
            .FirstOrDefaultAsync(b => b.RegisteredAt == timestamp);

        return battery;
    }

    /// <summary>
    /// Retrieves a list of all Battery objects of a date.
    /// </summary>
    /// <param name="dateTimeOffset">The <see cref="DateTimeOffset"/> of the objects to retrieve.</param>
    /// <returns>Returns a list of <see cref="Battery"/> objects if non-empty list; otherwise, null.</returns>
    public async Task<IEnumerable<Battery>> GetBatteriesByDateAsync(DateTimeOffset dateTimeOffset)
    {
        IEnumerable<Battery> batteries = await _dbContext.Batteries
            .Where(b => b.RegisteredAt.Date == dateTimeOffset.Date)
            .AsNoTracking()
            .ToListAsync();

        return batteries;
    }

    /// <summary>
    /// Creates a Battery object asynchronously.
    /// </summary>
    /// <param name="battery">The <see cref="Battery"/> object to create.</param>
    /// <returns>Returns the <see cref="Battery"/> object if created; otherwise, null.</returns>
    public async Task CreateBatteryAsync(Battery battery) => await _dbContext.Batteries.AddAsync(battery);

    /// <summary>
    /// Deletes a Battery object by its id asynchronously.
    /// </summary>
    /// <param name="id">The <see cref="Guid"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="Battery"/> object if deleted; otherwise, null.</returns>
    public async Task<Battery?> DeleteBatteryByIdAsync(Guid id)
    {
        Battery? battery = await _dbContext.Batteries.FindAsync(id);

        if (battery == null)
        {
            return null;
        }

        _dbContext.Remove(battery);

        return battery;
    }

    /// <summary>
    /// Deletes a Battery object by its timestamp asynchronously.
    /// </summary>
    /// <param name="timestamp">The <see cref="DateTimeOffset"/> of the object to delete.</param>
    /// <returns>Returns the <see cref="Battery"/> object if deleted; otherwise, null.</returns>
    public async Task<Battery?> DeleteBatteryByTimestampAsync(DateTimeOffset timestamp)
    {
        Battery? battery = await _dbContext.Batteries
            .FirstOrDefaultAsync(b => b.RegisteredAt == timestamp);

        if (battery == null)
        {
            return null;
        }

        _dbContext.Remove(battery);

        return battery;
    }

    /// <summary>
    /// Save all changes made in this context to the database.
    /// </summary>
    public async Task SaveChangesAsync() => await _dbContext.SaveChangesAsync();
}
