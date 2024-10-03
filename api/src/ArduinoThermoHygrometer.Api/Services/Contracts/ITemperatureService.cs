using ArduinoThermoHygrometer.Api.DTOs;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface ITemperatureService
{
    Task<TemperatureDto?> GetTemperatureDtoByIdAsync(Guid id);

    Task<TemperatureDto?> GetTemperatureDtoByDateAndTimeAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<TemperatureDto?>> GetAllTemperatureDtosWithinTimestampRangeAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp);

    Task<TemperatureDto?> AddTemperatureDtoAsync(TemperatureDto temperatureDto);

    TemperatureDto? RemoveTemperatureDto(TemperatureDto temperatureDto);

    Task SaveChangesAsync();
}
