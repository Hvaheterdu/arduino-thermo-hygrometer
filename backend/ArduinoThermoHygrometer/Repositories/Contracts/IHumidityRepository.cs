using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Web.DTOs;

namespace ArduinoThermoHygrometer.Web.Repositories.Contracts;

public interface IHumidityRepository
{
    Task<IEnumerable<Humidity?>> GetAllHumiditiesAsync();

    Task<Humidity?> GetHumidityByIdAsync(Guid id);

    Task<Humidity?> GetHumidityByDateAndTimeAsync(DateTimeOffset dateTimeOffset);

    Task<Humidity?> AddHumidityAsync(HumidityDto humidityDto);

    Task<Humidity?> UpdateHumidityAsync(HumidityDto humidityDto);

    Humidity? RemoveHumidity(HumidityDto humidityDto);

    Task SaveChangesAsync();
}
