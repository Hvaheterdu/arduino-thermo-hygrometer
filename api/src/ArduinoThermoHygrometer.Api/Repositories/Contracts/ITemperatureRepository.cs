using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Api.Repositories.Contracts;

public interface ITemperatureRepository
{
    Task<Temperature?> GetTemperatureByIdAsync(Guid id);

    Task<Temperature?> GetTemperatureByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<Temperature?>> GetTemperaturesByTimestampsAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp);

    Task<IEnumerable<Temperature?>> GetTemperaturesByDatesAsync(DateTimeOffset startDate, DateTimeOffset endDate);

    Task<Temperature?> AddTemperatureAsync(Temperature temperature);

    Temperature? RemoveTemperature(Temperature temperature);

    Task SaveChangesAsync();
}
