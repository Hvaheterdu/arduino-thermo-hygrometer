using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Web.DTOs;

namespace ArduinoThermoHygrometer.Web.Repositories.Contracts;

public interface IBatteryRepository
{
    Task<IEnumerable<Battery?>> GetAllBatteriesAsync();

    Task<Battery?> GetBatteryByIdAsync(Guid id);

    Task<Battery?> GetBatteryByDateAndTimeAsync(DateTimeOffset registeredAt);

    Task<Battery?> AddBatteryAsync(Battery battery);

    Battery? UpdateBatteryAsync(Battery battery);

    Battery? RemoveBattery(Battery battery);

    Task SaveChangesAsync();
}
