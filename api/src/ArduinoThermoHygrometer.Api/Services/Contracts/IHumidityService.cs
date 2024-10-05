using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface IHumidityService
{
    Task<HumidityDto?> GetHumidityDtoByIdAsync(Guid id);

    Task<HumidityDto?> GetHumidityDtoByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<HumidityDto>?> GetHumidityDtosByDateAsync(DateTimeOffset dateTimeOffset);

    Task<HumidityDto?> AddHumidityDtoAsync(HumidityDto humidityDto);

    HumidityDto? RemoveHumidityDto(HumidityDto humidityDto);
}
