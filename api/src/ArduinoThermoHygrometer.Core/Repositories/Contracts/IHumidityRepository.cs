using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Core.Repositories.Contracts;

public interface IHumidityRepository
{
    Task<Humidity?> GetHumidityByIdAsync(Guid id);

    Task<Humidity?> GetHumidityByTimestampAsync(DateTimeOffset timestamp);

    Task<IEnumerable<Humidity>> GetHumiditiesByDateAsync(DateTimeOffset dateTimeOffset);

    Task CreateHumidityAsync(Humidity humidity);

    Task<Humidity?> DeleteHumidityByIdAsync(Guid id);

    Task<Humidity?> DeleteHumidityByTimestampAsync(DateTimeOffset timestamp);

    Task SaveChangesAsync();
}
