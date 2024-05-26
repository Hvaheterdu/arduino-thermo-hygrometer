using ArduinoThermometer.API.Repositories;
using ArduinoThermometer.API.Services;
using ArduinoThermometer.API.Validators;
using Microsoft.AspNetCore.Mvc;

namespace ArduinoThermometer.API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class TemperatureController : ControllerBase
{
    private readonly TemperatureService _temperatureService;
    private readonly TemperatureRepository _temperatureRepository;
    private readonly TemperatureDtoValidator _temperatureDtoValidator;

    public TemperatureController(TemperatureService temperatureService, TemperatureRepository temperatureRepository, TemperatureDtoValidator temperatureDtoValidator)
    {
        _temperatureService = temperatureService;
        _temperatureRepository = temperatureRepository;
        _temperatureDtoValidator = temperatureDtoValidator;
    }
}
