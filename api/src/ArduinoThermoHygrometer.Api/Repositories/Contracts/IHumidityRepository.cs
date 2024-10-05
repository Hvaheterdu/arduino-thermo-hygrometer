using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Api.Repositories.Contracts;

public interface IHumidityRepository
{
    Task<Humidity?> GetHumidityByIdAsync(Guid id);

    Task<Humidity?> GetHumidityByDateAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<Humidity?>> GetHumiditiesByDatesAsync(DateTimeOffset startDate, DateTimeOffset endDate);

    Task<Humidity?> GetHumidityByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<Humidity?>> GetHumiditiesByTimestampsAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp);

    Task<Humidity?> AddHumidityAsync(Humidity humidity);

    Humidity? RemoveHumidity(Humidity humidity);

    Task SaveChangesAsync();
}
