using ArduinoThermoHygrometer.Api.Services.Contracts;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Controllers;

[ApiController]
[ApiVersion(0.1)]
[Route("api/v{version:apiVersion}/health")]
public class HealthcheckController : ControllerBase
{
    private readonly IHealthcheckService _healthcheckService;

    public HealthcheckController(IHealthcheckService healthcheckService)
    {
        _healthcheckService = healthcheckService;
    }

    /// <summary>
    /// Retrieves the health check report for the system.
    /// </summary>
    /// <returns>An <see cref="IActionResult"/> containing the health check report.</returns>
    /// <response code="200">Returns the health check report if the system is healthy.</response>
    /// <response code="500">Returns a <see cref="ProblemDetails"/> object if the system is degraded or unhealthy.</response>
    [HttpGet]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status200OK)]
    [ProducesResponseType(typeof(ProblemDetails), StatusCodes.Status500InternalServerError)]
    public async Task<IActionResult> GetHealthCheckReportAsync()
    {
        HealthReport healthReport = await _healthcheckService.GetHealthCheckReportAsync();

        if (healthReport.Status is HealthStatus.Degraded or HealthStatus.Unhealthy)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, healthReport);
        }

        return Ok(healthReport);
    }
}
