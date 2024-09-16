using ArduinoThermoHygrometer.Domain.Entities;
using ArduinoThermoHygrometer.Web.DTOs;

namespace ArduinoThermoHygrometer.Web.Repositories.Contracts;

public interface IBatteryRepository
{
    Task<IEnumerable<Battery?>> GetAllBatteriesAsync();

    Task<Battery?> GetBatteryByIdAsync(Guid id);

    Task<Battery?> GetBatteryByDateAndTimeAsync(DateTimeOffset dateTimeOffset);

    Task<Battery?> AddBatteryAsync(BatteryDto batteryDto);

    Task<Battery?> UpdateBatteryAsync(BatteryDto batteryDto);

    Battery? RemoveBattery(BatteryDto batteryDto);

    Task SaveChangesAsync();
}
