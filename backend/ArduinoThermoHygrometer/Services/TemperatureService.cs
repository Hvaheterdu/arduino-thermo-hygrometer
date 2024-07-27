using ArduinoThermoHygrometer.Web.DTOs;
using ArduinoThermoHygrometer.Web.Repositories.Contracts;
using ArduinoThermoHygrometer.Web.Services.Contracts;
using FluentValidation;

namespace ArduinoThermoHygrometer.Web.Services;

public class TemperatureService : ITemperatureService
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
