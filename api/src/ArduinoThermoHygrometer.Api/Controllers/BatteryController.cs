using ArduinoThermoHygrometer.Api.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;

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
    [HttpGet("get-by-timestamp/{registeredAt:datetime}")]
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

    /// <summary>
    /// Retrieves a list of battery objects by its date.
    /// </summary>
    /// <param name="dateTimeOffset">The date of the battery to retrieve.</param>
    /// <returns>Returns battery or NotFound</returns>
    /// <response code="200">Returns a list of <c>Batteries</c>.</response>
    /// <response code="404">Returns a <c>NotFound</c> if the list of batteries is empty or if the input is invalid.</response>
    [HttpGet("get-by-date/{dateTimeOffset:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(typeof(BatteryDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<IEnumerable<BatteryDto>>> GetByDateAsync(DateTimeOffset dateTimeOffset)
    {
        IEnumerable<BatteryDto>? batteryDto = await _batteryService.GetBatteryDtosByDateAsync(dateTimeOffset);

        if (batteryDto.IsNullOrEmpty())
        {
            return NotFound(batteryDto);
        }

        return Ok(batteryDto);
    }

    /// <summary>
    /// Adds a battery object.
    /// </summary>
    /// <param name="batteryDto">The battery to add.</param>
    /// <returns>Returns Created or BadRequest</returns>
    /// <response code="201">Returns a <c>Created</c>.</response>
    /// <response code="400">Returns a <c>BadRequest</c> if the input is invalid.</response>
    [HttpPost("add")]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status201Created)]
    [ProducesResponseType(StatusCodes.Status400BadRequest)]
    public async Task<ActionResult<BatteryDto>> AddAsync(BatteryDto batteryDto)
    {
        await _batteryService.AddBatteryDtoAsync(batteryDto);

        if (!ModelState.IsValid)
        {
            return BadRequest(batteryDto);
        }

        return Created();
    }

    /// <summary>
    /// Deletes a battery object by its id.
    /// </summary>
    /// <param name="id">The id of the battery to delete.</param>
    /// <returns>Returns NoContent or NotFound.</returns>
    /// <response code="204">Returns <c>NoContent</c>.</response>
    /// <response code="404">Returns a <c>NotFound</c> if the battery is not found or if the input is invalid.</response>
    [HttpDelete("delete/{id:guid}")]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status204NoContent)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<BatteryDto>> RemoveByIdAsync(Guid id)
    {
        BatteryDto? batteryDto = await _batteryService.RemoveBatteryByIdAsync(id);

        if (batteryDto == null)
        {
            return NotFound(batteryDto);
        }

        return NoContent();
    }

    /// <summary>
    /// Deletes a battery object by its registration timestamp.
    /// </summary>
    /// <param name="registeredAt">The timestamp of the battery to delete.</param>
    /// <returns>Returns NoContent or NotFound</returns>
    /// <response code="204">Returns <c>NoContent</c>.</response>
    /// <response code="404">Returns a <c>NotFound</c> if the battery is not found or if the input is invalid.</response>
    [HttpDelete("delete/{registeredAt:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status204NoContent)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<BatteryDto>> RemoveByTimestampAsync(DateTimeOffset registeredAt)
    {
        BatteryDto? batteryDto = await _batteryService.RemoveBatteryByTimestampAsync(registeredAt);

        if (batteryDto == null)
        {
            return NotFound(batteryDto);
        }

        return NoContent();
    }
}
