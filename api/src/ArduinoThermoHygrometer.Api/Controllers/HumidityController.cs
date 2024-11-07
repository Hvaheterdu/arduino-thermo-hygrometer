using ArduinoThermoHygrometer.Api.Services.Contracts;
using ArduinoThermoHygrometer.Domain.DTOs;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;

namespace ArduinoThermoHygrometer.Api.Controllers;

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

    /// <summary>
    /// Retrieves a humidity object by its id.
    /// </summary>
    /// <param name="id">The id of the humidity object to retrieve.</param>
    /// <returns>Returns humidity object or NotFound.</returns>
    /// <response code="200">Returns <c>Humidity</c> object.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid id or the humidity object is not found.</response>
    [HttpGet("{id:Guid}")]
    [Produces("application/json")]
    [ProducesResponseType(typeof(HumidityDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<HumidityDto>> GetByIdAsync(Guid id)
    {
        HumidityDto? humidityDto = await _humidityService.GetHumidityDtoByIdAsync(id);

        if (humidityDto == null)
        {
            return NotFound(humidityDto);
        }

        return Ok(humidityDto);
    }

    /// <summary>
    /// Retrieves a humidity object by its registration timestamp.
    /// </summary>
    /// <param name="timestamp">The timestamp of the humidity object to retrieve.</param>
    /// <returns>Returns humidity object or NotFound</returns>
    /// <response code="200">Returns <c>Humidity</c> object.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid timestamp or the humidity object is not found.</response>
    [HttpGet("get-by-timestamp/{timestamp:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(typeof(HumidityDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<HumidityDto>> GetByTimestampAsync(DateTimeOffset timestamp)
    {
        HumidityDto? humidityDto = await _humidityService.GetHumidityDtoByTimestampAsync(timestamp);

        if (humidityDto == null)
        {
            return NotFound(humidityDto);
        }

        return Ok(humidityDto);
    }

    /// <summary>
    /// Retrieves a list of humidity objects by its date.
    /// </summary>
    /// <param name="date">The date of the humidity objects to retrieve.</param>
    /// <returns>Returns humidity objects or NotFound</returns>
    /// <response code="200">Returns a list of <c>Humidity</c> objects.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid date or list of humidity objects is empty.</response>
    [HttpGet("get-by-date/{date:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(typeof(HumidityDto), StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<IEnumerable<HumidityDto>>> GetByDateAsync(DateTimeOffset date)
    {
        IEnumerable<HumidityDto>? humidityDto = await _humidityService.GetHumidityDtosByDateAsync(date);

        if (humidityDto == null || !humidityDto.Any())
        {
            return NotFound(humidityDto);
        }

        return Ok(humidityDto);
    }

    /// <summary>
    /// Creates a humidity object.
    /// </summary>
    /// <param name="humidityDto">The humidity object to create.</param>
    /// <returns>Returns Created or BadRequest</returns>
    /// <response code="201">Returns <c>Created</c>.</response>
    /// <response code="400">Returns <c>BadRequest</c> if invalid humidityDto object.</response>
    [HttpPost("add")]
    [Produces("application/json"), Consumes("application/json")]
    [ProducesResponseType(StatusCodes.Status201Created)]
    [ProducesResponseType(StatusCodes.Status400BadRequest)]
    public async Task<ActionResult<HumidityDto>> CreateAsync([FromBody] HumidityDto humidityDto)
    {
        HumidityDto humidityDtoCreated = await _humidityService.CreateHumidityDtoAsync(humidityDto);

        if (!ModelState.IsValid)
        {
            return BadRequest(humidityDto);
        }

        Uri uri = new($"{Request.Scheme}://{Request.Host}{Request.Path}");

        return Created(uri, humidityDtoCreated);
    }

    /// <summary>
    /// Deletes a humidity object by its id.
    /// </summary>
    /// <param name="id">The id of the humidity object to delete.</param>
    /// <returns>Returns NoContent or NotFound.</returns>
    /// <response code="204">Returns <c>NoContent</c>.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid id or the humidity object is not found.</response>
    [HttpDelete("delete/{id:guid}")]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status204NoContent)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<HumidityDto>> DeleteByIdAsync(Guid id)
    {
        HumidityDto? humidityDto = await _humidityService.DeleteHumidityDtoByIdAsync(id);

        if (humidityDto == null)
        {
            return NotFound(humidityDto);
        }

        return NoContent();
    }

    /// <summary>
    /// Deletes a humidity object by its registration timestamp.
    /// </summary>
    /// <param name="timestamp">The timestamp of the humidity object to delete.</param>
    /// <returns>Returns NoContent or NotFound</returns>
    /// <response code="204">Returns <c>NoContent</c>.</response>
    /// <response code="404">Returns <c>NotFound</c> if invalid timestamp or the humidity object is not found.</response>
    [HttpDelete("delete/{timestamp:datetime}")]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status204NoContent)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    public async Task<ActionResult<HumidityDto>> DeleteByTimestampAsync(DateTimeOffset timestamp)
    {
        HumidityDto? humidityDto = await _humidityService.DeleteHumidityDtoByTimestampAsync(timestamp);

        if (humidityDto == null)
        {
            return NotFound(humidityDto);
        }

        return NoContent();
    }
}
