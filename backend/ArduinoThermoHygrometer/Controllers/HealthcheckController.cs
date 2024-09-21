using ArduinoThermoHygrometer.Api.Services.Contracts;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Controllers;

[ApiController]
[ApiVersion(0.1)]
[Route("api/v{version:apiVersion}/_health")]
public class HealthcheckController : ControllerBase
{
    private readonly IHealthcheckService _healthcheckService;

    public HealthcheckController(IHealthcheckService healthcheckService)
    {
        _healthcheckService = healthcheckService;
    }

    /// <summary>
    /// Retrieves the health check report for the application.
    /// </summary>
    /// <returns>An <see cref="IActionResult"/> containing the health check report if the report is found.</returns>
    /// <response code="200">Returns the health check report with healthy status.</response>
    /// <response code="404">Returns null.</response>
    /// <response code="500">Returns the health check report with degraded or unhealthy status.</response>
    [HttpGet]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status200OK)]
    [ProducesResponseType(StatusCodes.Status404NotFound)]
    [ProducesResponseType(StatusCodes.Status500InternalServerError)]
    public async Task<IActionResult> GetHealthCheckReport()
    {
        HealthReport? healthReport = await _healthcheckService.GetHealthcheckReport();

        if (healthReport is null)
        {
            return NotFound(healthReport);
        }

        if (healthReport.Status is HealthStatus.Degraded or HealthStatus.Unhealthy)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, healthReport);
        }

        return Ok(healthReport);
    }
}
