using ArduinoThermoHygrometer.Domain.Entities;

namespace ArduinoThermoHygrometer.Web.Repositories.Contracts;

public interface IBatteryRepository
{
    Task<IEnumerable<Battery>> GetAllBatteriesAsync();

    Task<Battery> GetBatteryByIdAsync(Guid id);

    Task<Battery> GetBatteryByDateAndTimeAsync(DateTimeOffset dateTimeOffset);

    Task<Battery> AddBatteryAsync(Battery battery);

    Task<Battery> UpdateBatteryAsync(Battery battery);

    Task<Battery> RemoveBatteryByIdAsync(Guid id);

    Task<Battery> RemoveBatteryByDateAndTimeAsync(DateTimeOffset dateTimeOffset);

    Task<Battery> SaveChangesAsync();
}