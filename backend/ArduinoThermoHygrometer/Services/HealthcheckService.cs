using ArduinoThermoHygrometer.Api.Services.Contracts;
using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Services;

public class HealthcheckService : IHealthCheckService
{
    private readonly HealthCheckService _healthCheckService;
    private readonly ILogger<HealthcheckService> _logger;

    public HealthcheckService(HealthCheckService healthCheckService, ILogger<HealthcheckService> logger)
    {
        _healthCheckService = healthCheckService;
        _logger = logger;
    }

    /// <summary>
    /// Asynchronously retrieves a health check report.
    /// </summary>
    /// <returns>Returns the <see cref="HealthReport"/> which provides the health check status.</returns>
    public async Task<HealthReport?> GetHealthCheckReport()
    {
        _logger.LogInformation("Retrieving health check report.");

        HealthReport? healthReport = await _healthCheckService.CheckHealthAsync();

        if (healthReport == null)
        {
            _logger.LogError("Health check report is null.");
        }

        if (healthReport?.Status is HealthStatus.Degraded or HealthStatus.Unhealthy)
        {
            _logger.LogError("Health check report status is {Status}.", healthReport.Status);
        }

        _logger.LogInformation("Retrieved health check report.");

        return healthReport;
    }
}
