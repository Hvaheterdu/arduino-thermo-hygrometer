using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;
using Microsoft.EntityFrameworkCore;

namespace ArduinoThermoHygrometer.Api.Repositories;

public class BatteryRepository : IBatteryRepository
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public BatteryRepository(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public async Task<Battery?> GetBatteryByIdAsync(Guid id)
    {
        Battery? battery = await _dbContext.Batteries
            .FindAsync(id);

        return battery;
    }

    public async Task<Battery?> GetBatteryByTimestampAsync(DateTimeOffset timestamp)
    {
        Battery? battery = await _dbContext.Batteries
            .FirstOrDefaultAsync(b => b.RegisteredAt == timestamp);

        return battery;
    }

    public async Task<IEnumerable<Battery>> GetBatteriesByDateAsync(DateTimeOffset dateTimeOffset)
    {
        IEnumerable<Battery> batteries = await _dbContext.Batteries
            .Where(b => b.RegisteredAt.Date == dateTimeOffset.Date)
            .ToListAsync();

        return batteries;
    }

    public async Task CreateBatteryAsync(Battery battery) => await _dbContext.Batteries.AddAsync(battery);

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

    public async Task<Battery?> DeleteBatteryByTimestampAsync(DateTimeOffset timestamp)
    {
        Battery? battery = await _dbContext.Batteries.FirstOrDefaultAsync(b => b.RegisteredAt == timestamp);

        if (battery == null)
        {
            return null;
        }

        _dbContext.Remove(battery);

        return battery;
    }

    public async Task SaveChangesAsync() => await _dbContext.SaveChangesAsync();
}
