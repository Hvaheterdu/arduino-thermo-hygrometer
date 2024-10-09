using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface ITemperatureService
{
    Task<TemperatureDto?> GetTemperatureDtoByIdAsync(Guid id);

    Task<TemperatureDto?> GetTemperatureDtoByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<TemperatureDto>?> GetTemperatureDtosByDateAsync(DateTimeOffset dateTimeOffset);

    Task<TemperatureDto> CreateTemperatureDtoAsync(TemperatureDto temperatureDto);

    Task<TemperatureDto?> DeleteTemperatureDtoByIdAsync(Guid id);

    Task<TemperatureDto?> DeleteTemperatureDtoByTimestampAsync(DateTimeOffset timestamp);
}
