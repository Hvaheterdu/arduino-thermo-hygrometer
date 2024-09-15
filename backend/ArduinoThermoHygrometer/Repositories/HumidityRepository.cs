using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;
using ArduinoThermoHygrometer.Web.Repositories.Contracts;

namespace ArduinoThermoHygrometer.Web.Repositories;

public class HumidityRepository : IHumidityRepository
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public HumidityRepository(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public Task<IEnumerable<Humidity>> GetAllHumiditiesAsync()
    {
        throw new NotImplementedException();
    }
    public Task<Humidity> GetHumidityByIdAsync(Guid id)
    {
        throw new NotImplementedException();
    }

    public Task<Humidity> GetHumidityByDateAndTimeAsync(DateTimeOffset dateTimeOffset)
    {
        throw new NotImplementedException();
    }

    public Task<Humidity> AddHumidityAsync(Humidity battery)
    {
        throw new NotImplementedException();
    }

    public Task<Humidity> UpdateHumidityAsync(Humidity battery)
    {
        throw new NotImplementedException();
    }

    public Task<Humidity> RemoveHumidityByDateAndTimeAsync(DateTimeOffset dateTimeOffset)
    {
        throw new NotImplementedException();
    }

    public Task<Humidity> RemoveHumidityByIdAsync(Guid id)
    {
        throw new NotImplementedException();
    }

    public Task<Humidity> SaveChangesAsync()
    {
        throw new NotImplementedException();
    }
}
