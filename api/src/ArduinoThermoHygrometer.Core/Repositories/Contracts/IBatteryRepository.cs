using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Core.Repositories.Contracts;

public interface IBatteryRepository
{
    Task<Battery?> GetBatteryByIdAsync(Guid id);

    Task<Battery?> GetBatteryByTimestampAsync(DateTimeOffset timestamp);

    Task<IEnumerable<Battery>> GetBatteriesByDateAsync(DateTimeOffset dateTimeOffset);

    Task CreateBatteryAsync(Battery battery);

    Task<Battery?> DeleteBatteryByIdAsync(Guid id);

    Task<Battery?> DeleteBatteryByTimestampAsync(DateTimeOffset timestamp);

    Task SaveChangesAsync();
}
