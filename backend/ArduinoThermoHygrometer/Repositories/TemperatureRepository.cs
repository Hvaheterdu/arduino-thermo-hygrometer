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
}
