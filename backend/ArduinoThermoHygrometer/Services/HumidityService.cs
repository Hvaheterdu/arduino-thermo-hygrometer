using ArduinoThermoHygrometer.Api.DTOs;
using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using FluentValidation;

namespace ArduinoThermoHygrometer.Api.Services;

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

    public void Dummy()
    {
        throw new NotImplementedException();
    }
}
