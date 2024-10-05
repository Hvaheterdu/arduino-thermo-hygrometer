using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Api.Repositories.Contracts;

public interface IBatteryRepository
{
    Task<Battery?> GetBatteryByIdAsync(Guid id);

    Task<Battery?> GetBatteryByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<Battery>> GetBatteriesByDateAsync(DateTimeOffset dateTimeOffset);

    Task<Battery?> AddBatteryAsync(Battery battery);

    Battery? RemoveBattery(Battery battery);

    Task SaveChangesAsync();
}
