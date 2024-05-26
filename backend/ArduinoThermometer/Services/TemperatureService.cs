using ArduinoThermometer.API.DTOs;
using ArduinoThermometer.API.Repositories;
using FluentValidation;

namespace ArduinoThermometer.API.Services;

public class TemperatureService
{
    private readonly ITemperatureRepository _temperatureRepository;
    private readonly IValidator<TemperatureDto> _temperatureDtoValidator;
    private readonly ILogger<TemperatureService> _logger;

    public TemperatureService(ITemperatureRepository temperatureRepository, IValidator<TemperatureDto> temperatureDtoValidator, ILogger<TemperatureService> logger)
    {
        _temperatureRepository = temperatureRepository;
        _temperatureDtoValidator = temperatureDtoValidator;
        _logger = logger;
    }
}
