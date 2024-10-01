using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Api.Repositories.Contracts;

public interface IHumidityRepository
{
    Task<Humidity?> GetHumidityByIdAsync(Guid id);

    Task<Humidity?> GetHumidityByDateAndTimeAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<Humidity?>> GetAllHumiditiesWithinTimestampRangeAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp);

    Task<Humidity?> AddHumidityAsync(Humidity humidity);

    Humidity? RemoveHumidity(Humidity humidity);

    Task SaveChangesAsync();
}
