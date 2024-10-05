using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface ITemperatureService
{
    Task<TemperatureDto?> GetTemperatureDtoByIdAsync(Guid id);

    Task<TemperatureDto?> GetTemperatureDtoByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<TemperatureDto>?> GetTemperatureDtosByDateAsync(DateTimeOffset dateTimeOffset);

    Task<TemperatureDto?> AddTemperatureDtoAsync(TemperatureDto temperatureDto);

    TemperatureDto? RemoveTemperatureDto(TemperatureDto temperatureDto);
}
