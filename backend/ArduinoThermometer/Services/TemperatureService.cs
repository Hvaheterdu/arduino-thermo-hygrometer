using ArduinoThermometer.API.Repositories;
using ArduinoThermometer.API.Validators;

namespace ArduinoThermometer.API.Services;

public class TemperatureService
{
    private readonly TemperatureRepository _temperatureRepository;
    private readonly TemperatureDtoValidator _temperatureDtoValidator;
    private readonly ILogger<TemperatureService> _logger;

    public TemperatureService(TemperatureRepository temperatureRepository, TemperatureDtoValidator temperatureDtoValidator, ILogger<TemperatureService> logger)
    {
        _temperatureRepository = temperatureRepository;
        _temperatureDtoValidator = temperatureDtoValidator;
        _logger = logger;
    }
}
