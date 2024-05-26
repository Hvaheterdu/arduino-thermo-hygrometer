using ArduinoThermometer.API.Models;
using ArduinoThermometer.API.Repositories;
using FluentValidation;

namespace ArduinoThermometer.API.Services;

public class TemperatureService
{
    private readonly ITemperatureRepository _temperatureRepository;
    private readonly IValidator<Temperature> _temperatureDtoValidator;
    private readonly ILogger<TemperatureService> _logger;

    public TemperatureService(ITemperatureRepository temperatureRepository, IValidator<Temperature> temperatureDtoValidator, ILogger<TemperatureService> logger)
    {
        _temperatureRepository = temperatureRepository;
        _temperatureDtoValidator = temperatureDtoValidator;
        _logger = logger;
    }
}
