using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Infrastructure.Data;
using ArduinoThermoHygrometer.Web.DTOs;
using ArduinoThermoHygrometer.Web.Repositories.Contracts;

namespace ArduinoThermoHygrometer.Web.Repositories;

public class TemperatureRepository : ITemperatureRepository
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public TemperatureRepository(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }

    public Task<IEnumerable<Temperature?>> GetAllTemperaturesAsync()
    {
        throw new NotImplementedException();
    }

    public Task<Temperature?> GetTemperatureByIdAsync(Guid id)
    {
        throw new NotImplementedException();
    }

    public Task<Temperature?> GetTemperatureByDateAndTimeAsync(DateTimeOffset registeredAt)
    {
        throw new NotImplementedException();
    }

    public Task<Temperature?> AddTemperatureAsync(Temperature temperature)
    {
        throw new NotImplementedException();
    }

    public Temperature? UpdateTemperatureAsync(Temperature temperature)
    {
        throw new NotImplementedException();
    }

    public Temperature? RemoveTemperature(Temperature temperature)
    {
        throw new NotImplementedException();
    }

    public Task SaveChangesAsync()
    {
        throw new NotImplementedException();
    }
}
