using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface IHumidityService
{
    Task<HumidityDto?> GetHumidityDtoByIdAsync(Guid id);

    Task<HumidityDto?> GetHumidityByDateAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<HumidityDto?>> GetHumidityDtosByDatesAsync(DateTimeOffset startDate, DateTimeOffset endDate);

    Task<HumidityDto?> GetHumidityDtoByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<HumidityDto?>> GetHumidityDtosByTimestampsAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp);

    Task<HumidityDto?> AddHumidityDtoAsync(HumidityDto humidityDto);

    HumidityDto? RemoveHumidityDto(HumidityDto humidityDto);
}
