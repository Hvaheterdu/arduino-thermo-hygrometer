using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Web.DTOs;

namespace ArduinoThermoHygrometer.Web.Repositories.Contracts;

public interface ITemperatureRepository
{
    Task<IEnumerable<Temperature?>> GetAllTemperaturesAsync();

    Task<Temperature?> GetTemperatureByIdAsync(Guid id);

    Task<Temperature?> GetTemperatureByDateAndTimeAsync(DateTimeOffset dateTimeOffset);

    Task<Temperature?> AddTemperatureAsync(TemperatureDto temperatureDto);

    Task<Temperature?> UpdateTemperatureAsync(TemperatureDto temperatureDto);

    Temperature? RemoveTemperature(TemperatureDto temperatureDto);

    Task SaveChangesAsync();
}
