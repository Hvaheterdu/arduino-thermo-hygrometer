using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Api.Services.Contracts;

public interface IBatteryService
{
    Task<BatteryDto?> GetBatteryDtoByIdAsync(Guid id);

    Task<BatteryDto?> GetBatteryDtoByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<BatteryDto>?> GetBatteryDtosByDateAsync(DateTimeOffset dateTimeOffset);

    Task AddBatteryDtoAsync(BatteryDto batteryDto);

    Task<BatteryDto?> RemoveBatteryByIdAsync(Guid id);

    Task<BatteryDto?> RemoveBatteryByTimestampAsync(DateTimeOffset registeredAt);
}
