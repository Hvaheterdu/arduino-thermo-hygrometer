using ArduinoThermoHygrometer.Api.Services.Contracts;
using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Services;

public class HealthcheckService : IHealthcheckService
{
    private readonly HealthCheckService _healthCheckService;

    public HealthcheckService(HealthCheckService healthCheckService)
    {
        _healthCheckService = healthCheckService;
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
