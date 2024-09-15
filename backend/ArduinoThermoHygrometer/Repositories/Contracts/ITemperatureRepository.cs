using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Web.Repositories.Contracts;

public interface ITemperatureRepository
{
    Task<IEnumerable<Temperature>> GetAllTemperaturesAsync();

    Task<Temperature> GetTemperatureByIdAsync(Guid id);

    Task<Temperature> GetTemperatureByDateAndTimeAsync(DateTimeOffset dateTimeOffset);

    Task<Temperature> AddTemperatureAsync(Temperature battery);

    Task<Temperature> UpdateTemperatureAsync(Temperature battery);

    Task<Temperature> RemoveTemperatureByIdAsync(Guid id);

    Task<Temperature> RemoveTemperatureByDateAndTimeAsync(DateTimeOffset dateTimeOffset);

    Task<Temperature> SaveChangesAsync();
}
