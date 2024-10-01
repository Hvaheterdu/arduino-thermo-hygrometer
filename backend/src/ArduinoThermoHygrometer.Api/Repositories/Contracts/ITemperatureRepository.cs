using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Api.Repositories.Contracts;

public interface ITemperatureRepository
{
    Task<IEnumerable<Temperature?>> GetAllTemperaturesAsync();

    Task<Temperature?> GetTemperatureByIdAsync(Guid id);

    Task<Temperature?> GetTemperatureByDateAndTimeAsync(DateTimeOffset registeredAt);

    Task<Temperature?> AddTemperatureAsync(Temperature temperature);

    Temperature? UpdateTemperature(Temperature temperature);

    Temperature? RemoveTemperature(Temperature temperature);

    Task SaveChangesAsync();
}
