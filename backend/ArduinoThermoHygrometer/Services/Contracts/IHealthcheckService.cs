using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface IHealthcheckService
{
    Task<HealthReport> GetHealthcheckReport();
}
