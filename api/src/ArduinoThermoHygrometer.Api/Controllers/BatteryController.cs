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
    /// <param name="id">The id of the battery object to retrieve.</param>
    /// <returns>Returns battery object or NotFound.</returns>
    /// <response code="200">Returns <c>Battery</c> object.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid id or the battery object is not found.</response>
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
    /// <param name="timestamp">The timestamp of the battery object to retrieve.</param>
    /// <returns>Returns battery object or NotFound</returns>
    /// <response code="200">Returns <c>Battery</c> object.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid timestamp or the battery object is not found.</response>
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
    /// <param name="date">The date of the battery objects to retrieve.</param>
    /// <returns>Returns battery objects or NotFound</returns>
    /// <response code="200">Returns a list of <c>Battery</c> objects.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid date or list of battery objects is empty.</response>
    [HttpGet("get-by-date/{date:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(typeof(BatteryDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<IEnumerable<BatteryDto>>> GetByDateAsync(DateTimeOffset date)
    {
        IEnumerable<BatteryDto>? batteryDto = await _batteryService.GetBatteryDtosByDateAsync(date);

        if (batteryDto == null || !batteryDto.Any())
        {
            return NotFound(batteryDto);
        }

        return Ok(batteryDto);
    }

    /// <summary>
    /// Creates a battery object.
    /// </summary>
    /// <param name="batteryDto">The battery object to create.</param>
    /// <returns>Returns Created or BadRequest</returns>
    /// <response code="201">Returns <c>Created</c>.</response>
    /// <response code="400">Returns <c>BadRequest</c> if invalid batteryDto object.</response>
    [HttpPost("add")]
    [Produces("application/json"), Consumes("application/json")]
    [ProducesResponseType(StatusCodes.Status201Created)]
    [ProducesResponseType(StatusCodes.Status400BadRequest)]
    public async Task<ActionResult<BatteryDto>> CreateAsync([FromBody] BatteryDto batteryDto)
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
    /// <param name="id">The id of the battery object to delete.</param>
    /// <returns>Returns NoContent or NotFound.</returns>
    /// <response code="204">Returns <c>NoContent</c>.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid id or the battery object is not found.</response>
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
    /// <param name="timestamp">The timestamp of the battery object to delete.</param>
    /// <returns>Returns NoContent or NotFound</returns>
    /// <response code="204">Returns <c>NoContent</c>.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid timestamp or the battery object is not found.</response>
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
