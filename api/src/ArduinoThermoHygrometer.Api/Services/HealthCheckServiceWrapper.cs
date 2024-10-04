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
    /// Asynchronously retrieves a health check report.
    /// </summary>
    /// <returns>Returns the <see cref="HealthReport"/> which provides the health check status.</returns>
    public async Task<HealthReport> GetHealthCheckReportAsync()
    {
        LoggingExtensions.LogRetrievingHealthCheckReport(_logger);

        HealthReport healthReport = await _healthCheckService.CheckHealthAsync();

        if (healthReport.Status is HealthStatus.Degraded or HealthStatus.Unhealthy)
        {
            LoggingExtensions.LogUnhealthyOrDegradedHealthCheckReportStatus(_logger, healthReport.Status);
        }

        LoggingExtensions.LogRetrievedHealthCheckReport(_logger);

        return healthReport;
    }
}
