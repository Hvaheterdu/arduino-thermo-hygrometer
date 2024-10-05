using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface ITemperatureService
{
    Task<TemperatureDto?> GetTemperatureDtoByIdAsync(Guid id);

    Task<TemperatureDto?> GetTemperatureDtoByDateAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<TemperatureDto?>> GetTemperatureDtosByDatesAsync(DateTimeOffset startDate, DateTimeOffset endDate);

    Task<TemperatureDto?> GetTemperatureDtoByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<TemperatureDto?>> GetTemperatureDtosByTimestampsAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp);

    Task<TemperatureDto?> AddTemperatureDtoAsync(TemperatureDto temperatureDto);

    TemperatureDto? RemoveTemperatureDto(TemperatureDto temperatureDto);
}
