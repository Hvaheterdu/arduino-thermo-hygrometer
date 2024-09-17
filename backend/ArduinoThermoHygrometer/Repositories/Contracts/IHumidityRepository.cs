using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Web.DTOs;

namespace ArduinoThermoHygrometer.Web.Repositories.Contracts;

public interface IHumidityRepository
{
    Task<IEnumerable<Humidity?>> GetAllHumiditiesAsync();

    Task<Humidity?> GetHumidityByIdAsync(Guid id);

    Task<Humidity?> GetHumidityByDateAndTimeAsync(DateTimeOffset dateTimeOffset);

    Task<Humidity?> AddHumidityAsync(Humidity humidity);

    Humidity? UpdateHumidityAsync(Humidity humidity);

    Humidity? RemoveHumidity(Humidity humidity);

    Task SaveChangesAsync();
}
