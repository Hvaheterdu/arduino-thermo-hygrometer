using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface IBatteryService
{
    Task<BatteryDto?> GetBatteryDtoByIdAsync(Guid id);

    Task<BatteryDto?> GetBatteryDtoByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<BatteryDto?>> GetBatteryDtosByTimestampsAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp);

    Task<IEnumerable<BatteryDto?>> GetBatteryDtosByDatesAsync(DateTimeOffset startDate, DateTimeOffset endDate);

    Task<BatteryDto?> AddBatteryDtoAsync(BatteryDto batteryDto);

    BatteryDto? RemoveBatteryDto(BatteryDto batteryDto);
}
