using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Core.Repositories.Contracts;

public interface ITemperatureRepository
{
    Task<Temperature?> GetTemperatureByIdAsync(Guid id);

    Task<Temperature?> GetTemperatureByTimestampAsync(DateTimeOffset timestamp);

    Task<IEnumerable<Temperature>> GetTemperatureByDateAsync(DateTimeOffset dateTimeOffset);

    Task CreateTemperatureAsync(Temperature temperature);

    Task<Temperature?> DeleteTemperatureByIdAsync(Guid id);

    Task<Temperature?> DeleteTemperatureByTimestampAsync(DateTimeOffset timestamp);

    Task SaveChangesAsync();
}
