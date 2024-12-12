using ArduinoThermoHygrometer.Core.Services.Contracts;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Controllers;

[ApiController]
[ApiVersion(0.1)]
[Route("api/v{version:apiVersion}/health")]
public class HealthCheckController : ControllerBase
{
    private readonly IHealthCheckService _healthCheckService;

    public HealthCheckController(IHealthCheckService healthCheckService)
    {
        _healthCheckService = healthCheckService;
    }

    /// <summary>
    /// Retrieves the healthcheck report for the system.
    /// </summary>
    /// <returns>The healthcheck report.</returns>
    /// <response code="200">Returns <c>HealthReport</c> if the system is healthy.</response>
    /// <response code="500">Returns <c>ProblemDetails</c> if the system is degraded or unhealthy.</response>
    [HttpGet]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status200OK)]
    [ProducesResponseType(typeof(ProblemDetails), StatusCodes.Status500InternalServerError)]
    public async Task<IActionResult> GetHealthCheckReportAsync()
    {
        HealthReport healthCheckReport = await _healthCheckService.GetHealthCheckReportAsync();

        if (healthCheckReport.Status is HealthStatus.Degraded or HealthStatus.Unhealthy)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, healthCheckReport);
        }

        return Ok(healthCheckReport);
    }
}
