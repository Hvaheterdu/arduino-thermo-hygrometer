using ArduinoThermometer.API.Data;

namespace ArduinoThermometer.API.Repositories;

public class BatteryRepository : IBatteryRepository
{
    private readonly ArduinoThermometerDbContext _context;

    public BatteryRepository(ArduinoThermometerDbContext context)
    {
        _context = context;
    }
}
