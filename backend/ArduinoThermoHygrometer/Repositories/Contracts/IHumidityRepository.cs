using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Web.Repositories.Contracts;

public interface IHumidityRepository
{
    Task<IEnumerable<Humidity>> GetAllHumiditiesAsync();

    Task<Humidity> GetHumidityByIdAsync(Guid id);

    Task<Humidity> GetHumidityByDateAndTimeAsync(DateTimeOffset dateTimeOffset);

    Task<Humidity> AddHumidityAsync(Humidity battery);

    Task<Humidity> UpdateHumidityAsync(Humidity battery);

    Task<Humidity> RemoveHumidityByIdAsync(Guid id);

    Task<Humidity> RemoveHumidityByDateAndTimeAsync(DateTimeOffset dateTimeOffset);

    Task<Humidity> SaveChangesAsync();
}
