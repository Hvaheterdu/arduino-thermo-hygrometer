using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Web.DTOs;

namespace ArduinoThermoHygrometer.Web.Repositories.Contracts;

public interface ITemperatureRepository
{
    Task<IEnumerable<Temperature?>> GetAllTemperaturesAsync();

    Task<Temperature?> GetTemperatureByIdAsync(Guid id);

    Task<Temperature?> GetTemperatureByDateAndTimeAsync(DateTimeOffset registeredAt);

    Task<Temperature?> AddTemperatureAsync(Temperature temperature);

    Temperature? UpdateTemperatureAsync(Temperature temperature);

    Temperature? RemoveTemperature(Temperature temperature);

    Task SaveChangesAsync();
}
