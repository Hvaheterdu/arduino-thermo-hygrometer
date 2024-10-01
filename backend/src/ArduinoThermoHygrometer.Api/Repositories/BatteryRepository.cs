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

    public async Task<Battery?> GetBatteryByDateAndTimeAsync(DateTimeOffset registeredAt)
    {
        Battery? battery = await _dbContext.Batteries
            .FirstOrDefaultAsync(b => b.RegisteredAt == registeredAt);

        return battery;
    }

    public async Task<IEnumerable<Battery?>> GetAllBatteriesWithinTimestampRangeAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp)
    {
        IEnumerable<Battery?> batteries = await _dbContext.Batteries
            .Where(b => b.RegisteredAt >= startTimestamp && b.RegisteredAt <= endTimestamp)
            .ToListAsync();

        return batteries;
    }

    public async Task<Battery?> AddBatteryAsync(Battery battery)
    {
        await _dbContext.Batteries.AddAsync(battery);

        return battery;
    }

    public Battery? RemoveBattery(Battery battery)
    {
        _dbContext.Batteries.Remove(battery);

        return battery;
    }

    public async Task SaveChangesAsync()
    {
        await _dbContext.SaveChangesAsync();
    }
}
