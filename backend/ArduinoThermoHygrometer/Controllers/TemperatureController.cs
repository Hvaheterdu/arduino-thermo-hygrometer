using ArduinoThermoHygrometer.Web.Services;
using Microsoft.AspNetCore.Mvc;

namespace ArduinoThermoHygrometer.Web.Controllers;

[ApiController]
[Route("api/[controller]")]
public class TemperatureController : ControllerBase
{
    private readonly TemperatureService _temperatureService;

    public TemperatureController(TemperatureService temperatureService)
    {
        _temperatureService = temperatureService;
    }
}
