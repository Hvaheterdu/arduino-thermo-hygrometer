using ArduinoThermoHygrometer.Web.DTOs;
using ArduinoThermoHygrometer.Web.Repositories;
using FluentValidation;

namespace ArduinoThermoHygrometer.Web.Services;

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
}
