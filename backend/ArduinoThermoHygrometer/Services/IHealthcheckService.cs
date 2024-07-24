using Microsoft.Extensions.Diagnostics.HealthChecks;

namespace ArduinoThermoHygrometer.Web.Services;

public interface IHealthcheckService
{
    Task<HealthReport> GetHealthcheckReport();
}
