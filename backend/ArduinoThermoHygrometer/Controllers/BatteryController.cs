using ArduinoThermoHygrometer.Services;
using Microsoft.AspNetCore.Mvc;

namespace ArduinoThermoHygrometer.Controllers;

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
