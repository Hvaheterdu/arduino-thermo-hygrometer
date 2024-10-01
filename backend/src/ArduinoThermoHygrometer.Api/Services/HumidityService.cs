using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;

namespace ArduinoThermoHygrometer.Api.Services;

public class HumidityService : IHumidityService
{
    private readonly IHumidityRepository _humidityRepository;
    private readonly ILogger<HumidityService> _logger;

    public HumidityService(IHumidityRepository humidityRepository, ILogger<HumidityService> logger)
    {
        _humidityRepository = humidityRepository;
        _logger = logger;
    }

    public void Dummy()
    {
        throw new NotImplementedException();
    }
}
