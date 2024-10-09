using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface IHumidityService
{
    Task<HumidityDto?> GetHumidityDtoByIdAsync(Guid id);

    Task<HumidityDto?> GetHumidityDtoByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<HumidityDto>?> GetHumidityDtosByDateAsync(DateTimeOffset dateTimeOffset);

    Task<HumidityDto> CreateHumidityDtoAsync(HumidityDto humidityDto);

    Task<HumidityDto?> DeleteHumidityDtoByIdAsync(Guid id);

    Task<HumidityDto?> DeleteHumidityDtoByTimestampAsync(DateTimeOffset timestamp);
}
