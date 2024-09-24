using ArduinoThermoHygrometer.Api.Services.Contracts;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Controllers;

[ApiController]
[ApiVersion(0.1)]
[Route("api/v{version:apiVersion}/health")]
public class HealthCheckController : ControllerBase
{
    private readonly IHealthCheckService _healthcheckService;

    public HealthCheckController(IHealthCheckService healthcheckService)
    {
        _healthcheckService = healthcheckService;
    }

    /// <summary>
    /// Retrieves the health check report for the application.
    /// </summary>
    /// <returns>An <see cref="IActionResult"/> containing the health check report if the report is found.</returns>
    /// <response code="200">Returns the health check report if the system is healthy.</response>
    /// <response code="404">Returns a <see cref="ProblemDetails"/> object if the health report is not found.</response>
    /// <response code="500">Returns a <see cref="ProblemDetails"/> object if the system is degraded or unhealthy.</response>
    [HttpGet]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status200OK)]
    [ProducesResponseType(typeof(ProblemDetails), StatusCodes.Status404NotFound)]
    [ProducesResponseType(typeof(ProblemDetails), StatusCodes.Status500InternalServerError)]
    public async Task<IActionResult> GetHealthCheckReport()
    {
        HealthReport? healthReport = await _healthcheckService.GetHealthCheckReport();

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
