using ArduinoThermoHygrometer.Domain.DTOs;

namespace ArduinoThermoHygrometer.Core.Services.Contracts;

public interface IBatteryService
{
    Task<BatteryDto?> GetBatteryDtoByIdAsync(Guid id);

    Task<BatteryDto?> GetBatteryDtoByTimestampAsync(DateTimeOffset timestamp);

    Task<IEnumerable<BatteryDto>?> GetBatteryDtosByDateAsync(DateTimeOffset dateTimeOffset);

    Task<BatteryDto> CreateBatteryDtoAsync(BatteryDto batteryDto);

    Task<BatteryDto?> DeleteBatteryDtoByIdAsync(Guid id);

    Task<BatteryDto?> DeleteBatteryDtoByTimestampAsync(DateTimeOffset timestamp);
}
