using ArduinoThermoHygrometer.Web.DTOs;
using ArduinoThermoHygrometer.Web.Repositories.Contracts;
using ArduinoThermoHygrometer.Web.Services.Contracts;
using FluentValidation;

namespace ArduinoThermoHygrometer.Web.Services;

public class HumidityService : IHumidityService
{
    private readonly IHumidityRepository _humidityRepository;
    private readonly IValidator<HumidityDto> _humidityDtovValidator;
    private readonly ILogger<HumidityService> _logger;

    public HumidityService(IHumidityRepository humidityRepository, IValidator<HumidityDto> humidityDtovValidator, ILogger<HumidityService> logger)
    {
        _humidityRepository = humidityRepository;
        _humidityDtovValidator = humidityDtovValidator;
        _logger = logger;
    }
}
