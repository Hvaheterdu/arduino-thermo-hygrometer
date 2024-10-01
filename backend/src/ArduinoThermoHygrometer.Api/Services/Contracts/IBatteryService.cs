using ArduinoThermoHygrometer.Api.DTOs;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface IBatteryService
{
    Task<BatteryDto?> GetBatteryDtoByIdAsync(Guid id);

    Task<BatteryDto?> GetBatteryDtoByDateAndTimeAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<BatteryDto?>> GetAllBatteryDtosWithinTimestampRangeAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp);

    Task<BatteryDto?> AddBatteryDtoAsync(BatteryDto batteryDto);

    BatteryDto? RemoveBatteryDto(BatteryDto batteryDto);

    Task SaveChangesAsync();
}
