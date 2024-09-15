using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;
using ArduinoThermoHygrometer.Web.Repositories.Contracts;

namespace ArduinoThermoHygrometer.Web.Repositories;

public class TemperatureRepository : ITemperatureRepository
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public TemperatureRepository(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public Task<IEnumerable<Temperature>> GetAllTemperaturesAsync()
    {
        throw new NotImplementedException();
    }

    public Task<Temperature> GetTemperatureByIdAsync(Guid id)
    {
        throw new NotImplementedException();
    }

    public Task<Temperature> GetTemperatureByDateAndTimeAsync(DateTimeOffset dateTimeOffset)
    {
        throw new NotImplementedException();
    }

    public Task<Temperature> AddTemperatureAsync(Temperature battery)
    {
        throw new NotImplementedException();
    }

    public Task<Temperature> UpdateTemperatureAsync(Temperature battery)
    {
        throw new NotImplementedException();
    }

    public Task<Temperature> RemoveTemperatureByDateAndTimeAsync(DateTimeOffset dateTimeOffset)
    {
        throw new NotImplementedException();
    }

    public Task<Temperature> RemoveTemperatureByIdAsync(Guid id)
    {
        throw new NotImplementedException();
    }

    public Task<Temperature> SaveChangesAsync()
    {
        throw new NotImplementedException();
    }
}
