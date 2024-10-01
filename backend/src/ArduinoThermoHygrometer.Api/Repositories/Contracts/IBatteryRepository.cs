using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Api.Repositories.Contracts;

public interface IBatteryRepository
{
    Task<Battery?> GetBatteryByIdAsync(Guid id);

    Task<Battery?> GetBatteryByDateAndTimeAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<Battery?>> GetAllBatteriesWithinTimestampRangeAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp);

    Task<Battery?> AddBatteryAsync(Battery battery);

    Battery? RemoveBattery(Battery battery);

    Task SaveChangesAsync();
}
