using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;

namespace ArduinoThermoHygrometer.Api.Repositories;

public class HumidityRepository : IHumidityRepository
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public HumidityRepository(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public Task<IEnumerable<Humidity?>> GetAllHumiditiesAsync()
    {
        throw new NotImplementedException();
    }

    public Task<Humidity?> GetHumidityByIdAsync(Guid id)
    {
        throw new NotImplementedException();
    }

    public Task<Humidity?> GetHumidityByDateAndTimeAsync(DateTimeOffset registeredAt)
    {
        throw new NotImplementedException();
    }

    public Task<Humidity?> AddHumidityAsync(Humidity humidity)
    {
        throw new NotImplementedException();
    }

    public Humidity? UpdateHumidityAsync(Humidity humidity)
    {
        throw new NotImplementedException();
    }

    public Humidity? RemoveHumidity(Humidity humidity)
    {
        throw new NotImplementedException();
    }

    public Task SaveChangesAsync()
    {
        throw new NotImplementedException();
    }
}
