using ArduinoThermoHygrometer.Web.Services.Contracts;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;

namespace ArduinoThermoHygrometer.Web.Controllers;

[ApiController]
[ApiVersion(0.1)]
[Route("api/v{version:apiVersion}/[controller]")]
public class TemperatureController : ControllerBase
{
    private readonly ITemperatureService _temperatureService;

    public TemperatureController(ITemperatureService temperatureService)
    {
        _temperatureService = temperatureService;
    }
}
