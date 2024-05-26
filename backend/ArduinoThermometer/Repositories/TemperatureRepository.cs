using ArduinoThermometer.API.Data;

namespace ArduinoThermometer.API.Repositories;

public class TemperatureRepository : ITemperatureRepository
{
    private readonly ArduinoThermometerDbContext _context;

    public TemperatureRepository(ArduinoThermometerDbContext context)
    {
        _context = context;
    }
}
