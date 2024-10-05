using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Api.Repositories.Contracts;

public interface ITemperatureRepository
{
    Task<Temperature?> GetTemperatureByIdAsync(Guid id);

    Task<Temperature?> GetTemperatureByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<Temperature>> GetTemperaturesByDateAsync(DateTimeOffset dateTimeOffset);

    Task<Temperature?> AddTemperatureAsync(Temperature temperature);

    Temperature? RemoveTemperature(Temperature temperature);

    Task SaveChangesAsync();
}
