using ArduinoThermoHygrometer.Api.Services.Contracts;
using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Services;

public class HealthcheckService : IHealthcheckService
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
    public async Task<HealthReport?> GetHealthcheckReport()
    {
        HealthReport? healthReport = await _healthCheckService.CheckHealthAsync();

        return healthReport;
    }
}
