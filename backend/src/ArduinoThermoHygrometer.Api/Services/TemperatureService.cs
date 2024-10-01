using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;

namespace ArduinoThermoHygrometer.Api.Services;

public class TemperatureService : ITemperatureService
{
    private readonly ITemperatureRepository _temperatureRepository;
    private readonly ILogger<TemperatureService> _logger;

    public TemperatureService(ITemperatureRepository temperatureRepository, ILogger<TemperatureService> logger)
    {
        _temperatureRepository = temperatureRepository;
        _logger = logger;
    }

    public void Dummy()
    {
        throw new NotImplementedException();
    }
}
