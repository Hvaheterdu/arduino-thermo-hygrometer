using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface IHealthCheckService
{
    Task<HealthReport> GetHealthCheckReport();
}
