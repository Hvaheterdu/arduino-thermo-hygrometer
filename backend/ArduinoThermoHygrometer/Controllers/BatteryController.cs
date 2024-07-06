using ArduinoThermoHygrometer.Web.Services;
using Microsoft.AspNetCore.Mvc;

namespace ArduinoThermoHygrometer.Web.Controllers;

[ApiController]
[Route("api/[controller]")]
public class BatteryController : ControllerBase
{
    private readonly BatteryService _batteryService;

    public BatteryController(BatteryService batteryService)
    {
        _batteryService = batteryService;
    }
}
