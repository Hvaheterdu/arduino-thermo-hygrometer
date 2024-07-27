using ArduinoThermoHygrometer.Web.Services.Contracts;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;

namespace ArduinoThermoHygrometer.Web.Controllers;

[ApiController]
[ApiVersion(0.1)]
[Route("api/v{version:apiVersion}/[controller]")]
public class BatteryController : ControllerBase
{
    private readonly IBatteryService _batteryService;

    public BatteryController(IBatteryService batteryService)
    {
        _batteryService = batteryService;
    }
}
