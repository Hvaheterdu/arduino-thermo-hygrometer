using ArduinoThermoHygrometer.Api.Extensions;
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
        LoggingExtensions.LogHealthCheckReportRetrieving(_logger);

        HealthReport? healthReport = await _healthCheckService.CheckHealthAsync();

        if (healthReport == null)
        {
            LoggingExtensions.LogHealthCheckReportNull(_logger);
        }

        if (healthReport?.Status is HealthStatus.Degraded or HealthStatus.Unhealthy)
        {
            LoggingExtensions.LogHealthCheckReportStatus(_logger, healthReport.Status);
        }

        LoggingExtensions.LogHealthCheckReportRetrieved(_logger);

        return healthReport;
    }
}
