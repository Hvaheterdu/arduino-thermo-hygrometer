using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Api.Repositories.Contracts;

public interface IHumidityRepository
{
    Task<IEnumerable<Humidity?>> GetAllHumiditiesAsync();

    Task<Humidity?> GetHumidityByIdAsync(Guid id);

    Task<Humidity?> GetHumidityByDateAndTimeAsync(DateTimeOffset registeredAt);

    Task<Humidity?> AddHumidityAsync(Humidity humidity);

    Humidity? UpdateHumidity(Humidity humidity);

    Humidity? RemoveHumidity(Humidity humidity);

    Task SaveChangesAsync();
}
