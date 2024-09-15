using ArduinoThermoHygrometer.Web.Services.Contracts;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;

namespace ArduinoThermoHygrometer.Web.Controllers;

[ApiController]
[ApiVersion(0.1)]
[Route("api/v{version:apiVersion}/[controller]")]
public class HumidityController : ControllerBase
{
    private readonly IHumidityService _humidityService;

    public HumidityController(IHumidityService humidityService)
    {
        _humidityService = humidityService;
    }
}
