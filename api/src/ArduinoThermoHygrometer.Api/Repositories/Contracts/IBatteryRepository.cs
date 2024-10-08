using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Api.Repositories.Contracts;

public interface IBatteryRepository
{
    Task<Battery?> GetBatteryByIdAsync(Guid id);

    Task<Battery?> GetBatteryByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<Battery>> GetBatteriesByDateAsync(DateTimeOffset dateTimeOffset);

    Task AddBatteryAsync(Battery battery);

    Task<Battery?> RemoveBatteryByIdAsync(Guid id);

    Task<Battery?> RemoveBatteryByTimestampAsync(DateTimeOffset registeredAt);

    Task SaveChangesAsync();
}
