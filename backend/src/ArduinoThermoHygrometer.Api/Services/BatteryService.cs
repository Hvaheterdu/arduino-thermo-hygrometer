using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;

namespace ArduinoThermoHygrometer.Api.Services;

public class BatteryService : IBatteryService
{
    private readonly IBatteryRepository _batteryRepository;
    private readonly ILogger<BatteryService> _logger;

    public BatteryService(IBatteryRepository batteryRepository, ILogger<BatteryService> logger)
    {
        _batteryRepository = batteryRepository;
        _logger = logger;
    }

    public void Dummy()
    {
        throw new NotImplementedException();
    }
}
