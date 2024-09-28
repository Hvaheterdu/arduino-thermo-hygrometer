using ArduinoThermoHygrometer.Api.Extensions;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Controllers;

[ApiController]
[ApiVersion(0.1)]
[Route("api/v{version:apiVersion}/health")]
public class HealthCheckController : ControllerBase
{
    private readonly HealthCheckService _healthCheckService;
    private readonly ILogger<HealthCheckController> _logger;

    public HealthCheckController(HealthCheckService healthCheckService, ILogger<HealthCheckController> logger)
    {
        _healthCheckService = healthCheckService;
        _logger = logger;
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
        LoggingExtensions.LogHealthCheckReportRetrieving(_logger);

        HealthReport healthCheckReport = await _healthCheckService.CheckHealthAsync();

        if (healthCheckReport.Status is HealthStatus.Degraded or HealthStatus.Unhealthy)
        {
            LoggingExtensions.LogHealthCheckReportStatus(_logger, healthCheckReport.Status);
            return StatusCode(StatusCodes.Status500InternalServerError, healthCheckReport);
        }

        LoggingExtensions.LogHealthCheckReportRetrieved(_logger);

        return Ok(healthCheckReport);
    }
}
