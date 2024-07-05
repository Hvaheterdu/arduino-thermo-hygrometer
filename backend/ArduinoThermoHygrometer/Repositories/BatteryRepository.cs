using ArduinoThermoHygrometer.Infrastructure.Data;

namespace ArduinoThermoHygrometer.Web.Repositories;

public class BatteryRepository : IBatteryRepository
{
    private readonly ArduinoThermoHygrometerDbContext _dbContext;

    public BatteryRepository(ArduinoThermoHygrometerDbContext dbContext)
    {
        _dbContext = dbContext;
    }
}
