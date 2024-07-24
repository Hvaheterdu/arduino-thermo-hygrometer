using ArduinoThermoHygrometer.Web.Services;
using Asp.Versioning;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Web.Controllers;

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
    /// Get Healthcheck status local development application.
    /// </summary>
    /// <returns>JSON representation of healthcheck status.</returns>
    [HttpGet]
    [Consumes("application/json")]
    [Produces("application/json")]
    [ProducesResponseType(StatusCodes.Status200OK)]
    public async Task<ActionResult> GetHealthCheckReport()
    {
        HealthReport healthReport = await _healthcheckService.GetHealthcheckReport();

        return Ok(healthReport);
    }
}
