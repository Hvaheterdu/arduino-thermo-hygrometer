using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Web.Services.Contracts;

public interface IHealthcheckService
{
    Task<HealthReport> GetHealthcheckReport();
}
