using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Api.Repositories.Contracts;

public interface IBatteryRepository
{
    Task<Battery?> GetBatteryByIdAsync(Guid id);

    Task<Battery?> GetBatteryByDateAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<Battery?>> GetBatteriesByDatesAsync(DateTimeOffset startDate, DateTimeOffset endDate);

    Task<Battery?> GetBatteryByTimestampAsync(DateTimeOffset registeredAt);

    Task<IEnumerable<Battery?>> GetBatteriesByTimestampsAsync(DateTimeOffset startTimestamp, DateTimeOffset endTimestamp);

    Task<Battery?> AddBatteryAsync(Battery battery);

    Battery? RemoveBattery(Battery battery);

    Task SaveChangesAsync();
}
