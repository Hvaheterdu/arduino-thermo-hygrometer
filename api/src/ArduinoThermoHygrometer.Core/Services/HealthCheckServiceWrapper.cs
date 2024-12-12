using ArduinoThermoHygrometer.Core.Logging;
using ArduinoThermoHygrometer.Core.Services.Contracts;
using Microsoft.Extensions.Diagnostics.HealthChecks;
using Microsoft.Extensions.Logging;

namespace ArduinoThermoHygrometer.Core.Services;

public class HealthCheckServiceWrapper : IHealthCheckService
{
    private readonly HealthCheckService _healthCheckService;
    private readonly ILogger<HealthCheckServiceWrapper> _logger;

    public HealthCheckServiceWrapper(HealthCheckService healthCheckService, ILogger<HealthCheckServiceWrapper> logger)
    {
        _healthCheckService = healthCheckService;
        _logger = logger;
    }

    /// <summary>
    /// Retrieves a healthcheck report asynchronously.
    /// </summary>
    /// <returns>Returns the <see cref="HealthReport"/> which provides the healthcheck status.</returns>
    public async Task<HealthReport> GetHealthCheckReportAsync()
    {
        HealthReport healthReport = await _healthCheckService.CheckHealthAsync();

        if (healthReport.Status is HealthStatus.Degraded or HealthStatus.Unhealthy)
        {
            LoggingExtensions.LogHealthCheckStatusDegradedOrUnhealthy(_logger, healthReport.Status);
        }

        return healthReport;
    }
}
