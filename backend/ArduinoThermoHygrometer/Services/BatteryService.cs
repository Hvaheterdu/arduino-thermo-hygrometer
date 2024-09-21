using ArduinoThermoHygrometer.Api.DTOs;
using ArduinoThermoHygrometer.Api.Repositories.Contracts;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using FluentValidation;

namespace ArduinoThermoHygrometer.Api.Services;

public class BatteryService : IBatteryService
{
    private readonly IBatteryRepository _batteryRepository;
    private readonly IValidator<BatteryDto> _batteryDtoValidator;
    private readonly ILogger<BatteryService> _logger;

    public BatteryService(IBatteryRepository batteryRepository, IValidator<BatteryDto> batteryDtoValidator, ILogger<BatteryService> logger)
    {
        _batteryRepository = batteryRepository;
        _batteryDtoValidator = batteryDtoValidator;
        _logger = logger;
    }

    public void Dummy()
    {
        throw new NotImplementedException();
    }
}
