using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Core.Services.Contracts;

public interface IHealthCheckService
{
    Task<HealthReport> GetHealthCheckReportAsync();
}
