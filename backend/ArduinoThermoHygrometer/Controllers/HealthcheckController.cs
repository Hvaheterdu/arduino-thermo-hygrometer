using ArduinoThermoHygrometer.Api.Services.Contracts;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Diagnostics.HealthChecks;

#pragma warning disable CA2007

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
    /// Retrieves the health check report.
    /// </summary>
    /// <returns>The health report.</returns>
    /// <response code="200">Returns the health report.</response>
    [HttpGet]
    [ProducesResponseType(StatusCodes.Status200OK)]
    public async Task<IActionResult> GetHealthCheckReport()
    {
        HealthReport healthReport = await _healthcheckService.GetHealthcheckReport();

        return Ok(healthReport);
    }
}
