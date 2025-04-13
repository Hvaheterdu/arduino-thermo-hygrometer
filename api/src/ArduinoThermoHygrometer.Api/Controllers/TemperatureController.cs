using ArduinoThermoHygrometer.Api.Attributes;
using ArduinoThermoHygrometer.Core.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;

namespace ArduinoThermoHygrometer.Api.Controllers;

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

    /// <summary>
    /// Retrieves a temperature object by its id.
    /// </summary>
    /// <param name="id">The id of the temperature object to retrieve.</param>
    /// <returns>Returns temperature object or NotFound.</returns>
    /// <response code="200">Returns <c>Temperature</c> object.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid id or the temperature object is not found.</response>
    [HttpGet("{id:Guid}")]
    [Produces("application/json")]
    [ProducesResponseType(typeof(TemperatureDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<TemperatureDto>> GetByIdAsync([FromRoute] Guid id)
    {
        TemperatureDto? temperatureDto = await _temperatureService.GetTemperatureDtoByIdAsync(id);

        if (temperatureDto == null)
        {
            return NotFound(temperatureDto);
        }

        return Ok(temperatureDto);
    }

    /// <summary>
    /// Retrieves a temperature object by its registration timestamp.
    /// </summary>
    /// <param name="timestamp">The timestamp of the temperature object to retrieve.</param>
    /// <returns>Returns temperature object or NotFound</returns>
    /// <response code="200">Returns <c>Temperature</c> object.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid timestamp or the temperature object is not found.</response>
    [HttpGet("get-by-timestamp/{timestamp:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(typeof(TemperatureDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<TemperatureDto>> GetByTimestampAsync([FromRoute] DateTimeOffset timestamp)
    {
        TemperatureDto? temperatureDto = await _temperatureService.GetTemperatureDtoByTimestampAsync(timestamp);

        if (temperatureDto == null)
        {
            return NotFound(temperatureDto);
        }

        return Ok(temperatureDto);
    }

    /// <summary>
    /// Retrieves a list of temperature objects by its date.
    /// </summary>
    /// <param name="date">The date of the temperature objects to retrieve.</param>
    /// <returns>Returns temperature objects or NotFound</returns>
    /// <response code="200">Returns a list of <c>Temperature</c> objects.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid date or list of temperature objects is empty.</response>
    [HttpGet("get-by-date/{date:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(typeof(TemperatureDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<IEnumerable<TemperatureDto>>> GetByDateAsync([FromRoute] DateTimeOffset date)
    {
        IEnumerable<TemperatureDto>? temperatureDto = await _temperatureService.GetTemperatureDtosByDateAsync(date);

        if (temperatureDto == null || !temperatureDto.Any())
        {
            return NotFound(temperatureDto);
        }

        return Ok(temperatureDto);
    }

    /// <summary>
    /// Creates a temperature object.
    /// </summary>
    /// <param name="temperatureDto">The temperature object to create.</param>
    /// <returns>Returns Created or BadRequest</returns>
    /// <response code="201">Returns <c>Created</c>.</response>
    /// <response code="400">Returns <c>BadRequest</c> if invalid temperatureDto object.</response>
    [ApiKeyAuthorization]
    [HttpPost("add")]
    [Produces("application/json"), Consumes("application/json")]
    [ProducesResponseType(StatusCodes.Status201Created)]
    [ProducesResponseType(StatusCodes.Status400BadRequest)]
    public async Task<ActionResult<TemperatureDto>> CreateAsync([FromBody] TemperatureDto temperatureDto)
    {
        TemperatureDto temperatureDtoCreated = await _temperatureService.CreateTemperatureDtoAsync(temperatureDto);

        if (!ModelState.IsValid)
        {
            return BadRequest(temperatureDto);
        }

        Uri uri = new($"{Request.Scheme}://{Request.Host}{Request.Path}");

        return Created(uri, temperatureDtoCreated);
    }

    /// <summary>
    /// Deletes a temperature object by its id.
    /// </summary>
    /// <param name="id">The id of the temperature object to delete.</param>
    /// <returns>Returns NoContent or NotFound.</returns>
    /// <response code="204">Returns <c>NoContent</c>.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid id or the temperature object is not found.</response>
    [ApiKeyAuthorization]
    [HttpDelete("delete/{id:guid}")]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status204NoContent)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<TemperatureDto>> DeleteByIdAsync([FromRoute] Guid id)
    {
        TemperatureDto? temperatureDto = await _temperatureService.DeleteTemperatureDtoByIdAsync(id);

        if (temperatureDto == null)
        {
            return NotFound(temperatureDto);
        }

        return NoContent();
    }

    /// <summary>
    /// Deletes a temperature object by its registration timestamp.
    /// </summary>
    /// <param name="timestamp">The timestamp of the temperature object to delete.</param>
    /// <returns>Returns NoContent or NotFound</returns>
    /// <response code="204">Returns <c>NoContent</c>.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid timestamp or the temperature object is not found.</response>
    [ApiKeyAuthorization]
    [HttpDelete("delete/{timestamp:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status204NoContent)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<TemperatureDto>> DeleteByTimestampAsync(DateTimeOffset timestamp)
    {
        TemperatureDto? temperatureDto = await _temperatureService.DeleteTemperatureDtoByTimestampAsync(timestamp);

        if (temperatureDto == null)
        {
            return NotFound(temperatureDto);
        }

        return NoContent();
    }
}
