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
    /// <response code="404">Returns <c>NotFound</c> if the battery is not found or if the input is invalid.</response>
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
    /// <param name="timestamp">The timestamp of the battery to retrieve.</param>
    /// <returns>Returns battery or NotFound</returns>
    /// <response code="200">Returns <c>Battery</c>.</response>
    /// <response code="404">Returns <c>NotFound</c> if the battery is not found or if the input is invalid.</response>
    [HttpGet("get-by-timestamp/{timestamp:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(typeof(BatteryDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<BatteryDto>> GetByTimestampAsync(DateTimeOffset timestamp)
    {
        BatteryDto? batteryDto = await _batteryService.GetBatteryDtoByTimestampAsync(timestamp);

        if (batteryDto == null)
        {
            return NotFound(batteryDto);
        }

        return Ok(batteryDto);
    }

    /// <summary>
    /// Retrieves a list of battery objects by its date.
    /// </summary>
    /// <param name="date">The date of the battery to retrieve.</param>
    /// <returns>Returns battery or NotFound</returns>
    /// <response code="200">Returns a list of <c>Batteries</c>.</response>
    /// <response code="404">Returns <c>NotFound</c> if the list of batteries is empty or if the input is invalid.</response>
    [HttpGet("get-by-date/{date:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(typeof(BatteryDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<IEnumerable<BatteryDto>>> GetByDateAsync(DateTimeOffset date)
    {
        IEnumerable<BatteryDto>? batteryDto = await _batteryService.GetBatteryDtosByDateAsync(date);

        if (batteryDto.IsNullOrEmpty())
        {
            return NotFound(batteryDto);
        }

        return Ok(batteryDto);
    }

    /// <summary>
    /// Creates a battery object.
    /// </summary>
    /// <param name="batteryDto">The battery to create.</param>
    /// <returns>Returns Created or BadRequest</returns>
    /// <response code="201">Returns <c>Created</c>.</response>
    /// <response code="400">Returns <c>BadRequest</c> if the input is invalid.</response>
    [HttpPost("add")]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status201Created)]
    [ProducesResponseType(StatusCodes.Status400BadRequest)]
    public async Task<ActionResult<BatteryDto>> CreateAsync(BatteryDto batteryDto)
    {
        BatteryDto batteryDtoCreated = await _batteryService.CreateBatteryDtoAsync(batteryDto);

        if (!ModelState.IsValid)
        {
            return BadRequest(batteryDto);
        }

        Uri uri = new($"{Request.Scheme}://{Request.Host}{Request.Path}");

        return Created(uri, batteryDtoCreated);
    }

    /// <summary>
    /// Deletes a battery object by its id.
    /// </summary>
    /// <param name="id">The id of the battery to delete.</param>
    /// <returns>Returns NoContent or NotFound.</returns>
    /// <response code="204">Returns <c>NoContent</c>.</response>
    /// <response code="404">Returns <c>NotFound</c> if the battery is not found or if the input is invalid.</response>
    [HttpDelete("delete/{id:guid}")]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status204NoContent)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<BatteryDto>> DeleteByIdAsync(Guid id)
    {
        BatteryDto? batteryDto = await _batteryService.DeleteBatteryDtoByIdAsync(id);

        if (batteryDto == null)
        {
            return NotFound(batteryDto);
        }

        return NoContent();
    }

    /// <summary>
    /// Deletes a battery object by its registration timestamp.
    /// </summary>
    /// <param name="timestamp">The timestamp of the battery to delete.</param>
    /// <returns>Returns NoContent or NotFound</returns>
    /// <response code="204">Returns <c>NoContent</c>.</response>
    /// <response code="404">Returns <c>NotFound</c> if the battery is not found or if the input is invalid.</response>
    [HttpDelete("delete/{timestamp:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status204NoContent)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<BatteryDto>> DeleteByTimestampAsync(DateTimeOffset timestamp)
    {
        BatteryDto? batteryDto = await _batteryService.DeleteBatteryDtoByTimestampAsync(timestamp);

        if (batteryDto == null)
        {
            return NotFound(batteryDto);
        }

        return NoContent();
    }
}
