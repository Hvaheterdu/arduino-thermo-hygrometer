using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Web.Services;

public class HealthcheckService : IHealthcheckService
{
    private readonly HealthCheckService _healthCheckService;

    public HealthcheckService(HealthCheckService healthCheckService)
    {
        _healthCheckService = healthCheckService;
    }

    public async Task<HealthReport> GetHealthcheckReport()
    {
        return await _healthCheckService.CheckHealthAsync();
    }
}
