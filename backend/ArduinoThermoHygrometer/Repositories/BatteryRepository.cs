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

    public async Task<IEnumerable<Battery?>> GetAllBatteriesAsync()
    {
        IEnumerable<Battery?> batteries = await _dbContext.Batteries.ToListAsync();

        return batteries;
    }

    public async Task<Battery?> GetBatteryByIdAsync(Guid id)
    {
        Battery? battery = await _dbContext.Batteries.FindAsync(id);

        return battery;
    }

    public async Task<Battery?> GetBatteryByDateAndTimeAsync(DateTimeOffset registeredAt)
    {
        Battery? battery = await _dbContext.Batteries.FirstOrDefaultAsync(b => b.RegisteredAt == registeredAt);

        return battery;
    }

    public async Task<Battery?> AddBatteryAsync(Battery battery)
    {
        await _dbContext.AddAsync(battery);

        return battery;
    }

    public Battery? UpdateBattery(Battery battery)
    {
        _dbContext.Batteries.Update(battery);

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
