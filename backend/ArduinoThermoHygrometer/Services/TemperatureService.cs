using ArduinoThermoHygrometer.DTOs;
using ArduinoThermoHygrometer.Repositories;
using FluentValidation;

namespace ArduinoThermoHygrometer.Services;

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
