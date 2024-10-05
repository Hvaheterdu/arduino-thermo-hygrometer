using ArduinoThermoHygrometer.Api.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;

namespace ArduinoThermoHygrometer.Api.Controllers;

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

    /// <summary>
    /// Retrieves a battery object by its id.
    /// </summary>
    /// <param name="id">The id of the battery to retrieve.</param>
    /// <returns>Returns battery or NotFound.</returns>
    /// <response code="200">Returns <c>Battery</c>.</response>
    /// <response code="404">Returns a <c>NotFound</c> if the battery is not found or if the input is invalid.</response>
    [HttpGet("{id:Guid}")]
    [Produces("application/json")]
    [ProducesResponseType(typeof(BatteryDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<BatteryDto>> GetByIdAsync(Guid id)
    {
        BatteryDto? batteryDto = await _batteryService.GetBatteryDtoByIdAsync(id);

        if (batteryDto == null)
        {
            return NotFound(batteryDto);
        }

        return Ok(batteryDto);
    }

    /// <summary>
    /// Retrieves a battery object by its registration timestamp.
    /// </summary>
    /// <param name="registeredAt">The timestamp of the battery to retrieve.</param>
    /// <returns>Returns battery or NotFound</returns>
    /// <response code="200">Returns <c>Battery</c>.</response>
    /// <response code="404">Returns a <c>NotFound</c> if the battery is not found or if the input is invalid.</response>
    [HttpGet("{registeredAt:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(typeof(BatteryDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<BatteryDto>> GetByTimestampAsync(DateTimeOffset registeredAt)
    {
        BatteryDto? batteryDto = await _batteryService.GetBatteryDtoByTimestampAsync(registeredAt);

        if (batteryDto == null)
        {
            return NotFound(batteryDto);
        }

        return Ok(batteryDto);
    }
}
