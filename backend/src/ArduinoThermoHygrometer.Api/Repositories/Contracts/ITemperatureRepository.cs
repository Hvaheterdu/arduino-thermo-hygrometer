using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Api.Repositories.Contracts;

public interface ITemperatureRepository
{
    Task<Temperature?> GetTemperatureByIdAsync(Guid id);

    Task<Temperature?> GetTemperatureByDateAndTimeAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<Temperature?>> GetAllTemperaturesWithinTimestampRangeAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp);

    Task<Temperature?> AddTemperatureAsync(Temperature temperature);

    Temperature? RemoveTemperature(Temperature temperature);

    Task SaveChangesAsync();
}
