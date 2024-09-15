using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;
using ArduinoThermoHygrometer.Web.Repositories.Contracts;

namespace ArduinoThermoHygrometer.Web.Repositories;

public class BatteryRepository : IBatteryRepository
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public BatteryRepository(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public Task<IEnumerable<Battery>> GetAllBatteriesAsync()
    {
        throw new NotImplementedException();
    }

    public Task<Battery> GetBatteryByIdAsync(Guid id)
    {
        throw new NotImplementedException();
    }

    public Task<Battery> GetBatteryByDateAndTimeAsync(DateTimeOffset dateTimeOffset)
    {
        throw new NotImplementedException();
    }

    public Task<Battery> AddBatteryAsync(Battery battery)
    {
        throw new NotImplementedException();
    }

    public Task<Battery> UpdateBatteryAsync(Battery battery)
    {
        throw new NotImplementedException();
    }

    public Task<Battery> RemoveBatteryByDateAndTimeAsync(DateTimeOffset dateTimeOffset)
    {
        throw new NotImplementedException();
    }

    public Task<Battery> RemoveBatteryByIdAsync(Guid id)
    {
        throw new NotImplementedException();
    }

    public Task<Battery> SaveChangesAsync()
    {
        throw new NotImplementedException();
    }
}
