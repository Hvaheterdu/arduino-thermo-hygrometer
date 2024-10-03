using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface IHumidityService
{
    Task<HumidityDto?> GetHumidityDtoByIdAsync(Guid id);

    Task<HumidityDto?> GetHumidityDtoByDateAndTimeAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<HumidityDto?>> GetAllBatteryDtosWithinTimestampRangeAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp);

    Task<HumidityDto?> AddHumidityDtoAsync(HumidityDto humidityDto);

    HumidityDto? RemoveHumidityDto(HumidityDto humidityDto);

    Task SaveChangesAsync();
}
