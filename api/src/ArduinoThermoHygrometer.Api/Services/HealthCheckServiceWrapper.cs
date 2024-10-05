using ArduinoThermoHygrometer.Api.Extensions;
using ArduinoThermoHygrometer.Api.Services.Contracts;
using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Services;

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
        LoggingExtensions.LogRetrievingHealthCheckReport(_logger);

        HealthReport healthReport = await _healthCheckService.CheckHealthAsync();

        if (healthReport.Status is HealthStatus.Degraded or HealthStatus.Unhealthy)
        {
            LoggingExtensions.LogHealthCheckReportStatus(_logger, healthReport.Status);
        }

        LoggingExtensions.LogRetrievedHealthCheckReport(_logger);

        return healthReport;
    }
}
